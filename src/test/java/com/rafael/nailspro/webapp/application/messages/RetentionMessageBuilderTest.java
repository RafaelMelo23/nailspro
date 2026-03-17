package com.rafael.nailspro.webapp.application.messages;

import com.rafael.nailspro.webapp.infrastructure.helper.DateAndZoneHelper;
import com.rafael.nailspro.webapp.infrastructure.helper.TenantUrlProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RetentionMessageBuilderTest {

    @Mock
    private TenantUrlProvider urlProvider;
    @Mock
    private DateAndZoneHelper dateHelper;

    @InjectMocks
    private RetentionMessageBuilder retentionMessageBuilder;

    @Test
    void placeholder() {
        assertTrue(true);
        // TODO: add unit tests for message formatting
    }
}