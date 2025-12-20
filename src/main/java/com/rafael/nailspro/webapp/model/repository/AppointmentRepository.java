package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> getClientAppointmentsById(Long id, Pageable pageable);
}
