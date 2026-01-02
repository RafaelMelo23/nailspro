package com.rafael.nailspro.webapp.application.service;

import com.rafael.nailspro.webapp.domain.TenantContext;
import com.rafael.nailspro.webapp.domain.enums.OperationalStatus;
import com.rafael.nailspro.webapp.domain.profile.SalonProfileRepository;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

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

    public Integer getSalonBufferTimeInMinutes(String tenantId) {

        return repository.findSalonProfileAppointmentBufferMinutesByTenantId(tenantId)
                .orElseThrow(() -> new BusinessException("Salão não encontrado."));
    }

    public ZoneId getSalonZoneId() {

        return repository.getZoneIdByTenantId(TenantContext.getTenant());
    }
}
