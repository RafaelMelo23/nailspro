package com.rafael.nailspro.webapp.model.dto.appointment;

import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
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
        Integer mainServiceDurationMinutes,
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

