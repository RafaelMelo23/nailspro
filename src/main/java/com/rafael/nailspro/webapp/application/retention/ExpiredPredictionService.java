package com.rafael.nailspro.webapp.application.retention;

import com.rafael.nailspro.webapp.domain.enums.appointment.RetentionStatus;
import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import com.rafael.nailspro.webapp.domain.repository.RetentionForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpiredPredictionService {

    private final VisitPredictionService visitPredictionService;
    private final RetentionForecastRepository repository;

    @Scheduled(cron = "0 0 0 * * *")
    public void expireForecasts() {

        List<RetentionForecast> expiredForecasts =
                repository.findAllExpiredPredictedForecastsAndNotInStatus(Instant.now(), RetentionStatus.EXPIRED);

        expiredForecasts.forEach(visitPredictionService::markForecastAsExpired);
    }
}