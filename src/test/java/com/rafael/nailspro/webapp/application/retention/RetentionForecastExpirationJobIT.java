package com.rafael.nailspro.webapp.application.retention;

import com.rafael.nailspro.webapp.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
class RetentionForecastExpirationJobIT extends BaseIntegrationTest {

    @Autowired
    private RetentionForecastExpirationJob retentionForecastExpirationJob;

    @Test
    void placeholder() {
        // TODO: add integration test for expiration job
    }
}