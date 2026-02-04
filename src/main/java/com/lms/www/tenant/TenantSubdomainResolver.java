package com.lms.www.tenant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class TenantSubdomainResolver {

    private final JdbcTemplate jdbcTemplate;

    public TenantSubdomainResolver(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String resolveTenantDbFromRequest(HttpServletRequest request) {

        String host = request.getServerName(); // john.localhost

        if (!host.contains(".")) return null;

        String subdomain = host.split("\\.")[0];

        return jdbcTemplate.queryForObject(
            "SELECT tenant_db_name FROM tenant_registry WHERE tenant_domain = ?",
            String.class,
            subdomain
        );
    }
}
