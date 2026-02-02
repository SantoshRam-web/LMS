package com.lms.www.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TenantDataSourceConfig {

    @Bean
    @Primary
    public TenantRoutingDataSource tenantRoutingDataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource
    ) {
        TenantRoutingDataSource ds = new TenantRoutingDataSource();

        Map<Object, Object> map = new HashMap<>();
        map.put("master", masterDataSource);

        ds.setDefaultTargetDataSource(masterDataSource);
        ds.setTargetDataSources(map);
        ds.afterPropertiesSet();

        return ds;
    }
}

