package com.rafael.nailspro.webapp.domain;

import com.rafael.nailspro.webapp.domain.model.SalonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BookingPolicyTest {

    @InjectMocks
    private BookingPolicy bookingPolicy;

    @Test
    void resolveAllowedWindowDays_prioritizeLoyalClient() {
        int resolved = bookingPolicy.resolveAllowedWindowDays(true, true, 30, 7);
        assertEquals(30, resolved);
    }

    @Test
    void determineStartDate_withoutMaintenanceOrLastAppointment_returnsToday() {
        LocalDate today = LocalDate.of(2026, 3, 17);
        LocalDate result = bookingPolicy.determineStartDate(
                List.of(new SalonService()),
                Optional.empty(),
                today
        );
        assertEquals(today, result);
    }

    @Test
    void calculateEarliestRecommendedDate_withoutLastAppointment_returnsNow() {
        Instant result = bookingPolicy.calculateEarliestRecommendedDate(Optional.empty());
        assertNotNull(result);
    }
}