package com.rafael.nailspro.webapp.infrastructure.security;

import com.rafael.nailspro.webapp.application.salon.service.SalonProfileService;
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
public class SalonMaintenanceFilter extends OncePerRequestFilter {

    private final SalonProfileService salonProfileService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/webhook");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isWhitelisted = path.startsWith("/admin") ||
                path.startsWith("/api/v1/auth") ||
                path.contains("/css/") ||
                path.contains("/js/") ||
                path.contains("/entrar");

        if (TenantContext.getTenant() != null &&
                !isWhitelisted &&
                !salonProfileService.isSalonOpenByTenantId(TenantContext.getTenant())) {

            request.getRequestDispatcher("/offline")
                    .forward(request, response);

            return;
        }


        filterChain.doFilter(request, response);
    }
}
