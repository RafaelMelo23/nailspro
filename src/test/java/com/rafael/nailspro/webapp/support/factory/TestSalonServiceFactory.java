package com.rafael.nailspro.webapp.support.factory;

import com.rafael.nailspro.webapp.domain.model.Professional;
import com.rafael.nailspro.webapp.domain.model.SalonService;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestSalonServiceFactory {

    private static SalonService.SalonServiceBuilder baseBuilder() {
        return SalonService.builder()
                .id(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE))
                .name("Service-" + UUID.randomUUID())
                .description("Desc-" + UUID.randomUUID())
                .durationInSeconds(3600)
                .value(50)
                .active(true)
                .maintenanceIntervalDays(15)
                .requiresLoyalty(false)
                .isAddOn(false)
                .professionals(new LinkedHashSet<>())
                .tenantId("tenant-test");
    }

    public static SalonService standard() {
        return baseBuilder().build();
    }

    public static SalonService withCustomValue(Integer value) {
        return baseBuilder()
                .value(value)
                .build();
    }

    public static SalonService standardWithoutMaintenanceInterval() {
        return baseBuilder()
                .maintenanceIntervalDays(null)
                .build();
    }

    public static SalonService addOnWithoutMaintenanceInterval() {
        return baseBuilder()
                .isAddOn(true)
                .nailCount(1)
                .durationInSeconds(900)
                .value(15)
                .maintenanceIntervalDays(null)
                .build();
    }

    public static SalonService addOnWithMaintenanceInterval() {
        return addOnWithMaintenanceInterval(10);
    }

    public static SalonService addOnWithMaintenanceInterval(int days) {
        return baseBuilder()
                .isAddOn(true)
                .nailCount(1)
                .durationInSeconds(900)
                .value(15)
                .maintenanceIntervalDays(days)
                .build();
    }

    public static SalonService withMaintenanceInterval(int days) {
        return baseBuilder()
                .maintenanceIntervalDays(days)
                .build();
    }

    public static SalonService withProfessionals(Set<Professional> professionals) {
        return baseBuilder()
                .professionals(professionals)
                .build();
    }

    public static SalonService withDuration(int durationSeconds) {
        return baseBuilder()
                .durationInSeconds(durationSeconds)
                .build();
    }

    public static SalonService withInterval(Integer interval) {
        return baseBuilder()
                .maintenanceIntervalDays(interval).build();
    }
}