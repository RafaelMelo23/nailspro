package com.rafael.nailspro.webapp.shared.tenant;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafael.nailspro.webapp.infrastructure.security.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.context.TenantIdentifierMismatchException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeaderOrSubdomainTenantResolver implements TenantResolver {

    private final TokenService tokenService;

    @Override
    public String resolve(HttpServletRequest request) {
        DecodedJWT token = tokenService.recoverAndValidate(request);

        String tokenTenant = (token != null)
                ? token.getClaim("tenantId").asString()
                : null;

        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) host = request.getHeader("Host");
        if (host == null) host = request.getServerName();

        String subdomainTenant = host.split("\\.")[0];

        if (tokenTenant != null && !subdomainTenant.equalsIgnoreCase(tokenTenant)) {
            throw new TenantIdentifierMismatchException("Tenant mismatch");
        }

        return (tokenTenant != null) ? tokenTenant : subdomainTenant;
    }
}