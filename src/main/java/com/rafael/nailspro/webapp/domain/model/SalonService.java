package com.rafael.nailspro.webapp.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@Table(name = "service")
public class SalonService extends BaseEntity {
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

    @Column(name = "duration_in_seconds", nullable = false)
    private Integer durationInSeconds;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // todo: include these 3 new attributes in the creation of a service

    @Column(name = "maintenance_interval_days")
    private Integer maintenanceIntervalDays;

    @Column(name = "requires_loyalty")
    private Boolean requiresLoyalty = false;

    @ManyToMany
    @JoinTable(name = "service_professionals",
            joinColumns = @JoinColumn(name = "salonService_id"),
            inverseJoinColumns = @JoinColumn(name = "professionals_id"))
    private Set<Professional> professionals = new LinkedHashSet<>();

    @Column(name = "is_add_on") // todo: put nullable false
    private Boolean isAddOn = false;

    @Override
    public void prePersist() {
        this.active = true;
        this.isDeleted = false;
    }
}