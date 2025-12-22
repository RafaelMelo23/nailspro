package com.rafael.nailspro.webapp.model.dto.salon.service;

import com.rafael.nailspro.webapp.model.dto.professional.ProfessionalSimplifiedDTO;
import lombok.Builder;

import java.util.Set;

@Builder
public record SalonServiceOutDTO(Long id,
                                 String name,
                                 Integer value,
                                 Integer durationMinutes,
                                 String description,
                                 Set<ProfessionalSimplifiedDTO> professionals,
                                 Boolean isActive) {
}
