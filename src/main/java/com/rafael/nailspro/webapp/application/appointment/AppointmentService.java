package com.rafael.nailspro.webapp.application.appointment;

import com.rafael.nailspro.webapp.application.admin.client.ClientManagementService;
import com.rafael.nailspro.webapp.application.salon.service.SalonProfileService;
import com.rafael.nailspro.webapp.application.salon.service.SalonServiceService;
import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.model.*;
import com.rafael.nailspro.webapp.domain.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.domain.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.date.TimeInterval;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final SalonServiceService salonService;
    private final ProfessionalRepository professionalRepository;
    private final ClientManagementService clientManagementService;
    private final AppointmentRepository repository;
    private final SalonProfileService salonProfileService;

    public Appointment findAndValidateAppointmentOwnership(Long appointmentId, Long clientId) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!appointment.getClient().getId().equals(clientId)) {
            throw new BusinessException("Você não pode cancelar esse horário");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        return appointment;
    }

    public TimeInterval calculateIntervalAndBuffer(AppointmentCreateDTO dto,
                                                   UserPrincipal principal,
                                                   SalonService mainService,
                                                   List<AppointmentAddOn> addOnServices) {

        long totalDurationInSeconds =
                calculateAppointmentDurationInSeconds(mainService, addOnServices);

        Integer salonBufferTimeInMinutes =
                salonProfileService.getSalonBufferTimeInMinutes(principal.getTenantId());

        Instant start = dto.zonedAppointmentDateTime().toInstant();
        Instant realEnd = start.plus(totalDurationInSeconds, ChronoUnit.SECONDS);
        Instant endWithBuffer = realEnd.plus(salonBufferTimeInMinutes, ChronoUnit.MINUTES);

        return TimeInterval.builder()
                .realTimeStart(start)
                .realTimeEnd(realEnd)
                .endTimeWithBuffer(endWithBuffer)
                .build();
    }

    public Appointment buildAppointment(AppointmentCreateDTO dto,
                                        Long clientId,
                                        TimeInterval interval,
                                        SalonService mainService,
                                        SalonProfile salonProfile,
                                        Professional professional,
                                        List<AppointmentAddOn> addOnServices) {

        Appointment appointment = Appointment.builder()
                .appointmentStatus(AppointmentStatus.PENDING)
                .startDate(interval.realTimeStart())
                .endDate(interval.realTimeEnd())
                .client(findClient(clientId))
                .professional(professional)
                .mainSalonService(mainService)
                .addOns(addOnServices)
                .observations(dto.observation().get())
                .salonTradeName(salonProfile.getTradeName())
                .salonZoneId(salonProfile.getZoneId())
                .build();

        appointment.setTotalValue(appointment.calculateTotalValue());
        appointment.setEndDate(interval.realTimeEnd());

        return appointment;
    }

    private static long calculateAppointmentDurationInSeconds(SalonService mainService,
                                                             List<AppointmentAddOn> addOnServices) {

        return mainService.getDurationInSeconds()
                + addOnServices.stream()
                .mapToLong(addon -> addon.getService().getDurationInSeconds())
                .sum();
    }

    public Appointment findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Não foi possível encontrar o agendamento especificado"));
    }

    public Client findClient(Long clientId) {
        return clientManagementService.getClient(clientId);
    }

    public Professional findProfessionalAndLock(String professionalExternalId) {
        return professionalRepository.findByExternalIdWithPessimisticLock(UUID.fromString(professionalExternalId));
    }

    public SalonService findService(Long serviceId) {
        return salonService.findById(serviceId);
    }

    public List<AppointmentAddOn> mapAddOns(List<Long> addOnIds) {
        return salonService.findAddOns(addOnIds);
    }
}