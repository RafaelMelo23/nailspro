package com.rafael.nailspro.webapp.shared.tenant;

public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void clear() {
        currentTenant.remove();
    }

    public static String getTenant() {
        return currentTenant.get();
    }

    public static void setTenant(String tenant) {
        currentTenant.set(tenant);
    }
}
