package com.lms.www.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class TenantDataSourceLoader {

    private final JdbcTemplate jdbcTemplate;
    private final TenantRoutingDataSource routingDataSource;
    private final DataSource masterDataSource;

    public TenantDataSourceLoader(
            JdbcTemplate jdbcTemplate,
            TenantRoutingDataSource routingDataSource,
            @Qualifier("masterDataSource") DataSource masterDataSource
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.routingDataSource = routingDataSource;
        this.masterDataSource = masterDataSource;
    }

    @PostConstruct
    public void loadTenantsOnStartup() {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("MASTER", masterDataSource);

        jdbcTemplate.query(
            "SELECT tenant_db_name FROM tenant_registry",
            rs -> {
                String tenantDb = rs.getString("tenant_db_name");

                HikariDataSource tenantDs = new HikariDataSource();
                tenantDs.setJdbcUrl(
                        ((HikariDataSource) masterDataSource)
                                .getJdbcUrl()
                                .replace("/master_db", "/" + tenantDb)
                );
                tenantDs.setUsername(
                        ((HikariDataSource) masterDataSource).getUsername()
                );
                tenantDs.setPassword(
                        ((HikariDataSource) masterDataSource).getPassword()
                );

                targetDataSources.put(tenantDb, tenantDs);
            }
        );

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();
    }
}
