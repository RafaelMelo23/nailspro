package com.rafael.nailspro.webapp.model.entity.profile;

import com.rafael.nailspro.webapp.model.entity.BaseEntity;
import com.rafael.nailspro.webapp.model.entity.user.Professional;
import com.rafael.nailspro.webapp.model.enums.OperationalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "salon_profile")
public class SalonProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
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

    @Column(name = "appointment_buffer_minutes", nullable = false)
    private Integer appointmentBufferMinutes;

    @Column(name = "domain_slug", nullable = false, unique = true, length = 40)
    private String domainSlug;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private Professional owner;

    //todo: add
    @Column(name = "salon_zone_id", nullable = false)
    private ZoneId zoneId;

    @Override
    public void prePersist() {
        super.prePersist();

        if (this.tradeName == null || this.tradeName.isBlank()) {
            this.tradeName = "Novo Estabelecimento";
        } else {
            this.tradeName = this.tradeName.trim();
        }

        if (this.primaryColor == null || this.primaryColor.isBlank()) {
            this.primaryColor = "#FB7185";
        }

        if (this.logoPath == null || this.logoPath.isBlank()) {
            this.logoPath = "default-logo.png";
        }

        if (this.comercialPhone == null || this.comercialPhone.isBlank()) {
            this.comercialPhone = "00000000000";
        } else {
            this.comercialPhone = this.comercialPhone.replaceAll("\\D", "");
        }

        if (this.fullAddress == null || this.fullAddress.isBlank()) {
            this.fullAddress = "Endereço a preencher";
        }

        if (this.appointmentBufferMinutes == null) {
            this.appointmentBufferMinutes = 0;
        }

        if (this.operationalStatus == null) {
            this.operationalStatus = OperationalStatus.OPEN;
        }
    }
}