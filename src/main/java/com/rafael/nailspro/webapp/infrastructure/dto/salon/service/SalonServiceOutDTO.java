package com.rafael.nailspro.webapp.infrastructure.dto.salon.service;

import com.rafael.nailspro.webapp.infrastructure.dto.professional.ProfessionalSimplifiedDTO;
import lombok.Builder;

import java.util.Set;

@Builder
public record SalonServiceOutDTO(Long id,
                                 String name,
                                 Integer value,
                                 Long durationInSeconds,
                                 String description,
                                 Set<ProfessionalSimplifiedDTO> professionals,
                                 Boolean isActive) {
}
