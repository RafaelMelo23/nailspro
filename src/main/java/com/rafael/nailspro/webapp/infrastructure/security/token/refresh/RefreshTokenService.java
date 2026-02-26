package com.rafael.nailspro.webapp.infrastructure.security.token.refresh;

import com.rafael.nailspro.webapp.domain.model.RefreshToken;
import com.rafael.nailspro.webapp.domain.model.User;
import com.rafael.nailspro.webapp.domain.repository.RefreshTokenRepository;
import com.rafael.nailspro.webapp.infrastructure.exception.TokenRefreshException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${app.jwt.refreshExpirationMs}")
    private Long tokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Transactional
    public void deleteExpiredTokens() {
        repository.deleteByExpiryDateBefore(Instant.now());
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        return saveRefreshToken(user);
    }

    private RefreshToken saveRefreshToken(User user) {
        return repository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(tokenDurationMs))
                        .isRevoked(false)
                        .build()
        );
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new TokenRefreshException("Expirado");
        }
        return token;
    }

    @Transactional
    public void revokeAllForUser(Long userId) {

        repository.revokeAllUserTokens(userId);
    }
}