package com.lms.www.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Primary
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger log =
            LoggerFactory.getLogger(TenantRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String tenant = TenantContext.getTenant();

        if (tenant == null) {
            return "MASTER";
        }
        return tenant;
    }

}
