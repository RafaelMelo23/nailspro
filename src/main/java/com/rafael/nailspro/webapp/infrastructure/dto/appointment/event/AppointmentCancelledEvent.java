package com.rafael.nailspro.webapp.infrastructure.dto.appointment.event;

public record AppointmentCancelledEvent(
        Long appointmentId,
        String tenantId,
        Long clientId
) {
}

