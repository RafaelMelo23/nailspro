package com.rafael.nailspro.webapp.infrastructure.controller.api.superadmin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SentryVerificationController {

    @GetMapping("/api/internal/debug-sentry")
    public String triggerSentryError() {
        try {
            throw new RuntimeException("Sentry Verification Exception - Production Environment");
        } catch (Exception e) {

            io.sentry.Sentry.captureException(e);
            return "Test exception has been sent to Sentry.";
        }
    }
}

