package com.lms.www.tenant;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class TenantDataSourceConfig {

    @Value("${aws.rds.endpoint}")
    private String endpoint;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    // 🔑 THIS MAP MUST BE SHARED
    private final Map<Object, Object> dataSources = new HashMap<>();

    private TenantRoutingDataSource routingDataSource;

    @Bean
    public DataSource dataSource() {

        routingDataSource = new TenantRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    public DataSource masterDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + endpoint + "/master_db");
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    public DataSource tenantDataSource(String dbName) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://" + endpoint + "/" + dbName);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    // 🔥 THIS IS THE ONLY NEW METHOD
    public void registerTenant(String dbName) {
        dataSources.put(dbName, tenantDataSource(dbName));
        routingDataSource.afterPropertiesSet();
    }
}
