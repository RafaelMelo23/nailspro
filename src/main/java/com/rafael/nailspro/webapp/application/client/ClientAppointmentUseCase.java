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
import com.rafael.nailspro.webapp.domain.repository.SalonServiceRepository;
import com.rafael.nailspro.webapp.domain.service.AvailabilityDomainService;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentTimesDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.ProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.booking.AppointmentBookedEvent;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.booking.AppointmentTimeWindow;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.TimeInterval;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.FindProfessionalAvailabilityDTO;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientAppointmentUseCase {

    private final ClientRepository clientRepository;
    private final AppointmentRepository repository;
    private final AppointmentService appointmentService;
    private final WorkScheduleService workScheduleService;
    private final ProfessionalAppointmentUseCase professionalAppointmentUseCase;
    private final SalonProfileService salonProfileService;
    private final BookingPolicyManager bookingPolicyManager;
    private final ApplicationEventPublisher eventPublisher;
    private final ProfessionalRepository professionalRepository;
    private final AvailabilityDomainService availabilityDomainService;
    private final SalonServiceRepository salonServiceRepository;

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
        eventPublisher.publishEvent(new AppointmentBookedEvent(bookedAppointment.getId()));
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
    public ProfessionalAvailabilityDTO findAvailableTimes(FindProfessionalAvailabilityDTO dto,
                                                          Long clientId) {

        SalonProfile salonProfile = salonProfileService.getSalonProfileByTenantId(TenantContext.getTenant());
        Professional professional = professionalRepository.findByExternalId(UUID.fromString(dto.professionalExternalId()));
        List<SalonService> services = salonServiceRepository.findAllById(dto.servicesIds());

        AppointmentTimeWindow appointmentTimeWindow =
                bookingPolicyManager.calculateAllowedWindows(services, clientId);

        List<AppointmentTimesDTO> possibleAppointmentDates = appointmentTimeWindow.start().datesUntil(appointmentTimeWindow.end())
                .map(date -> availabilityDomainService.findProfessionalDailyAvailability(
                        professional,
                        date,
                        salonProfile,
                        dto.serviceDurationInSeconds()))
                .flatMap(Optional::stream)
                .toList();

        return new ProfessionalAvailabilityDTO(salonProfile.getZoneId(), possibleAppointmentDates);
    }
}