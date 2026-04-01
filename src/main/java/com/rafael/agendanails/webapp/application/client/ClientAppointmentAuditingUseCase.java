package com.rafael.agendanails.webapp.application.client;

import com.rafael.agendanails.webapp.domain.repository.AppointmentRepository;
import com.rafael.agendanails.webapp.infrastructure.dto.appointment.AddOnDTO;
import com.rafael.agendanails.webapp.infrastructure.dto.appointment.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ClientAppointmentAuditingUseCase {

    private final AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getClientsAppointments(Long clientId, Pageable page) {

        return appointmentRepository.findByClientId(clientId, page)
                .map(ap -> new AppointmentDTO(
                        ap.getId(),
                        ap.getProfessional().getFullName(),
                        ap.getMainSalonService().getName(),
                        ap.getTotalValue(),
                        ap.getObservations(),
                        ap.getAppointmentStatus().name(),
                        ZonedDateTime.ofInstant(ap.getStartDate(), ap.getSalonZoneId()),
                        ZonedDateTime.ofInstant(ap.getEndDate(), ap.getSalonZoneId()),
                        ap.getSalonTradeName(),
                        ap.getSalonZoneId().toString(),
                        ap.getAddOns().stream()
                                .map(addOn -> AddOnDTO.builder()
                                        .serviceName(addOn.getService().getName())
                                        .quantity(addOn.getQuantity())
                                        .unitPriceSnapshot(addOn.getUnitPriceSnapshot())
                                        .build()
                                )
                        .toList()
                ));
    }
}
