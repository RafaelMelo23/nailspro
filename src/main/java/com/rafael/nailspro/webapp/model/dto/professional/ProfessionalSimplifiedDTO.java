package com.rafael.nailspro.webapp.model.dto.professional;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProfessionalSimplifiedDTO(UUID externalId,
                                        String name,
                                        String professionalPicture) {
}
