package com.rafael.nailspro.webapp.service.appointment;

import com.rafael.nailspro.webapp.model.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.model.dto.appointment.TimeInterval;
import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.entity.AppointmentAddOn;
import com.rafael.nailspro.webapp.model.entity.salon.SalonService;
import com.rafael.nailspro.webapp.model.entity.user.Client;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.model.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.service.admin.client.AdminClientService;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import com.rafael.nailspro.webapp.service.professional.ProfessionalService;
import com.rafael.nailspro.webapp.service.salon.service.SalonProfileService;
import com.rafael.nailspro.webapp.service.salon.service.SalonServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    /*
        whats done:
        user can register an appointment
        user can cancel an appointment
        admin needs to be able to see a specific users appointments
        professionals needs to see the appointments with them
        professionals need the APIs to change appointments statuses

        what needs to be done:

        users need the API to see the available times for the selected professional
        users need to be able to see their future and past appointments
     */

    private final SalonServiceService salonService;
    private final ProfessionalService professionalService;
    private final AdminClientService adminClientService;
    private final AppointmentRepository repository;
    private final SalonProfileService salonProfileService;

    Appointment findAndValidateAppointmentOwnership(Long appointmentId, Long clientId) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (!appointment.getClient().getId().equals(clientId)) {
            throw new BusinessException("Você não pode cancelar esse horário");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        return appointment;
    }

    TimeInterval calculateIntervalAndBuffer(AppointmentCreateDTO dto,
                                            UserPrincipal principal,
                                            SalonService mainService,
                                            List<AppointmentAddOn> addOnServices) {

        int totalDurationInMinutes =
                calculateAppointmentMinutage(mainService, addOnServices);

        Integer salonBufferTime =
                salonProfileService.getSalonBufferTime(principal.getTenantId());

        LocalDateTime start = dto.appointmentDate();
        LocalDateTime realEnd = start.plusMinutes(totalDurationInMinutes);
        LocalDateTime endWithBuffer = realEnd.plusMinutes(salonBufferTime);

        return TimeInterval.builder()
                .realStart(start)
                .realEnd(realEnd)
                .endWithBuffer(endWithBuffer)
                .build();
    }

    Appointment buildAppointment(AppointmentCreateDTO dto,
                                 Long clientId,
                                 TimeInterval interval,
                                 SalonService mainService,
                                 List<AppointmentAddOn> addOnServices) {

        Appointment appointment = Appointment.builder()
                .appointmentStatus(AppointmentStatus.PENDING)
                .startDate(interval.realStart())
                .endDate(interval.realEnd())
                .client(findClient(clientId))
                .professional(findProfessional(dto.professionalExternalId()))
                .mainSalonService(mainService)
                .addOns(addOnServices)
                .observations(dto.observation().get())
                .build();

        appointment.setTotalValue(appointment.calculateTotalValue());
        appointment.setEndDate(interval.realEnd());

        return appointment;
    }

    private static int calculateAppointmentMinutage(SalonService mainService,
                                                    List<AppointmentAddOn> addOnServices) {

        return mainService.getDurationMinutes()
                + addOnServices.stream()
                .mapToInt(addon -> addon.getService().getDurationMinutes())
                .sum();
    }

    public Client findClient(Long clientId) {
        return adminClientService.getClient(clientId);
    }

    public Professional findProfessional(String professionalExternalId) {
        return professionalService.findByExternalId(professionalExternalId);
    }

    public SalonService findService(Long serviceId) {
        return salonService.findById(serviceId);
    }

    public List<AppointmentAddOn> mapAddOns(List<Long> addOnIds) {
        return salonService.findAddOns(addOnIds);
    }
}
