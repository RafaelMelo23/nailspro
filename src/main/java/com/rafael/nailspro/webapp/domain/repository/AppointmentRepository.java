package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> getClientAppointmentsById(Long id, Pageable pageable);

    Page<Appointment> findByClient_Id(Long userId, Pageable pageable);

    List<Appointment> findByProfessional_IdAndStartDateBetween(Long professionalId,
                                                               Instant startDateAfter,
                                                               Instant startDateBefore);

    List<Appointment> findByStartDateBetween(Instant start, Instant end);

    Optional<Appointment> findFirstByClientIdAndProfessional_ExternalIdOrderByStartDateDesc(Long clientId, UUID professionalId);

    Optional<Appointment> findFirstByClientIdOrderByStartDateDesc(Long clientId);

    double countByClientIdAndAppointmentStatus(Long clientId, AppointmentStatus attr0);


    @Query("SELECT a FROM Appointment a WHERE a.professional.id = :id " +
            "AND a.startDate < :endRange AND a.endDate > :startRange")
    List<Appointment> findBusyAppointmentsInRange(@Param("id") Long professionalId,
                                                  @Param("startRange") Instant startRange,
                                                  @Param("endRange") Instant endRange);

    @Query("""
    SELECT CASE WHEN COUNT(ap) > 0 THEN TRUE ELSE FALSE END
    FROM Appointment ap WHERE ap.mainSalonService.maintenanceIntervalDays != NULL
    AND ap.client.id = :clientId""")
    boolean clientBookedServiceRequiringMaintenance(@Param("clientId") Long clientId);

}
