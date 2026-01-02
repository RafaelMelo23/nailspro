package com.rafael.nailspro.webapp.application.client;

import com.rafael.nailspro.webapp.application.professional.WorkScheduleService;
import com.rafael.nailspro.webapp.application.service.SalonProfileService;
import com.rafael.nailspro.webapp.domain.TenantContext;
import com.rafael.nailspro.webapp.domain.appointment.*;
import com.rafael.nailspro.webapp.domain.client.ClientDomainService;
import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.professional.ProfessionalDomainService;
import com.rafael.nailspro.webapp.domain.professional.WorkSchedule;
import com.rafael.nailspro.webapp.domain.service.SalonService;
import com.rafael.nailspro.webapp.domain.user.Professional;
import com.rafael.nailspro.webapp.domain.user.UserPrincipal;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.SimpleBusyInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.contract.BusyInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClientAppointmentUseCase {

    private final ClientDomainService clientDomainService;
    private final AppointmentRepository repository;
    private final AppointmentDomainService appointmentDomainService;
    private final WorkScheduleService workScheduleService;
    private final ProfessionalDomainService professionalDomainService;
    private final SalonProfileService salonProfileService;

    @Transactional
    public void createAppointment(AppointmentCreateDTO dto, UserPrincipal principal) {
        List<AppointmentAddOn> addOnServices = appointmentDomainService.mapAddOns(dto.addOnsIds());
        SalonService mainService = appointmentDomainService.findService(dto.mainServiceId());

        TimeInterval interval = appointmentDomainService.calculateIntervalAndBuffer(dto, principal, mainService, addOnServices);

        workScheduleService.checkProfessionalAvailability(
                UUID.fromString(dto.professionalExternalId()),
                interval
        );

        professionalDomainService.checkIfProfessionalHasTimeConflicts(
                UUID.fromString(dto.professionalExternalId()),
                interval
        );

        Appointment appointment = appointmentDomainService.buildAppointment(
                dto,
                principal.getUserId(),
                interval,
                mainService,
                addOnServices
        );

        repository.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, Long clientId) {
        Appointment appointment = appointmentDomainService.findAndValidateAppointmentOwnership(appointmentId, clientId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        clientDomainService.incrementClientCancelledAppointments(clientId);
    }


    @Transactional(readOnly = true)
    public List<ProfessionalAvailabilityDTO> findAvailableTimes(Long professionalId,
                                                                long serviceDurationInSeconds) {
        ZoneId salonZoneId = salonProfileService.getSalonZoneId();
        Professional professional = professionalDomainService.findById(professionalId);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = YearMonth.from(startDate).atEndOfMonth();

        return startDate.datesUntil(endDate.plusDays(1))
                .map(date -> findProfessionalDailyAvailability(
                        professional,
                        date,
                        salonZoneId,
                        serviceDurationInSeconds))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ProfessionalAvailabilityDTO> findProfessionalDailyAvailability(Professional professional,
                                                                                    LocalDate date,
                                                                                    ZoneId salonZoneId,
                                                                                    Long serviceDurationInSeconds) {

        Optional<WorkSchedule> scheduleOpt = professional.getWorkSchedules().stream()
                .filter(ws -> ws.getDayOfWeek() == date.getDayOfWeek())
                .findFirst();

        if (scheduleOpt.isEmpty()) {
            return Optional.empty();
        }

        WorkSchedule schedule = scheduleOpt.get();
        LocalTime workStart = schedule.getWorkStart();
        LocalTime workEnd = schedule.getWorkEnd();

        List<BusyInterval> busyIntervals = calculateOccupiedIntervals(professional, date, salonZoneId);

        List<ZonedDateTime> availableTimes = new ArrayList<>();
        for (BusyInterval busyInterval : busyIntervals) {

            while (workStart.plusSeconds(serviceDurationInSeconds).isBefore(busyInterval.getEnd()) ||
                    workStart.plusSeconds(serviceDurationInSeconds).equals(busyInterval.getEnd())) {

                addAvailableTime(date, salonZoneId, workStart, availableTimes);

                workStart = workStart.plusSeconds(serviceDurationInSeconds);
            }
        }

        while (workStart.plusSeconds(serviceDurationInSeconds).isBefore(workEnd) ||
                workStart.plusSeconds(serviceDurationInSeconds).equals(workEnd)) {

            addAvailableTime(date, salonZoneId, workStart, availableTimes);
            workStart = workStart.plusSeconds(serviceDurationInSeconds);
        }

        return Optional.of(new ProfessionalAvailabilityDTO(
                date,
                scheduleOpt.get().getDayOfWeek(),
                availableTimes
        ));
    }

    private static void addAvailableTime(LocalDate date,
                                         ZoneId salonZoneId,
                                         LocalTime workStart,
                                         List<ZonedDateTime> availableTimes) {


        LocalDateTime availableDate = LocalDateTime.of(date, workStart);
        availableTimes.add(ZonedDateTime.of(availableDate, salonZoneId));
    }

    private List<BusyInterval> calculateOccupiedIntervals(Professional professional,
                                                          LocalDate date,
                                                          ZoneId salonZoneId) {
        Integer salonBufferInMinutes = salonProfileService.getSalonBufferTimeInMinutes(TenantContext.getTenant());

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

        return Stream.concat(appointments, blocks)
                .sorted(Comparator.comparing(BusyInterval::getStart))
                .toList();
    }
}
