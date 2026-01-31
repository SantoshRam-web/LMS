package com.lms.www.tenant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.zaxxer.hikari.HikariDataSource;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> dataSources = new ConcurrentHashMap<>();

    public void addTenant(String tenantDb) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + tenantDb);
        ds.setUsername("root");
        ds.setPassword("password");

        dataSources.put(tenantDb, ds);
        setTargetDataSources(dataSources);
        afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenant();
    }
}
