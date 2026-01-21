package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.domain.professional.ProfessionalRepository;
import com.rafael.nailspro.webapp.domain.user.Professional;
import com.rafael.nailspro.webapp.infrastructure.dto.professional.ProfessionalSimplifiedDTO;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
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

    public Professional findByTenantId(String tenantId) {

        return repository.findSalonOwnerByTenantId(tenantId)
                .orElseThrow(() -> new BusinessException("Unable to find Owner"));
    }
}
