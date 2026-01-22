package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.model.Professional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    @Modifying
    @Query("UPDATE Professional p set p.isActive = false WHERE p.id = :id")
    void deactivateProfessional(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
            SELECT (COUNT(p) > 0)
            FROM Professional p
            WHERE p.externalId = :externalId
            AND (
            EXISTS (
            SELECT 1 FROM Appointment ap
            WHERE ap.professional = p
            AND ap.appointmentStatus <> com.rafael.nailspro.webapp.domain.enums.AppointmentStatus.CANCELLED
            AND ap.endDate > :startDate
            AND ap.startDate < :endDate
            )
             OR (
            EXISTS (
            SELECT 1 From ScheduleBlock sb
            WHERE sb.professional = p
            AND sb.dateAndStartTime < :endDate
            AND sb.dateAndEndTime > :startDate
                )
            )
                        )
            """)
    boolean hasTimeConflicts(@Param("externalId") UUID professionalId,
                             @Param("startDateAndTime") LocalDateTime startDate,
                             @Param("endDateAndTime") LocalDateTime endDate,
                             @Param("day") DayOfWeek day);

    Professional findByExternalId(UUID externalId);

    @Query("SELECT sp.owner from SalonProfile sp WHERE sp.tenantId = :id")
    Optional<Professional> findSalonOwnerByTenantId(@Param("id") String tenantId);

}
