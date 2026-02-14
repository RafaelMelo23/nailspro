package com.rafael.nailspro.webapp.infrastructure.dto.appointment;

import lombok.Builder;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record ProfessionalAvailabilityDTO(ZoneId zoneId,
                                          List<AppointmentTimesDTO> appointmentTimesDTOList,
                                          ZonedDateTime earliestRecommendedDate
                                          ) {
}
