package com.rafael.nailspro.webapp.service.professional;

import com.rafael.nailspro.webapp.model.dto.appointment.TimeInterval;
import com.rafael.nailspro.webapp.model.dto.professional.ProfessionalSimplifiedDTO;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.repository.ProfessionalRepository;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository repository;

    public Set<Professional> findAllById(List<Long> ids) {

        return new HashSet<>(repository.findAllById(ids));
    }

    public Professional findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found"));
    }

    public Professional findByExternalId(String externalId) {

        return repository.findByExternalId(UUID.fromString(externalId));
    }

    public List<ProfessionalSimplifiedDTO> findAllSimplified() {

        return repository.findAll().stream()
                .map(p -> ProfessionalSimplifiedDTO.builder()
                        .externalId(p.getExternalId())
                        .name(p.getFullName())
                        .professionalPicture(p.getProfessionalPicture())
                        .build())
                .toList();
    }

    public Set<Professional> findAll() {

        return new HashSet<>(repository.findAll());
    }

    public void checkIfProfessionalHasTimeConflicts(UUID professionalId, TimeInterval interval) {
        if (repository.hasTimeConflicts(
                professionalId,
                interval.start(),
                interval.end(),
                interval.getDayOfWeek())) {
            throw new BusinessException("O profissional já possui um compromisso ou bloqueio neste horário.");
        }
    }
}
