package com.rafael.nailspro.webapp.model.entity.appointment;

import com.rafael.nailspro.webapp.model.entity.BaseEntity;
import com.rafael.nailspro.webapp.model.entity.salon.service.SalonService;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "appointment_addons_record")
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentAddOn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private SalonService service;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price_at_moment")
    private Integer unitPriceSnapshot;
}
