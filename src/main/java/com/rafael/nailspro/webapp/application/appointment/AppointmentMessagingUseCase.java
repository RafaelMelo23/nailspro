package com.rafael.nailspro.webapp.application.appointment;

import com.rafael.nailspro.webapp.application.messages.AppointmentMessageBuilder;
import com.rafael.nailspro.webapp.application.whatsapp.WhatsappProvider;
import com.rafael.nailspro.webapp.domain.model.AppointmentNotification;
import com.rafael.nailspro.webapp.domain.repository.AppointmentNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    @Async
    @Transactional
    public void sendAppointmentConfirmationMessage(Long appointmentId) {
        var appointment = appointmentService.findById(appointmentId);
        var status = SENT;
        var targetNumber = appointment.getClient().getPhoneNumber();
        String logError = null;

        try {
            var confirmationMessage = messageBuilder.buildAppointmentConfirmationMessage(appointment);

            Map<String, Object> response = whatsappProvider.sendText(
                    appointment.getTenantId(),
                    confirmationMessage,
                    targetNumber
            );

            log.info("EVO API RESPONSE: {}", response);
        } catch (Exception e) {
            log.error("Erro ao processar envio para agendamento {}", appointment.getId(), e);
            status = FAILED;
            logError = e.toString();
        }

        notificationRepository.save(AppointmentNotification.builder()
                .destinationNumber(targetNumber)
                .appointmentNotificationType(CONFIRMATION)
                .appointmentNotificationStatus(status)
                .appointment(appointment)
                .failedMessageContent(logError)
                .build());
    }

    @Transactional
    public void sendAppointmentReminderMessage(Long appointmentId) {
        var appointment = appointmentService.findById(appointmentId);
        var targetNumber = appointment.getClient().getPhoneNumber();
        var status = SENT;
        String logError = null;

        try {
            var reminderMessage = messageBuilder.buildAppointmentReminderMessage(appointment);

            whatsappProvider.sendText(
                    appointment.getTenantId(),
                    reminderMessage,
                    targetNumber
            );

        } catch (Exception e) {
            log.error("Erro ao processar envio de lembrete para agendamento {}", appointmentId, e);
            status = FAILED;
            logError = e.toString();
        }

        notificationRepository.save(AppointmentNotification.builder()
                .destinationNumber(targetNumber)
                .appointmentNotificationType(REMINDER)
                .appointmentNotificationStatus(status)
                .appointment(appointment)
                .failedMessageContent(logError)
                .build());
    }

}
