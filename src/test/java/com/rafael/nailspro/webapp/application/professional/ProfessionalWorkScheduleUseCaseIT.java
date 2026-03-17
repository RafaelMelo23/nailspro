package com.rafael.nailspro.webapp.application.professional;

import com.rafael.nailspro.webapp.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
class ProfessionalWorkScheduleUseCaseIT extends BaseIntegrationTest {

    @Autowired
    private ProfessionalWorkScheduleUseCase professionalWorkScheduleUseCase;

    @Test
    void placeholder() {
        // TODO: add integration tests for schedule registration and validation
    }
}