package com.rafael.nailspro.webapp.model.dto.appointment;

import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ProfessionalAppointmentScheduleDTO(Long appointmentId,

                                                 Long clientId,
                                                 String clientName,
                                                 String clientPhoneNumber,
                                                 Integer clientMissedAppointments,
                                                 Integer clientCanceledAppointments,

                                                 AppointmentStatus status,
                                                 Integer totalValue,
                                                 String observations,

                                                 ZonedDateTime startDate,
                                                 ZonedDateTime endDate) {
}
