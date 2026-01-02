package com.rafael.nailspro.webapp.infrastructure.dto.appointment;

import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record ProfessionalAvailabilityDTO(LocalDate date,
                                          DayOfWeek day,
                                          List<ZonedDateTime> availableTimes) {
}
