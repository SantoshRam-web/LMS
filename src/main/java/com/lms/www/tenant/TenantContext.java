package com.lms.www.tenant;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_DB = new ThreadLocal<>();

    public static void setTenant(String dbName) {
        CURRENT_DB.set(dbName);
    }

    public static String getTenant() {
        return CURRENT_DB.get();
    }

    public static void clear() {
        CURRENT_DB.remove();
    }
}
