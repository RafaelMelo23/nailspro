package com.rafael.nailspro.webapp.domain.model;

import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus;
import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment_notification")
public class AppointmentNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_external_id")
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_notification_type", nullable = false)
    private AppointmentNotificationType appointmentNotificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_notification_status", nullable = false)
    private AppointmentNotificationStatus appointmentNotificationStatus;

    @Column(name = "external_message_id")
    private String externalMessageId;

    @Column(name = "destination_number")
    private String destinationNumber;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "failed_message_content", length = 500)
    private String failedMessageContent;
}