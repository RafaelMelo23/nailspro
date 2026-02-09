package com.rafael.nailspro.webapp.application.client;

import com.rafael.nailspro.webapp.application.appointment.AppointmentService;
import com.rafael.nailspro.webapp.application.appointment.BookingPolicyManager;
import com.rafael.nailspro.webapp.application.professional.ProfessionalAppointmentUseCase;
import com.rafael.nailspro.webapp.application.professional.WorkScheduleService;
import com.rafael.nailspro.webapp.application.service.SalonProfileService;
import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.model.*;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.domain.repository.ClientRepository;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentTimesDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract.BusyInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.SimpleBusyInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.TimeInterval;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClientAppointmentUseCase {

    private final ClientRepository clientRepository;
    private final AppointmentRepository repository;
    private final AppointmentService appointmentService;
    private final WorkScheduleService workScheduleService;
    private final ProfessionalAppointmentUseCase professionalAppointmentUseCase;
    private final ProfessionalRepository professionalRepository;
    private final SalonProfileService salonProfileService;
    private final BookingPolicyManager bookingPolicyManager;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createAppointment(AppointmentCreateDTO dto, UserPrincipal principal) {
        List<AppointmentAddOn> addOnServices = appointmentService.mapAddOns(dto.addOnsIds());
        SalonService mainService = appointmentService.findService(dto.mainServiceId());
        TimeInterval interval = appointmentService.calculateIntervalAndBuffer(dto, principal, mainService, addOnServices);

        workScheduleService.checkProfessionalAvailability(
                UUID.fromString(dto.professionalExternalId()),
                interval
        );

        professionalAppointmentUseCase.checkIfProfessionalHasTimeConflicts(
                UUID.fromString(dto.professionalExternalId()),
                interval
        );

        bookingPolicyManager.validate(mainService, dto.zonedAppointmentDateTime().toLocalDateTime(), principal.getUserId());

        Appointment appointment = appointmentService.buildAppointment(
                dto,
                principal.getUserId(),
                interval,
                mainService,
                salonProfileService.getSalonProfileByTenantId(TenantContext.getTenant()),
                addOnServices
        );

        Appointment bookedAppointment = repository.save(appointment);
        eventPublisher.publishEvent(bookedAppointment.getId());
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, Long clientId) {
        Appointment appointment = appointmentService.findAndValidateAppointmentOwnership(appointmentId, clientId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        Instant twoDaysFromNow = Instant.now().plus(2, ChronoUnit.DAYS);

        if (appointment.getStartDate().isBefore(twoDaysFromNow)) {
            clientRepository.incrementCanceledAppointments(clientId);
        }
    }

    @Transactional(readOnly = true)
    public ProfessionalAvailabilityDTO findAvailableTimes(String professionalExternalId,
                                                          int serviceDurationInSeconds) {
        ZoneId salonZoneId = salonProfileService.getSalonZoneIdByContext();
        Professional professional = professionalRepository.findByExternalId(UUID.fromString(professionalExternalId));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = YearMonth.from(startDate).atEndOfMonth();

        List<AppointmentTimesDTO> possibleAppointmentDates = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> findProfessionalDailyAvailability(
                        professional,
                        date,
                        salonZoneId,
                        serviceDurationInSeconds))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return new ProfessionalAvailabilityDTO(salonZoneId, possibleAppointmentDates);
    }

    private Optional<AppointmentTimesDTO> findProfessionalDailyAvailability(Professional professional,
                                                                            LocalDate date,
                                                                            ZoneId salonZoneId,
                                                                            Integer serviceDurationInSeconds) {

        Optional<WorkSchedule> scheduleOpt = professional.getWorkSchedules().stream()
                .filter(ws -> ws.getDayOfWeek() == date.getDayOfWeek())
                .findFirst();

        if (scheduleOpt.isEmpty()) {
            return Optional.empty();
        }

        WorkSchedule schedule = scheduleOpt.get();

        List<BusyInterval> busyIntervals = calculateOccupiedIntervals(professional, date, salonZoneId);

        List<LocalTime> availableTimes = new ArrayList<>();

        LocalTime cursor = schedule.getWorkStart();

        for (BusyInterval interval : busyIntervals) {
            if (cursor.isBefore(interval.getStart())) {
                processTimeSlotGaps(cursor, interval.getStart(), serviceDurationInSeconds, availableTimes);
            }

            if (interval.getEnd().isAfter(cursor)) {
                cursor = interval.getEnd();
            }
        }

        if (cursor.isBefore(schedule.getWorkEnd())) {
            processTimeSlotGaps(cursor, schedule.getWorkEnd(), serviceDurationInSeconds, availableTimes);
        }

        return Optional.of(new AppointmentTimesDTO(
                date,
                availableTimes
        ));
    }

    private void processTimeSlotGaps(LocalTime gapStart,
                                     LocalTime gapEnd,
                                     int duration,
                                     List<LocalTime> suggestions) {

        final long LARGE_GAP_THRESHOLD = TimeUnit.HOURS.toSeconds(3);

        long gapSize = ChronoUnit.SECONDS.between(gapStart, gapEnd);
        if (gapSize < duration) return;

        suggestions.add(gapStart);

        LocalTime backLoadSlot = gapEnd.minusSeconds(duration);

        if (!backLoadSlot.equals(gapStart)) {
            suggestions.add(backLoadSlot);
        }

        if (gapSize >= LARGE_GAP_THRESHOLD) {
            LocalTime candidate = gapStart.plusHours(1);

            while (candidate.plusSeconds(duration).isBefore(gapEnd)) {

                if (!candidate.equals(backLoadSlot)) {
                    suggestions.add(candidate);
                }
                candidate = candidate.plusHours(1);
            }
        }
    }

    private List<BusyInterval> calculateOccupiedIntervals(Professional professional,
                                                          LocalDate date,
                                                          ZoneId salonZoneId) {
        Integer salonBufferInMinutes = salonProfileService.getSalonBufferTimeInMinutes(TenantContext.getTenant());

        Stream<BusyInterval> lunchBreak = professional.getWorkSchedules().stream()
                .filter(ws -> ws.getDayOfWeek() == date.getDayOfWeek())
                .map(ws -> SimpleBusyInterval.builder()
                        .start(ws.getLunchBreakStartTime())
                        .end(ws.getLunchBreakEndTime())
                        .build()
                );

        Stream<BusyInterval> appointments = professional.getProfessionalAppointments().stream()
                .filter(ap -> date.equals(LocalDate.ofInstant(ap.getStartDate(), salonZoneId)))
                .map(ap -> SimpleBusyInterval.builder()
                        .start(LocalTime.ofInstant(ap.getStartDate(), salonZoneId))
                        .end(LocalTime.ofInstant(ap.getEndDate(), salonZoneId).plusMinutes(salonBufferInMinutes))
                        .build()
                );

        Stream<BusyInterval> blocks = professional.getScheduleBlocks().stream()
                .filter(sb -> date.equals(LocalDate.ofInstant(sb.getDateAndStartTime(), salonZoneId)))
                .map(sb -> SimpleBusyInterval.builder()
                        .start(LocalTime.ofInstant(sb.getDateAndStartTime(), salonZoneId))
                        .end(LocalTime.ofInstant(sb.getDateAndEndTime(), salonZoneId))
                        .build()
                );

        Stream<BusyInterval> concat = Stream.concat(lunchBreak, appointments);

        return Stream.concat(concat, blocks)
                .sorted(Comparator.comparing(BusyInterval::getStart))
                .toList();
    }
}
