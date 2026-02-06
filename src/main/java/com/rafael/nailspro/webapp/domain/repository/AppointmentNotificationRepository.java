package com.rafael.nailspro.webapp.domain.repository;

import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.AppointmentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentNotificationRepository extends JpaRepository<AppointmentNotification, Long> {


    @Query("SELECT ap FROM Appointment ap WHERE ap.id IN :appointments")
    List<AppointmentNotification> findByAppointments(@Param("appointments") List<Appointment> appointmentId);
}
