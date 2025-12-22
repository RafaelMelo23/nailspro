package com.rafael.nailspro.webapp.service.infra.mapper;

import com.rafael.nailspro.webapp.model.dto.professional.ProfessionalSimplifiedDTO;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProfessionalMapper {

    public Set<ProfessionalSimplifiedDTO> mapProfessionalsToSimplifiedDTO(Set<Professional> professionals) {
        return professionals.stream()
                .map(p -> ProfessionalSimplifiedDTO.builder()
                        .externalId(p.getExternalId())
                        .name(p.getFullName())
                        .professionalPicture(p.getProfessionalPicture())
                        .build())
                .collect(Collectors.toSet());
    }

}
