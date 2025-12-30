package com.rafael.nailspro.webapp.service.infra.security;

import com.rafael.nailspro.webapp.model.entity.TenantContext;
import com.rafael.nailspro.webapp.service.salon.service.SalonProfileService;
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        boolean isWhitelisted = path.startsWith("/admin") ||
                path.startsWith("/api/v1/auth") ||
                path.contains("/css/") ||
                path.contains("/js/") ||
                path.contains("/entrar");


        if (!isWhitelisted && !salonProfileService.isSalonOpenByTenantId(TenantContext.getTenant())) {

            request.getRequestDispatcher("/offline")
                    .forward(request, response);

            return;
        }

        filterChain.doFilter(request, response);
    }
}
