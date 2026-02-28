package com.rafael.nailspro.webapp.application.appointment.message;

import com.rafael.nailspro.webapp.application.appointment.AppointmentService;
import com.rafael.nailspro.webapp.application.messages.AppointmentMessageBuilder;
import com.rafael.nailspro.webapp.application.whatsapp.WhatsappProvider;
import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus;
import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.AppointmentNotification;
import com.rafael.nailspro.webapp.domain.repository.AppointmentNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus.FAILED;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus.SENT;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType.CONFIRMATION;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType.REMINDER;

@Log4j2
@Service
@RequiredArgsConstructor
public class AppointmentMessagingUseCase {

    private final AppointmentService appointmentService;
    private final AppointmentNotificationRepository notificationRepository;
    private final WhatsappProvider whatsappProvider;
    private final AppointmentMessageBuilder messageBuilder;
    @Lazy private final AppointmentMessagingUseCase self;

    public void processNotification(Long appointmentId, AppointmentNotificationType type) {
        AppointmentNotification notification = self.prepareNotification(appointmentId, type);

        try {
            var message = buildMessage(notification.getAppointment(), type);
            dispatchMessage(notification.getAppointment(), message, notification.getDestinationNumber());

            self.updateNotificationStatus(notification.getId(), SENT, null);
        } catch (Exception e) {
            log.error("Erro ao enviar {} para agendamento {}", type, appointmentId, e);
            self.updateNotificationStatus(notification.getId(), FAILED, e.getMessage());
        }
    }

    @Transactional
    public AppointmentNotification prepareNotification(Long appointmentId, AppointmentNotificationType type) {
        return notificationRepository
                .findByAppointmentIdAndNotificationType(appointmentId, type)
                .orElseGet(() -> {
                    var appointment = appointmentService.findById(appointmentId);
                    var newNotif = AppointmentNotification.builder()
                            .appointment(appointment)
                            .destinationNumber(appointment.getClient().getPhoneNumber())
                            .notificationType(type)
                            .notificationStatus(FAILED)
                            .tenantId(appointment.getTenantId())
                            .attempts(0)
                            .build();
                    return notificationRepository.save(newNotif);
                });
    }

    @Transactional
    public void updateNotificationStatus(Long id, AppointmentNotificationStatus status, String error) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setNotificationStatus(status);
            n.setAttempts(n.getAttempts() + 1);
            n.setLastAttemptAt(Instant.now());
            n.setLastErrorMessage(error);
            if (status == SENT) n.setSentAt(Instant.now());
            notificationRepository.save(n);
        });
    }

    @Async
    public void sendAppointmentConfirmationMessageAsync(Long appointmentId) {
        processNotification(appointmentId, CONFIRMATION);
    }

    @Transactional
    public void sendAppointmentReminderMessage(Long appointmentId) {
        processNotification(appointmentId, REMINDER);
    }

    private String buildMessage(Appointment appointment, AppointmentNotificationType type) {
        return switch (type) {
            case CONFIRMATION -> messageBuilder.buildAppointmentConfirmationMessage(appointment);
            case REMINDER -> messageBuilder.buildAppointmentReminderMessage(appointment);
        };
    }

    private void dispatchMessage(Appointment appointment,
                                 String message,
                                 String targetNumber) {
        whatsappProvider.sendText(
                appointment.getTenantId(),
                message,
                targetNumber
        );
    }
}