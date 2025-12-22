package com.rafael.nailspro.webapp.model.entity;

import com.rafael.nailspro.webapp.model.enums.OperationalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "salon_profile")
public class SalonProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "externalId", nullable = false)
    private Long id;

    @Column(name = "trade_name", nullable = false, length = 60)
    private String tradeName;

    @Column(name = "slogan", length = 120)
    private String slogan;

    @Column(name = "primary_color", nullable = false, length = 15)
    private String primaryColor;

    @Column(name = "logo_path", nullable = false)
    private String logoPath;

    @Column(name = "comercial_phone", nullable = false, length = 11)
    private String comercialPhone;

    @Column(name = "full_address", nullable = false, length = 80)
    private String fullAddress;

    @Column(name = "social_media_link", length = 50)
    private String socialMediaLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", nullable = false)
    private OperationalStatus operationalStatus = OperationalStatus.OPEN;

    @Column(name = "warning_message", length = 200)
    private String warningMessage;

    @Column(name = "appointment_cancel_window_in_minutes", nullable = false)
    private Integer appointmentCancelWindowInMinutes;

}