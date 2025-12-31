package com.rafael.nailspro.webapp.service.admin.salon.profile;

import com.rafael.nailspro.webapp.model.dto.appointment.AddOnDTO;
import com.rafael.nailspro.webapp.model.dto.appointment.AdminUserAppointmentDTO;
import com.rafael.nailspro.webapp.model.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAppointmentService {

    private final AppointmentRepository repository;

    @Transactional(readOnly = true)
    public Page<AdminUserAppointmentDTO> listUsersAppointments(Long userId, Pageable pageable) {

        return repository.findByClient_Id(userId, pageable)
                .map(ap -> AdminUserAppointmentDTO.builder()
                        .appointmentId(ap.getId())
                        .clientId(ap.getClient().getId())
                        .clientName(ap.getClient().getFullName())
                        .clientPhoneNumber(ap.getClient().getPhoneNumber())
                        .clientMissedAppointments(ap.getClient().getMissedAppointments())
                        .clientCanceledAppointments(ap.getClient().getCanceledAppointments())
                        .professionalId(ap.getProfessional().getId())
                        .professionalName(ap.getProfessional().getFullName())
                        .mainServiceId(ap.getMainSalonService().getId())
                        .mainServiceName(ap.getMainSalonService().getName())
                        .mainServiceDurationMinutes(ap.getMainSalonService().getDurationMinutes())
                        .mainServiceValue(ap.getMainSalonService().getValue())
                        .addOns(
                                ap.getAddOns().stream()
                                        .map(addOn -> new AddOnDTO(
                                                addOn.getService().getId(),
                                                addOn.getService().getName(),
                                                addOn.getQuantity(),
                                                addOn.getUnitPriceSnapshot()
                                        ))
                                        .toList()
                        )
                        .status(ap.getAppointmentStatus())
                        .totalValue(ap.calculateTotalValue())
                        .observations(ap.getObservations())
                        .startDate(ap.getStartDate())
                        .endDate(ap.getEndDate())
                        .build()
                );
    }
}
