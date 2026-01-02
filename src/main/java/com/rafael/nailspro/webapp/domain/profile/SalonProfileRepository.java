package com.rafael.nailspro.webapp.domain.profile;

import com.rafael.nailspro.webapp.domain.enums.OperationalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZoneId;
import java.util.Optional;

public interface SalonProfileRepository extends JpaRepository<SalonProfile, Long> {

    Optional<SalonProfile> findByOwner_Id(Long id);

    Optional<Integer> findSalonProfileAppointmentBufferMinutesByTenantId(String tenantId);

    boolean existsSalonProfileByTenantIdAndOperationalStatus(String tenantId, OperationalStatus operationalStatus);

    Optional<String> findWarningMessageByTenantId(String tenantId);

    ZoneId getZoneIdByTenantId(String tenantId);
}
