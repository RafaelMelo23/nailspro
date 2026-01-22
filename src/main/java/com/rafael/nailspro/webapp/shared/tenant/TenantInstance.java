package com.rafael.nailspro.webapp.shared.tenant;

public record TenantInstance(String tenantId) {

    private static final String PREFIX = "tenant_";

    public String getFormattedName() {
        return PREFIX + tenantId;
    }

    public static String format(String tenantId) {

        return new TenantInstance(tenantId).getFormattedName();
    }

    public static String fromRawString(String tenantId) {

        return tenantId.replace(PREFIX, "");
    }
}
