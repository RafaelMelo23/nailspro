package com.rafael.nailspro.webapp.model.dto.admin.client;

import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClientAppointmentDTO(
        Long appointmentId,
        Long professionalId,
        String professionalName,

        LocalDateTime appointmentDate,
        AppointmentStatus status,

        String mainServiceName,
        List<String> addOnServiceNames,
        Integer totalValue,

        String observations
) {
}