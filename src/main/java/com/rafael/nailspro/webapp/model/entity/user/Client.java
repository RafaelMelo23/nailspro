package com.rafael.nailspro.webapp.model.entity.user;

import com.rafael.nailspro.webapp.model.entity.Appointment;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {
    private Integer missedAppointments;

    @OneToMany(mappedBy = "client")
    private List<Appointment> clientAppointments;
}