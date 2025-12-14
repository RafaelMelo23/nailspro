package com.rafael.nailspro.webapp.model.entity.client;

import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.entity.user.User;
import com.rafael.nailspro.webapp.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Column(name = "missed_appointments", nullable = false)
    private Integer missedAppointments = 0;

    @OneToMany(mappedBy = "client")
    private List<Appointment> clientAppointments;

    @PrePersist
    public void prePersist() {
        setUserRole(UserRole.CLIENT);
    }
}