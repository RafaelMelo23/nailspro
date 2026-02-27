package com.rafael.nailspro.webapp.domain.model;

import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment")
public class Appointment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "externalId", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "main_service_id", nullable = false)
    private SalonService mainSalonService;

    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

    @Column(name = "observations", length = 120)
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointmentStatus appointmentStatus;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "salon_trade_name")
    private String salonTradeName;

    @Column(name = "salon_zone_id")
    private ZoneId salonZoneId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appointment_addon_id")
    private List<AppointmentAddOn> addOns = new ArrayList<>();

    @OneToMany(mappedBy = "appointment", orphanRemoval = true)
    private List<AppointmentNotification> appointmentNotifications = new ArrayList<>();


    public BigDecimal calculateTotalValue() {

        BigDecimal mainValue = BigDecimal.valueOf(this.mainSalonService.getValue());

        BigDecimal addOnsValue = this.addOns.stream()
                .map(addon -> BigDecimal.valueOf(addon.getService().getValue())
                        .multiply(BigDecimal.valueOf(addon.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return mainValue.add(addOnsValue);
    }

    public ZonedDateTime getZonedEndDate(ZoneId zoneId) {
        return endDate.atZone(zoneId);
    }
}