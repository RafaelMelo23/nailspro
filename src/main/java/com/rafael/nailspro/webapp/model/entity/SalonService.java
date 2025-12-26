package com.rafael.nailspro.webapp.model.entity;

import com.rafael.nailspro.webapp.model.entity.user.Professional;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
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
    private Boolean active = true;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToMany
    @JoinTable(name = "service_professionals",
            joinColumns = @JoinColumn(name = "salonService_id"),
            inverseJoinColumns = @JoinColumn(name = "professionals_id"))
    private Set<Professional> professionals = new LinkedHashSet<>();

    @Column(name = "is_add_on") // todo: put nullable false
    private Boolean isAddOn = false;

    @PrePersist
    protected void onCreate() {
        this.active = true;
        this.isDeleted = false;
    }

}