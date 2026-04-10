package com.rafael.agendanails.webapp.infrastructure.security.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafael.agendanails.webapp.infrastructure.exception.StandardError;
import com.rafael.agendanails.webapp.shared.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoReadOnlyInterceptor implements HandlerInterceptor {

    @Value("${demo.tenant}")
    private String demoTenant;
    private ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentTenant = TenantContext.getTenant();

        if (!currentTenant.equalsIgnoreCase(demoTenant)) return true;

        if (HttpMethod.DELETE.name().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonError = buildCustomErrorJson(request);
            response.getWriter().write(jsonError);

            return false;
        }
        return true;
    }

    private String buildCustomErrorJson(HttpServletRequest request) throws JsonProcessingException {
        StandardError err = new StandardError(
                Instant.now(),
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden",
                List.of("Exclusões não são permitidas no ambiente de demonstração."),
                request.getRequestURI()
        );

        return mapper.writeValueAsString(err);
    }
}
