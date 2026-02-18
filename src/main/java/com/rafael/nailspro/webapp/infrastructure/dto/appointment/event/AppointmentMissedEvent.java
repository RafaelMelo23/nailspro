package com.rafael.nailspro.webapp.infrastructure.dto.appointment.event;


public record AppointmentMissedEvent(
        Long appointmentId,
        String tenantId,
        Long clientId
) {
}

