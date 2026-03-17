package com.rafael.nailspro.webapp.application.retention;

import com.rafael.nailspro.webapp.support.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
class FollowUpAutomationServiceIT extends BaseIntegrationTest {

    @Autowired
    private FollowUpAutomationService followUpAutomationService;

    @Test
    void placeholder() {
        // TODO: add integration test for follow-up window selection
    }
}