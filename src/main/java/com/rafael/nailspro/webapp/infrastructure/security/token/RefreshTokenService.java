package com.rafael.nailspro.webapp.infrastructure.security.token;

import com.rafael.nailspro.webapp.domain.model.RefreshToken;
import com.rafael.nailspro.webapp.domain.model.User;
import com.rafael.nailspro.webapp.domain.repository.RefreshTokenRepository;
import com.rafael.nailspro.webapp.domain.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Value("${app.jwt.refreshExpirationMs}")
    private Long tokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        return saveRefreshToken(user);
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = getUser(userId);
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

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        repository.deleteByUser_Id(userId);
    }
}