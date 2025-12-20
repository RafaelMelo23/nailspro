package com.rafael.nailspro.webapp.service.infra.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.model.enums.UserRole;
import com.rafael.nailspro.webapp.model.enums.security.TokenClaim;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = recoverAndValidateToken(request);

        if (token != null) {

            Long userId = token.getClaim(TokenClaim.ID.getValue()).asLong();
            String userEmail = token.getSubject();
            UserRole userRole = UserRole.fromString(token.getClaim(TokenClaim.ROLE.getValue()).asString());

            UserPrincipal userPrincipal = UserPrincipal.builder()
                    .id(userId)
                    .email(userEmail)
                    .userRole(userRole)
                    .build();

            var authentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    userPrincipal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } filterChain.doFilter(request, response);
    }

    private DecodedJWT recoverAndValidateToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        Optional<Cookie> jwtCookie = (cookies == null)
                ? Optional.empty()
                : Arrays.stream(cookies)
                .filter(cookie -> "AUTH_TOKEN".equals(cookie.getName()))
                .findFirst();

        return jwtCookie.map(cookie -> tokenService.validateAndDecode(cookie.getValue()))
                .orElse(null);
    }
}
