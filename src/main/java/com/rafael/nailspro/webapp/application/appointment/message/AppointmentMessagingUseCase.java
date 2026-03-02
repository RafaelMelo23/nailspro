package com.rafael.nailspro.webapp.application.appointment.message;

import com.rafael.nailspro.webapp.application.messages.AppointmentMessageBuilder;
import com.rafael.nailspro.webapp.application.whatsapp.WhatsappProvider;
import com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus.FAILED;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationStatus.SENT;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType.CONFIRMATION;
import static com.rafael.nailspro.webapp.domain.enums.appointment.AppointmentNotificationType.REMINDER;

@Log4j2
@Service
@RequiredArgsConstructor
public class AppointmentMessagingUseCase {

    private final WhatsappProvider whatsappProvider;
    private final AppointmentMessageBuilder messageBuilder;
    private final AppointmentNotificationService appointmentNotificationService;

    public void processNotification(Long appointmentId, AppointmentNotificationType type) {

        var notification = appointmentNotificationService.prepareNotification(appointmentId, type);

        try {
            var message = buildMessage(notification.getAppointment(), type);

            dispatchMessage(
                    notification.getAppointment(),
                    message,
                    notification.getDestinationNumber()
            );

            appointmentNotificationService.updateNotificationStatus(
                    notification.getId(),
                    SENT,
                    null
            );

        } catch (Exception e) {

            appointmentNotificationService.updateNotificationStatus(
                    notification.getId(),
                    FAILED,
                    e.getMessage()
            );
        }
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