package com.lms.www.tenant;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class TenantDataSourceConfig {

    @Bean
    public DataSource tenantRoutingDataSource(
            DataSource masterDataSource,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String pass
    ) {
        TenantRoutingDataSource routing = new TenantRoutingDataSource();
        routing.init(masterDataSource, url, user, pass);

        // 🔥 REQUIRED
        return new LazyConnectionDataSourceProxy(routing);
    }
}
