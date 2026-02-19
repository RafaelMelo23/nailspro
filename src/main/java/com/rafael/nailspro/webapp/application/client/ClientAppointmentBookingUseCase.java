package com.rafael.nailspro.webapp.application.client;

import com.rafael.nailspro.webapp.application.appointment.AppointmentService;
import com.rafael.nailspro.webapp.application.appointment.BookingPolicyManager;
import com.rafael.nailspro.webapp.application.professional.ProfessionalAppointmentUseCase;
import com.rafael.nailspro.webapp.application.professional.WorkScheduleService;
import com.rafael.nailspro.webapp.application.salon.service.SalonProfileService;
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
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import com.rafael.nailspro.webapp.infrastructure.exception.ProfessionalBusyException;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClientAppointmentBookingUseCase {

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
        log.info("Request to create appointment: Client={}, Professional={}, Start={}",
                principal.getUserId(), dto.professionalExternalId(), dto.zonedAppointmentDateTime());

        try {
            List<AppointmentAddOn> addOnServices = appointmentService.mapAddOns(dto.addOnsIds());
            SalonService mainService = appointmentService.findService(dto.mainServiceId());
            TimeInterval interval = appointmentService.calculateIntervalAndBuffer(dto, principal, mainService, addOnServices);

            Professional professional = appointmentService.findProfessionalAndLock(dto.professionalExternalId());

            workScheduleService.checkProfessionalAvailability(UUID.fromString(dto.professionalExternalId()), interval);
            professionalAppointmentUseCase.checkIfProfessionalHasTimeConflicts(UUID.fromString(dto.professionalExternalId()), interval);
            bookingPolicyManager.validate(dto.zonedAppointmentDateTime().toLocalDateTime(), principal.getUserId());

            Appointment appointment = appointmentService.buildAppointment(
                    dto, principal.getUserId(), interval, mainService,
                    salonProfileService.getSalonProfileByTenantId(TenantContext.getTenant()),
                    professional, addOnServices
            );

            Appointment bookedAppointment = repository.save(appointment);

            log.info("Appointment created successfully: ID={}, Client={}, Professional={}",
                    bookedAppointment.getId(), principal.getUserId(), professional.getId());

            eventPublisher.publishEvent(new AppointmentBookedEvent(bookedAppointment.getId()));

        } catch (PessimisticLockingFailureException e) {
            log.warn("Booking conflict: Professional {} is currently locked. Client {} request timed out.",
                    dto.professionalExternalId(), principal.getUserId());
            throw new ProfessionalBusyException("O calendário do profissional está sendo atualizado. Por favor tente novamente em instantes.");
        } catch (Exception e) {
            log.error("Failed to create appointment for Client={}. Reason: {}", principal.getUserId(), e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, Long clientId) {
        log.info("Attempting to cancel appointment: ID={}, Client={}", appointmentId, clientId);

        Appointment appointment = appointmentService.findAndValidateAppointmentOwnership(appointmentId, clientId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        Instant twoDaysFromNow = Instant.now().plus(2, ChronoUnit.DAYS);

        if (appointment.getStartDate().isBefore(twoDaysFromNow)) {
            log.info("Late cancellation detected for Client={}. Incrementing penalty counter.", clientId);
            clientRepository.incrementCanceledAppointments(clientId);
        }

        log.info("Appointment {} successfully cancelled.", appointmentId);
    }

    @Transactional(readOnly = true)
    public ProfessionalAvailabilityDTO findAvailableTimes(FindProfessionalAvailabilityDTO dto, Long clientId) {
        log.debug("Searching availability: Professional={}, Client={}", dto.professionalExternalId(), clientId);

        SalonProfile salonProfile = salonProfileService.getSalonProfileByTenantId(TenantContext.getTenant());

        Professional professional = professionalRepository.findByExternalId(UUID.fromString(dto.professionalExternalId()))
                .orElseThrow(() -> new BusinessException("Professional not found"));

        List<SalonService> services = salonServiceRepository.findAllById(dto.servicesIds());

        AppointmentTimeWindow appointmentTimeWindow = bookingPolicyManager.calculateAllowedWindows(services, clientId);

        List<AppointmentTimesDTO> availableTimes = availabilityDomainService.findAvailableTimes(
                professional, appointmentTimeWindow, salonProfile, dto.serviceDurationInSeconds());

        log.debug("Found {} available slots for Professional={}", availableTimes.size(), dto.professionalExternalId());

        return ProfessionalAvailabilityDTO.builder()
                .appointmentTimesDTOList(availableTimes)
                .zoneId(salonProfile.getZoneId())
                .earliestRecommendedDate(bookingPolicyManager.calculateEarliestRecommendedDate(clientId).atZone(salonProfile.getZoneId()))
                .build();
    }
}