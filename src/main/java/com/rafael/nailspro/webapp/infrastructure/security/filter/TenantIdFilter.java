package com.rafael.nailspro.webapp.infrastructure.security.filter;

import com.rafael.nailspro.webapp.shared.tenant.TenantContext;
import com.rafael.nailspro.webapp.shared.tenant.TenantResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantIdFilter extends OncePerRequestFilter {

    private final TenantResolver tenantResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/webhook") || path.startsWith("/api/internal") || path.startsWith("/public");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantId = tenantResolver.resolve(request);

            if (tenantId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tenant cannot be null");
                return;
            }

            MDC.put("tenant", tenantId);
            TenantContext.setTenant(tenantId);

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            MDC.remove("tenant");
        }
    }
}