package com.rafael.nailspro.webapp.shared.tenant;

import jakarta.servlet.http.HttpServletRequest;

public interface TenantResolver {

    String resolve(HttpServletRequest request);
}
