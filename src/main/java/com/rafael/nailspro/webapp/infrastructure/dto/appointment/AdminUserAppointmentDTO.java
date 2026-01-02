package com.rafael.nailspro.webapp.infrastructure.dto.appointment;

import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record AdminUserAppointmentDTO(

        Long appointmentId,

        // Client info
        Long clientId,
        String clientName,
        String clientPhoneNumber,
        Integer clientMissedAppointments,
        Integer clientCanceledAppointments,

        // Professional info
        Long professionalId,
        String professionalName,

        // Main service
        Long mainServiceId,
        String mainServiceName,
        Long mainServiceDurationInSeconds,
        Integer mainServiceValue,

        // Add-ons
        List<AddOnDTO> addOns,

        // Appointment data
        AppointmentStatus status,
        Integer totalValue,
        String observations,

        ZonedDateTime startDateAndTime,
        ZonedDateTime endDateAndTime
) {
}

