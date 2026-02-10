package com.rafael.nailspro.webapp.infrastructure.dto.professional;

import java.util.List;

public record FindProfessionalAvailabilityDTO(String professionalExternalId,
                                              int serviceDurationInSeconds,
                                              List<Long> servicesIds) {
}
