package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.www.service.PlatformTenantService;

@RestController
@RequestMapping("/platform/tenants")
public class PlatformTenantController {

    private final PlatformTenantService platformTenantService;

    public PlatformTenantController(
            PlatformTenantService platformTenantService
    ) {
        this.platformTenantService = platformTenantService;
    }

    @PostMapping("/{tenantDomain}/disable")
    public ResponseEntity<String> disableTenant(
            @PathVariable String tenantDomain
    ) {
        platformTenantService.disableTenantByDomain(tenantDomain);
        return ResponseEntity.ok("Tenant disabled successfully");
    }
}
