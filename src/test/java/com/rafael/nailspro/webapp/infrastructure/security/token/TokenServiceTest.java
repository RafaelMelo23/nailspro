package com.rafael.nailspro.webapp.infrastructure.security.token;

import com.rafael.nailspro.webapp.domain.enums.user.UserRole;
import com.rafael.nailspro.webapp.domain.model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Test
    void generateAuthToken_createsJwt() {
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");

        Client user = new Client();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.CLIENT);
        user.setTenantId("tenant-1");

        String token = tokenService.generateAuthToken(user);
        assertNotNull(token);
    }
}