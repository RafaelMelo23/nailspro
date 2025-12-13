package com.rafael.nailspro.webapp.model.entity;

import com.rafael.nailspro.webapp.model.entity.user.professional.Professional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "service")
public class SalonService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 200)
    private String name;

    @Column(name = "description", nullable = false, unique = true, length = 250)
    private String description;

    @Column(name = "nail_count", nullable = false)
    private Integer nailCount;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @ManyToMany
    @JoinTable(name = "service_professionals",
            joinColumns = @JoinColumn(name = "salonService_id"),
            inverseJoinColumns = @JoinColumn(name = "professionals_id"))
    private Set<Professional> professionals = new LinkedHashSet<>();

}