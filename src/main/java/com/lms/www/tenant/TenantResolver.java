package com.lms.www.tenant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TenantResolver {

    private final JdbcTemplate jdbcTemplate;

    public TenantResolver(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String resolveTenantDomain(String tenantDb) {
        return jdbcTemplate.queryForObject(
            "SELECT tenant_domain FROM tenant_registry WHERE tenant_db_name = ?",
            String.class,
            tenantDb
        );
    }
    
    public String buildTenantLoginUrl(String tenantDomain) {
        return "http://" + tenantDomain + ".yourdomain.com:9090";
    }

}
