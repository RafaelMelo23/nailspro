package com.rafael.nailspro.webapp.application.retention;

import com.rafael.nailspro.webapp.application.messages.RetentionMessageBuilder;
import com.rafael.nailspro.webapp.domain.repository.RetentionForecastRepository;
import com.rafael.nailspro.webapp.domain.whatsapp.WhatsappProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class VisitPredictionServiceTest {

    @Mock
    private RetentionForecastRepository repository;
    @Mock
    private RetentionMessageBuilder messageBuilder;
    @Mock
    private WhatsappProvider whatsappProvider;

    @InjectMocks
    private VisitPredictionService visitPredictionService;

    @Test
    void placeholder() {
        assertTrue(true);
        // TODO: add unit tests for forecast creation and message send behavior
    }
}