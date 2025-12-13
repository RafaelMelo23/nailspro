package com.rafael.nailspro.webapp.model.entity.user.professional;

import com.rafael.nailspro.webapp.model.entity.Appointment;
import com.rafael.nailspro.webapp.model.entity.SalonService;
import com.rafael.nailspro.webapp.model.entity.user.User;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "professionals")
@PrimaryKeyJoinColumn(name = "user_id")
public class Professional extends User {
    private String professionalPicture;

    @OneToOne
    private WorkSchedule workSchedule;

    @OneToMany(mappedBy = "professional")
    private List<Appointment> professionalAppointments;

    @ManyToMany(mappedBy = "professionals")
    private Set<SalonService> salonServices = new LinkedHashSet<>();
}
