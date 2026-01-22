package com.rafael.nailspro.webapp.infrastructure.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/webhook");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenantId = null;

        try {
            DecodedJWT token = tokenService.recoverAndValidateToken(request);

            if (token != null) {
                tenantId = token.getClaim("tenantId").asString();

            } else {
                String serverName = request.getServerName();
                tenantId = serverName.split("\\.")[0];
            }

            if (tenantId != null) {
                TenantContext.setTenant(tenantId);
            }
        } catch (Exception ignored) {

        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
