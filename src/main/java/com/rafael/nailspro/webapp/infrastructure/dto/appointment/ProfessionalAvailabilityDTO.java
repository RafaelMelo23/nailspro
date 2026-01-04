package com.rafael.nailspro.webapp.infrastructure.dto.appointment;

import java.time.ZoneId;
import java.util.List;

public record ProfessionalAvailabilityDTO(ZoneId zoneId,
                                          List<AppointmentTimesDTO> appointmentTimesDTOList) {
}
