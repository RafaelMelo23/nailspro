package com.rafael.nailspro.webapp.domain.user;

import com.rafael.nailspro.webapp.domain.appointment.Appointment;
import com.rafael.nailspro.webapp.domain.enums.UserRole;
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

    @Column(name = "cancelled_appointments", nullable = false)
    private Integer canceledAppointments = 0;

    @OneToMany(mappedBy = "client")
    private List<Appointment> clientAppointments;

    @Column(name = "phone_number", nullable = false, unique = true, length = 13)
    private String phoneNumber;

    @Override
    public void prePersist() {
        super.prePersist();

        setUserRole(UserRole.CLIENT);

        if (this.missedAppointments == null) this.missedAppointments = 0;
        if (this.canceledAppointments == null) this.setCanceledAppointments(0);
    }
}