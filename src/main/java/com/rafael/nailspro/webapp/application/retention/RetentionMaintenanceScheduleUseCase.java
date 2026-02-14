package com.rafael.nailspro.webapp.application.retention;

import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import com.rafael.nailspro.webapp.domain.repository.RetentionForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.rafael.nailspro.webapp.domain.enums.RetentionStatus.FAILED_TO_SEND;
import static com.rafael.nailspro.webapp.domain.enums.RetentionStatus.PENDING;

@Component
@RequiredArgsConstructor
public class RetentionMaintenanceScheduleUseCase {

    private final RetentionForecastUseCase retentionForecastUseCase;
    private final RetentionForecastRepository repository;

    @Scheduled(cron = "0 30 9 * * *")
    public void sendMaintenanceForecastMessage() {
        Instant now = Instant.now();
        Instant twoDaysFromNow = now.plus(2, ChronoUnit.DAYS);

        List<RetentionForecast> forecasts =
                repository.findAllPredictedForecastsBetween(now, twoDaysFromNow, List.of(PENDING, FAILED_TO_SEND));

        forecasts.forEach(fr -> retentionForecastUseCase.sendMaintenanceMessage(fr.getId()));
    }
}