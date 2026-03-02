package com.rafael.nailspro.webapp.domain;

import com.rafael.nailspro.webapp.domain.model.Appointment;
import com.rafael.nailspro.webapp.domain.model.AppointmentAddOn;
import com.rafael.nailspro.webapp.domain.model.SalonService;
import com.rafael.nailspro.webapp.infrastructure.dto.appointment.booking.AppointmentTimeWindow;
import com.rafael.nailspro.webapp.infrastructure.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class BookingPolicy {

    public int resolveAllowedWindowDays(
            boolean prioritizeLoyalClients,
            boolean isLoyalClient,
            int loyalClientWindowDays,
            int standardWindowDays
    ) {
        if (!prioritizeLoyalClients) {
            return standardWindowDays;
        }

        return isLoyalClient
                ? loyalClientWindowDays
                : standardWindowDays;
    }

    public void validateBookingHorizon(
            LocalDate requestedDate,
            int allowedDays,
            LocalDate today
    ) {
        long daysAhead = ChronoUnit.DAYS.between(today, requestedDate);

        if (daysAhead > allowedDays) {
            throw new BusinessException(
                    "Sua janela de agendamento não é preferencial, aguarde alguns dias para reservar esta data."
            );
        }
    }

    public AppointmentTimeWindow buildWindow(
            LocalDate startDate,
            int windowDays
    ) {
        return AppointmentTimeWindow.builder()
                .start(startDate)
                .end(startDate.plusDays(windowDays))
                .build();
    }

    public LocalDate determineStartDate(
            List<SalonService> services,
            Optional<Appointment> lastAppointment,
            LocalDate today
    ) {
        Integer maintenanceInterval = services.stream()
                .map(SalonService::getMaintenanceIntervalDays)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(null);

        if (maintenanceInterval == null || lastAppointment.isEmpty()) {
            return today;
        }

        Appointment last = lastAppointment.get();
        ZoneId zoneId = last.getSalonZoneId();

        return LocalDate
                .ofInstant(last.getStartDate(), zoneId)
                .plusDays(maintenanceInterval - 3);
    }

    public Instant calculateEarliestRecommendedDate(Optional<Appointment> lastAppointment) {

        if (lastAppointment.isEmpty()) {
            return Instant.now();
        }

        Appointment last = lastAppointment.get();

        List<SalonService> allServices =
                Stream.concat(
                        Stream.of(last.getMainSalonService()),
                        last.getAddOns().stream().map(AppointmentAddOn::getService)
                ).toList();

        Integer maxInterval = allServices.stream()
                .map(SalonService::getMaintenanceIntervalDays)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);

        return last.getStartDate().plus(maxInterval, ChronoUnit.DAYS);
    }
}