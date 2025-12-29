package com.rafael.nailspro.webapp.service.infra.security;

import com.rafael.nailspro.webapp.model.entity.user.UserPrincipal;
import com.rafael.nailspro.webapp.model.enums.UserRole;
import com.rafael.nailspro.webapp.model.enums.security.TokenClaim;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.rafael.nailspro.webapp.model.enums.security.TokenPurpose.AUTHENTICATION;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = tokenService.recoverAndValidateToken(request);

        if (token != null) {
            String tokenPurposeClaim = token.getClaim("purpose").asString();

            if (AUTHENTICATION.getValue().equalsIgnoreCase(tokenPurposeClaim)) {

                UserRole userRole = UserRole.fromString(token.getClaim(TokenClaim.ROLE.getValue()).asString());
                Long userId = Long.parseLong(token.getSubject());
                String userEmail = token.getClaim(TokenClaim.EMAIL.getValue()).asString();
                String tenantId = token.getClaim(TokenClaim.TENANT_ID.getValue()).asString();

                UserPrincipal userPrincipal = UserPrincipal.builder()
                        .userId(userId)
                        .email(userEmail)
                        .userRole(userRole)
                        .tenantId(tenantId)
                        .build();

                var authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
