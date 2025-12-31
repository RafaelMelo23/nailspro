package com.rafael.nailspro.webapp.model.dto.admin.client;

import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record ClientAppointmentDTO(
        Long appointmentId,
        Long professionalId,
        String professionalName,

        ZonedDateTime startDateAndTime,
        ZonedDateTime endDateAndTime,
        AppointmentStatus status,

        String mainServiceName,
        List<String> addOnServiceNames,
        Integer totalValue,

        String observations
) {
}