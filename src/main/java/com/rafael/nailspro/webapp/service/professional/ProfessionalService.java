package com.rafael.nailspro.webapp.service.professional;

import com.rafael.nailspro.webapp.model.dto.professional.ProfessionalSimplifiedDTO;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public Set<Professional> findAllById(List<Long> ids) {

        return new HashSet<>(professionalRepository.findAllById(ids));
    }

    public Professional findById(Long id) {
        return professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found"));
    }

    public List<ProfessionalSimplifiedDTO> findAllSimplified() {

        return professionalRepository.findAll().stream()
                .map(p -> ProfessionalSimplifiedDTO.builder()
                        .externalId(p.getExternalId())
                        .name(p.getFullName())
                        .professionalPicture(p.getProfessionalPicture())
                        .build())
                .toList();
    }

    public Set<Professional> findAll() {

        return new HashSet<>(professionalRepository.findAll());
    }
}
