package com.rafael.nailspro.webapp.domain.profile;

import com.rafael.nailspro.webapp.domain.enums.OperationalStatus;
import com.rafael.nailspro.webapp.domain.user.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SalonProfileRepository extends JpaRepository<SalonProfile, Long> {

    Optional<SalonProfile> findByOwner_Id(Long id);

    @Query("SELECT s.appointmentBufferMinutes FROM SalonProfile s WHERE s.tenantId = :tenantId")
    Optional<Integer> findSalonProfileAppointmentBufferMinutesByTenantId(@Param("tenantId") String tenantId);

    boolean existsSalonProfileByTenantIdAndOperationalStatus(String tenantId, OperationalStatus operationalStatus);

    @Query("SELECT s.warningMessage FROM SalonProfile s WHERE s.tenantId = :tenantId")
    Optional<String> findWarningMessageByTenantId(@Param("tenantId") String tenantId);

    @Query("SELECT s.zoneId FROM SalonProfile s WHERE s.tenantId = :tenantId")
    Optional<String> fetchZoneIdByTenantId(@Param("tenantId") String tenantId);


    SalonProfile findByTenantId(String tenantId);
}
