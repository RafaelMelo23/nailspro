package com.rafael.nailspro.webapp.application.retention;

import com.rafael.nailspro.webapp.application.messages.RetentionMessageBuilder;
import com.rafael.nailspro.webapp.application.whatsapp.WhatsappMessageService;
import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.AppointmentAddOn;
import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import com.rafael.nailspro.webapp.domain.model.WhatsappMessage;
import com.rafael.nailspro.webapp.domain.repository.RetentionForecastRepository;
import com.rafael.nailspro.webapp.domain.enums.whatsapp.WhatsappMessageStatus;
import com.rafael.nailspro.webapp.domain.enums.whatsapp.WhatsappMessageType;
import com.rafael.nailspro.webapp.domain.whatsapp.SentMessageResult;
import com.rafael.nailspro.webapp.domain.whatsapp.WhatsappProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.rafael.nailspro.webapp.domain.enums.appointment.RetentionStatus.*;

@Service
@RequiredArgsConstructor
public class VisitPredictionService {

    private final RetentionForecastRepository repository;
    private final RetentionMessageBuilder messageBuilder;
    private final WhatsappProvider whatsappProvider;
    private final WhatsappMessageService whatsappMessageService;

    @Transactional
    public void createForecast(Appointment appointment) {
        List<RetentionForecast> forecastsToSave = new ArrayList<>();

        if (appointment.getMainSalonService().getMaintenanceIntervalDays() != null) {
            forecastsToSave.add(RetentionForecast.create(appointment, appointment.getMainSalonService()));
        }

        List<RetentionForecast> addOnRetentions = appointment.getAddOns().stream()
                .map(AppointmentAddOn::getService)
                .filter(ss -> ss.getMaintenanceIntervalDays() != null)
                .map(ss -> RetentionForecast.create(appointment, ss))
                .toList();

        forecastsToSave.addAll(addOnRetentions);

        if (!forecastsToSave.isEmpty()) {
            repository.saveAll(forecastsToSave);
        }
    }

    @Transactional
    public void markForecastAsExpired(RetentionForecast forecast) {
        if (forecast.getPredictedReturnDate().isAfter(Instant.now())) {
            throw new IllegalStateException("Can't expire an still active forecast");
        }
        forecast.setStatus(EXPIRED);
    }

    @Transactional
    public void markForecastAsConverted(RetentionForecast forecast) {
        forecast.setStatus(CONVERTED);
    }

    @Transactional
    public void sendMaintenanceMessage(Long retentionForecastId) {

        RetentionForecast retentionForecast =
                findByIdWithClientAppointmentAndService(retentionForecastId);

        WhatsappMessage messageRecord =
                whatsappMessageService.prepareRetentionMessage(
                        retentionForecastId,
                        WhatsappMessageType.RETENTION_MAINTENANCE
                );

        try {
            sendMessage(retentionForecast, messageRecord);
        } catch (Exception e) {
            handleFailedMessage(retentionForecast, messageRecord, e);
        }
    }

    private RetentionForecast findByIdWithClientAppointmentAndService(Long id) {
        return repository.findWithJoins(id)
                .orElseThrow(() -> new IllegalArgumentException("Retention forecast not found with the ID: " + id));
    }

    private void sendMessage(RetentionForecast retentionForecast, WhatsappMessage messageRecord) {
        String message = messageBuilder.buildRetentionMessage(retentionForecast);

        SentMessageResult result = whatsappProvider.sendText(
                retentionForecast.getTenantId(),
                message,
                retentionForecast.getClient().getPhoneNumber()
        );

        saveSuccessfulAttempt(result, messageRecord);
    }

    private void saveSuccessfulAttempt(SentMessageResult result, WhatsappMessage messageRecord) {
        whatsappMessageService.updateMessageStatus(
                WhatsappMessageStatus.fromEvolutionStatus(result.status()),
                null,
                result.messageId(),
                messageRecord.getId()
        );
    }

    private void handleFailedMessage(RetentionForecast retentionForecast,
                                     WhatsappMessage messageRecord,
                                     Exception e) {
        retentionForecast.setStatus(FAILED_TO_SEND);
        whatsappMessageService.updateMessageStatus(
                WhatsappMessageStatus.FAILED,
                e.getMessage(),
                null,
                messageRecord.getId()
        );
    }
}