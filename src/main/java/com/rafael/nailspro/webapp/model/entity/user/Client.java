package com.rafael.nailspro.webapp.model.entity.user;

import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Column(name = "missed_appointments", nullable = false)
    private Integer missedAppointments = 0;

    @OneToMany(mappedBy = "client")
    private List<Appointment> clientAppointments;

    @Column(name = "phone_number", nullable = false, unique = true, length = 13)
    private String phoneNumber;

    @PrePersist
    public void prePersist() {
        setUserRole(UserRole.CLIENT);
    }
}