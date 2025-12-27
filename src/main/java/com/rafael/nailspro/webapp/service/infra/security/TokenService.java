package com.rafael.nailspro.webapp.service.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafael.nailspro.webapp.model.entity.user.User;
import com.rafael.nailspro.webapp.model.enums.security.TokenClaim;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

@Service
public class TokenService {

    @Value("${api.security.jwt.secret:whatever}")
    private String secret;
    private static final String ISSUER = "nailspro-api";
    private static final String TENANT = "tenantId";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withClaim(TokenClaim.ID.getValue(), user.getId())
                    .withClaim(TokenClaim.ROLE.getValue(), user.getUserRole().getRole())
                    .withClaim(TENANT, user.getTenantId())
                    .withExpiresAt(generateExpirationTime())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Failed to create JWT", e);
        }
    }

    public DecodedJWT recoverAndValidateToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        Optional<Cookie> jwtCookie = (cookies == null)
                ? Optional.empty()
                : Arrays.stream(cookies)
                .filter(cookie -> "AUTH_TOKEN".equals(cookie.getName()))
                .findFirst();

        return jwtCookie.map(cookie -> validateAndDecode(cookie.getValue()))
                .orElse(null);
    }

    public DecodedJWT validateAndDecode(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public Instant generateExpirationTime() {

        return Instant.now().plus(24, ChronoUnit.HOURS);
    }
}
