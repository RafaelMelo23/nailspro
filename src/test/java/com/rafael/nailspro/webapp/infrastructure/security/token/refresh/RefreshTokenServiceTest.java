package com.rafael.nailspro.webapp.infrastructure.security.token.refresh;

import com.rafael.nailspro.webapp.domain.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void placeholder() {
        ReflectionTestUtils.setField(refreshTokenService, "tokenDurationMs", 60000L);
        assertTrue(true);
        // TODO: add unit tests for expiration and revoke
    }
}