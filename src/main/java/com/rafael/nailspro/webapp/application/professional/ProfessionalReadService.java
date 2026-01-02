package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.domain.professional.ProfessionalRepository;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.ProfessionalSimplifiedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessionalReadService {

    private final ProfessionalRepository repository;

    public List<ProfessionalSimplifiedDTO> findAllSimplified() {

        return repository.findAll().stream()
                .map(p -> ProfessionalSimplifiedDTO.builder()
                        .externalId(p.getExternalId())
                        .name(p.getFullName())
                        .professionalPicture(p.getProfessionalPicture())
                        .build())
                .toList();
    }
}
