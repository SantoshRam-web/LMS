package com.lms.www.service.Impl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.model.User;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.PlatformTenantService;
import com.lms.www.tenant.TenantContext;
import com.lms.www.tenant.TenantRoutingDataSource;

@Service
@Transactional
public class PlatformTenantServiceImpl implements PlatformTenantService {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final DataSource tenantRoutingDataSource;

    public PlatformTenantServiceImpl(
            JdbcTemplate jdbcTemplate,
            UserRepository userRepository,
            DataSource tenantRoutingDataSource
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.tenantRoutingDataSource = tenantRoutingDataSource;
    }

    @Override
    public void disableTenantByDomain(String tenantDomain) {

        // ================================
        // 1️⃣ DISABLE TENANT (MASTER DB)
        // ================================
        String tenantDb = jdbcTemplate.queryForObject(
                "SELECT tenant_db_name FROM tenant_registry WHERE tenant_domain = ?",
                String.class,
                tenantDomain.toLowerCase()
        );

        jdbcTemplate.update(
                "UPDATE tenant_registry SET enabled = false WHERE tenant_domain = ?",
                tenantDomain.toLowerCase()
        );

        // ================================
        // 2️⃣ REGISTER TENANT DATASOURCE
        // ================================
        TenantRoutingDataSource routing =
                (TenantRoutingDataSource)
                        ((LazyConnectionDataSourceProxy) tenantRoutingDataSource)
                                .getTargetDataSource();

        routing.addTenant(tenantDb);

        // ================================
        // 3️⃣ SWITCH TO TENANT DB
        // ================================
        TenantContext.setTenant(tenantDb);

        try {
            // ================================
            // 4️⃣ DISABLE SUPER ADMIN USER
            // ================================
            User superAdmin = userRepository
                    .findByRoleName("ROLE_SUPER_ADMIN")
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (superAdmin != null && Boolean.TRUE.equals(superAdmin.getEnabled())) {
                superAdmin.setEnabled(false);
                userRepository.save(superAdmin);
            }

        } finally {
            // ================================
            // 5️⃣ ALWAYS CLEAR CONTEXT
            // ================================
            TenantContext.clear();
        }
    }
}
