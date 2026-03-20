package com.lms.www.settings.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.lms.www.settings.service.TenantSettingsService;

@RestController
@RequestMapping("/api/v1/settings")
public class TenantSettingsController {

    private final TenantSettingsService tenantSettingsService;

    public TenantSettingsController(TenantSettingsService tenantSettingsService) {
        this.tenantSettingsService = tenantSettingsService;
    }

    @GetMapping
    public Map<String, Object> getAllSettings() {
        return tenantSettingsService.getAllSettings();
    }

    @PutMapping("/platform")
    public Map<String, String> updatePlatform(@RequestBody Map<String, Object> request) {
        tenantSettingsService.updatePlatformSettings(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Platform settings updated successfully");
        return response;
    }

    @PutMapping("/security")
    public Map<String, String> updateSecurity(@RequestBody Map<String, Object> request) {
        tenantSettingsService.updateSecuritySettings(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Security settings updated successfully");
        return response;
    }

    @PutMapping("/communication")
    public Map<String, String> updateCommunication(@RequestBody Map<String, Object> request) {
        tenantSettingsService.updateCommunicationSettings(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Communication settings updated successfully");
        return response;
    }

    @PutMapping("/general")
    public Map<String, String> updateGeneral(@RequestBody Map<String, Object> request) {
        tenantSettingsService.updateGeneralSettings(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "General settings updated successfully");
        return response;
    }

    @PutMapping("/custom-fields")
    public Map<String, String> replaceCustomFields(@RequestBody List<Map<String, Object>> request) {
        tenantSettingsService.replaceCustomFields(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Custom fields updated successfully");
        return response;
    }
}