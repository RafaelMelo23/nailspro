package com.rafael.nailspro.webapp.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "appointment_addons_record")
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentAddOn {

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
