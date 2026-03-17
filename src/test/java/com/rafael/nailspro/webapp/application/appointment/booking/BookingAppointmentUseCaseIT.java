package com.rafael.nailspro.webapp.application.appointment.booking;

import com.rafael.nailspro.webapp.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
class BookingAppointmentUseCaseIT extends BaseIntegrationTest {

    @Autowired
    private BookingAppointmentUseCase bookingAppointmentUseCase;

    @Test
    void placeholder() {
        // TODO: add integration test for booking flow
    }
}