package com.rafael.nailspro.webapp.service.salon.service;

import com.rafael.nailspro.webapp.model.enums.OperationalStatus;
import com.rafael.nailspro.webapp.model.repository.SalonProfileRepository;
import com.rafael.nailspro.webapp.service.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalonProfileService {

    private final SalonProfileRepository repository;

    public boolean isSalonOpenByTenantId(String tenantId) {

        return repository.existsSalonProfileByTenantIdAndOperationalStatus(tenantId, OperationalStatus.OPEN);
    }

    public String getSalonOperationalMessageByTenantId(String tenantId) {

        return repository.findWarningMessageByTenantId(tenantId)
                .orElse(null);
    }

    public Integer getSalonBufferTime(String tenantId) {

        return repository.findSalonProfileAppointmentBufferMinutesByTenantId(tenantId)
                .orElseThrow(() -> new BusinessException("Salão não encontrado."));
    }
}
