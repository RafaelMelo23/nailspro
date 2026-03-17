package com.rafael.nailspro.webapp.application.appointment.booking;

import com.rafael.nailspro.webapp.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
class FindProfessionalAvailabilityUseCaseIT extends BaseIntegrationTest {

    @Autowired
    private FindProfessionalAvailabilityUseCase findProfessionalAvailabilityUseCase;

    @Test
    void placeholder() {
        // TODO: add integration test for availability lookup
    }
}