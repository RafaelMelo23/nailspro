package com.rafael.nailspro.webapp.model.dto.professional;

import lombok.Builder;

@Builder
public record ProfessionalSimplifiedDTO(Long id,
                                        String name,
                                        String professionalPicture) {
}
