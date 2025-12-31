package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.appointment.AppointmentAddOn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnRepository extends JpaRepository<AppointmentAddOn, Long> {
}
