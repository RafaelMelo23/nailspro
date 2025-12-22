package com.rafael.nailspro.webapp.model.entity;

import com.rafael.nailspro.webapp.model.entity.user.Client;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "externalId", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "main_service_id", nullable = false)
    private SalonService mainSalonService;

    @ManyToMany
    @JoinTable(name = "appointment_addons",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "services_id"))
    private List<SalonService> addOnSalonServices = new ArrayList<>();

    @Column(name = "total_value", nullable = false)
    private Integer totalValue;

    @Column(name = "observations", length = 120)
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointmentStatus appointmentStatus;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

}