package com.rafael.nailspro.webapp.support.factory;

import com.rafael.nailspro.webapp.domain.enums.appointment.RetentionStatus;
import com.rafael.nailspro.webapp.domain.model.Client;
import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.domain.model.RetentionForecast;
import com.rafael.nailspro.webapp.domain.model.SalonService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TestRetentionForecastFactory {

    private static RetentionForecast.RetentionForecastBuilder<?, ?> baseBuilder() {
        return RetentionForecast.builder()
                .id(null)
                .client(null)
                .professional(TestProfessionalFactory.standard())
                .status(RetentionStatus.PENDING)
                .predictedReturnDate(Instant.now().plus(15, ChronoUnit.DAYS));
    }

    public static RetentionForecast standard() {
        return baseBuilder().build();
    }

    public static RetentionForecast standardForIt(Professional p,
                                                  Client c,
                                                  List<SalonService> s) {
        return baseBuilder()
                .client(c)
                .professional(p)
                .salonServices(s)
                .build();
    }

    public static RetentionForecast expiredForIt(Professional p,
                                                 Client c,
                                                 List<SalonService> s) {
        long randomDays = ThreadLocalRandom.current().nextLong(1, 61);

        return baseBuilder()
                .predictedReturnDate(Instant.now().minus(randomDays, ChronoUnit.DAYS))
                .status(RetentionStatus.PENDING)
                .professional(p)
                .client(c)
                .salonServices(s)
                .build();
    }

    public static RetentionForecast activeWithOldReturnDate(Professional p,
                                                Client c,
                                                List<SalonService> s) {
        long randomDays = ThreadLocalRandom.current().nextLong(1, 61);

        return baseBuilder()
                .status(RetentionStatus.PENDING)
                .predictedReturnDate(Instant.now().minus(randomDays, ChronoUnit.DAYS))
                .professional(p)
                .client(c)
                .salonServices(s)
                .build();
    }

    public static RetentionForecast activeForIt(Professional p,
                                                Client c,
                                                List<SalonService> s) {
        return baseBuilder()
                .status(RetentionStatus.PENDING)
                .predictedReturnDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .professional(p)
                .client(c)
                .salonServices(s)
                .build();
    }

    public static RetentionForecast withId(Long id) {
        RetentionForecast forecast = standard();
        forecast.setId(id);
        return forecast;
    }
}