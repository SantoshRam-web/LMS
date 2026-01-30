package com.lms.www.tenant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TenantResolver {

    private final JdbcTemplate jdbcTemplate;

    public TenantResolver(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String resolveTenantDb(String email) {
        return jdbcTemplate.queryForObject(
            "SELECT tenant_db_name FROM tenant_registry WHERE super_admin_email = ?",
            String.class,
            email
        );
    }
}
