package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.enums.AppointmentStatus;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
