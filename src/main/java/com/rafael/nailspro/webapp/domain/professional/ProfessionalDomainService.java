package com.rafael.nailspro.webapp.domain.professional;

import com.rafael.nailspro.webapp.domain.appointment.TimeInterval;
import com.rafael.nailspro.webapp.domain.user.Professional;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalDomainService {

    private final ProfessionalRepository repository;

    public void checkIfProfessionalHasTimeConflicts(UUID professionalId, TimeInterval interval) {
        if (repository.hasTimeConflicts(
                professionalId,
                interval.toLocalDateTime(interval.realTimeStart()),
                interval.toLocalDateTime(interval.endTimeWithBuffer()),
                interval.getDayOfWeek())) {
            throw new BusinessException("O profissional já possui um compromisso ou bloqueio neste horário.");
        }
    }

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

    public Set<Professional> findAll() {

        return new HashSet<>(repository.findAll());
    }
}
