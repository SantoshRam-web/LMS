package com.lms.www.settings.service;

import java.util.List;
import java.util.Map;

public interface TenantSettingsService {

    Map<String, Object> getAllSettings();

    void updatePlatformSettings(Map<String, Object> request);

    void updateSecuritySettings(Map<String, Object> request);

    void updateCommunicationSettings(Map<String, Object> request);

    void updateGeneralSettings(Map<String, Object> request);

    void replaceCustomFields(List<Map<String, Object>> customFields);

    void ensureDefaultSettings();
}