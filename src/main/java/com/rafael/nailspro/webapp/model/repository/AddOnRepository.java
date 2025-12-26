package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.AppointmentAddOn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnRepository extends JpaRepository<AppointmentAddOn, Long> {
}
