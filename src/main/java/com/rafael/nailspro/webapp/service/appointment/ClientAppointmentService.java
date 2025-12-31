package com.rafael.nailspro.webapp.service.appointment;

import com.rafael.nailspro.webapp.model.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.model.entity.appointment.TimeInterval;
import com.rafael.nailspro.webapp.model.entity.appointment.Appointment;
import com.rafael.nailspro.webapp.model.entity.appointment.AppointmentAddOn;
import com.rafael.nailspro.webapp.model.entity.salon.service.SalonService;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.model.repository.AppointmentRepository;
import com.rafael.nailspro.webapp.service.client.ClientService;
import com.rafael.nailspro.webapp.service.professional.ProfessionalService;
import com.rafael.nailspro.webapp.service.professional.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientAppointmentService {

    private final ClientService clientService;
    private final AppointmentRepository repository;
    private final AppointmentService appointmentService;
    private final WorkScheduleService workScheduleService;
    private final ProfessionalService professionalService;

    @Transactional
    public void createAppointment(AppointmentCreateDTO dto, UserPrincipal principal) {
        List<AppointmentAddOn> addOnServices = appointmentService.mapAddOns(dto.addOnsIds());
        SalonService mainService = appointmentService.findService(dto.mainServiceId());

        TimeInterval interval = appointmentService.calculateIntervalAndBuffer(dto, principal, mainService, addOnServices);

        workScheduleService.checkProfessionalAvailability(
                UUID.fromString(dto.professionalExternalId()),
                interval
        );

        professionalService.checkIfProfessionalHasTimeConflicts(
                UUID.fromString(dto.professionalExternalId()),
                interval
        );

        Appointment appointment = appointmentService.buildAppointment(
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
        Appointment appointment = appointmentService.findAndValidateAppointmentOwnership(appointmentId, clientId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        clientService.incrementClientCancelledAppointments(clientId);
    }
}
