package com.rafael.nailspro.webapp.model.dto.appointment;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Builder
public record AppointmentCreateDTO(String professionalExternalId,
                                   Long mainServiceId,
                                   List<Long> addOnsIds,
                                   ZonedDateTime zonedAppointmentDateTime,
                                   Optional<String> observation) {
}
