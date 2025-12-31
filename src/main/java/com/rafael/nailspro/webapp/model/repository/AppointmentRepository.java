package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.dto.appointment.AppointmentCreateDTO;
import com.rafael.nailspro.webapp.model.dto.appointment.ProfessionalAppointmentScheduleDTO;
import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.entity.user.Client;
import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> getClientAppointmentsById(Long id, Pageable pageable);

    @Modifying
    @Query("UPDATE Appointment ap SET ap.appointmentStatus = :status WHERE ap.id = :id")
    void updateAppointmentStatus(@Param("id") Long id, @Param("status") AppointmentStatus status);

    Page<Appointment> findByClient_Id(Long userId, Pageable pageable);

    List<Appointment> findByProfessional_IdAndStartDateBetween(Long professionalId, LocalDateTime startDateAfter, LocalDateTime startDateBefore);
}
