package com.lms.www.settings.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.settings.model.CustomUserField;
import com.lms.www.settings.model.GeneralSettings;
import com.lms.www.settings.model.PlatformSettings;
import com.lms.www.settings.model.TenantCommunicationSettings;
import com.lms.www.settings.model.TenantSecuritySettings;
import com.lms.www.settings.repository.CustomUserFieldRepository;
import com.lms.www.settings.repository.GeneralSettingsRepository;
import com.lms.www.settings.repository.PlatformSettingsRepository;
import com.lms.www.settings.repository.TenantCommunicationSettingsRepository;
import com.lms.www.settings.repository.TenantSecuritySettingsRepository;
import com.lms.www.settings.service.TenantSettingsService;

@Service
public class TenantSettingsServiceImpl implements TenantSettingsService {

    private final PlatformSettingsRepository platformSettingsRepository;
    private final TenantSecuritySettingsRepository tenantSecuritySettingsRepository;
    private final TenantCommunicationSettingsRepository tenantCommunicationSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomUserFieldRepository customUserFieldRepository;

    public TenantSettingsServiceImpl(
            PlatformSettingsRepository platformSettingsRepository,
            TenantSecuritySettingsRepository tenantSecuritySettingsRepository,
            TenantCommunicationSettingsRepository tenantCommunicationSettingsRepository,
            GeneralSettingsRepository generalSettingsRepository,
            CustomUserFieldRepository customUserFieldRepository
    ) {
        this.platformSettingsRepository = platformSettingsRepository;
        this.tenantSecuritySettingsRepository = tenantSecuritySettingsRepository;
        this.tenantCommunicationSettingsRepository = tenantCommunicationSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customUserFieldRepository = customUserFieldRepository;
    }

    @Override
    @Transactional
    public void ensureDefaultSettings() {

        if (platformSettingsRepository.findAll().isEmpty()) {
            PlatformSettings platform = new PlatformSettings();
            platform.setCustomDomain("");
            platform.setCloudflare(false);
            platform.setCurrency("USD ($)");
            platform.setTaxType("None");
            platform.setForeignPricing(false);
            platform.setTaxId("");
            platform.setBankAccount("");
            platform.setIfsc("");
            platform.setEnableInvoices(true);
            platform.setLegalName("");
            platform.setTaxIdLabel("GSTIN");
            platform.setAddress("");
            platform.setPrefix("INV-");
            platform.setSerial(1001L);
            platform.setFooterNote("");
            platformSettingsRepository.save(platform);
        }

        if (tenantSecuritySettingsRepository.findAll().isEmpty()) {
            TenantSecuritySettings security = new TenantSecuritySettings();
            security.setMaxDevices(2L);
            security.setWatermarking(true);
            security.setShowEmail(true);
            security.setShowPhone(true);
            security.setShowIp(false);
            security.setAdmin2fa(false);
            security.setGoogleLogin(true);
            security.setPasswordPolicy("Standard");
            security.setDoubleOptIn(false);
            tenantSecuritySettingsRepository.save(security);
        }

        if (tenantCommunicationSettingsRepository.findAll().isEmpty()) {
            TenantCommunicationSettings communication = new TenantCommunicationSettings();
            communication.setVerification(true);
            communication.setCommunication(true);
            communication.setWelcomeMessage("");
            communication.setSenderName("LMS Academy Team");
            communication.setReplyTo("");
            tenantCommunicationSettingsRepository.save(communication);
        }

        if (generalSettingsRepository.findAll().isEmpty()) {
            GeneralSettings general = new GeneralSettings();
            general.setLogo("");
            general.setSiteName("");
            general.setLanguage("en");
            general.setTimezone("UTC");
            generalSettingsRepository.save(general);
        }

        if (customUserFieldRepository.findAll().isEmpty()) {
            CustomUserField field = new CustomUserField();
            field.setFieldKey("mobile");
            field.setLabel("Mobile Number");
            field.setType("Mandatory");
            field.setIcon("smartphone");
            field.setDisplayOrder(1L);
            customUserFieldRepository.save(field);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> getAllSettings() {
        ensureDefaultSettings();

        PlatformSettings platform = platformSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Platform settings not found"));

        TenantSecuritySettings security = tenantSecuritySettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Tenant security settings not found"));

        TenantCommunicationSettings communication = tenantCommunicationSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Tenant communication settings not found"));

        GeneralSettings general = generalSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("General settings not found"));

        List<CustomUserField> customFields = customUserFieldRepository.findAllByOrderByDisplayOrderAsc();

        Map<String, Object> response = new HashMap<>();

        Map<String, Object> generalSettings = new HashMap<>();
        generalSettings.put("logo", general.getLogo());
        generalSettings.put("siteName", general.getSiteName());
        generalSettings.put("language", general.getLanguage());
        generalSettings.put("timezone", general.getTimezone());

        Map<String, Object> domainSettings = new HashMap<>();
        domainSettings.put("customDomain", platform.getCustomDomain());
        domainSettings.put("cloudflare", platform.getCloudflare());

        Map<String, Object> paymentSettings = new HashMap<>();
        paymentSettings.put("currency", platform.getCurrency());
        paymentSettings.put("taxType", platform.getTaxType());
        paymentSettings.put("foreignPricing", platform.getForeignPricing());
        paymentSettings.put("taxId", platform.getTaxId());
        paymentSettings.put("bankAccount", platform.getBankAccount());
        paymentSettings.put("ifsc", platform.getIfsc());

        Map<String, Object> taxSettings = new HashMap<>();
        taxSettings.put("enableInvoices", platform.getEnableInvoices());
        taxSettings.put("legalName", platform.getLegalName());
        taxSettings.put("taxIdLabel", platform.getTaxIdLabel());
        taxSettings.put("address", platform.getAddress());
        taxSettings.put("prefix", platform.getPrefix());
        taxSettings.put("serial", platform.getSerial());
        taxSettings.put("footerNote", platform.getFooterNote());

        Map<String, Object> securitySettings = new HashMap<>();
        securitySettings.put("maxDevices", security.getMaxDevices());
        securitySettings.put("watermarking", security.getWatermarking());
        securitySettings.put("showEmail", security.getShowEmail());
        securitySettings.put("showPhone", security.getShowPhone());
        securitySettings.put("showIp", security.getShowIp());
        securitySettings.put("admin2fa", security.getAdmin2fa());

        Map<String, Object> authSettings = new HashMap<>();
        authSettings.put("googleLogin", security.getGoogleLogin());
        authSettings.put("passwordPolicy", security.getPasswordPolicy());
        authSettings.put("doubleOptIn", security.getDoubleOptIn());

        Map<String, Object> uxSettings = new HashMap<>();
        uxSettings.put("verification", communication.getVerification());
        uxSettings.put("communication", communication.getCommunication());
        uxSettings.put("welcomeMessage", communication.getWelcomeMessage());

        Map<String, Object> commSettings = new HashMap<>();
        commSettings.put("senderName", communication.getSenderName());
        commSettings.put("replyTo", communication.getReplyTo());

        List<Map<String, Object>> fieldList = new ArrayList<>();
        for (CustomUserField field : customFields) {
            Map<String, Object> fieldMap = new HashMap<>();
            fieldMap.put("id", field.getFieldKey());
            fieldMap.put("label", field.getLabel());
            fieldMap.put("type", field.getType());
            fieldMap.put("icon", field.getIcon());
            fieldList.add(fieldMap);
        }

        response.put("generalSettings", generalSettings);
        response.put("domainSettings", domainSettings);
        response.put("paymentSettings", paymentSettings);
        response.put("taxSettings", taxSettings);
        response.put("securitySettings", securitySettings);
        response.put("authSettings", authSettings);
        response.put("uxSettings", uxSettings);
        response.put("commSettings", commSettings);
        response.put("customFields", fieldList);

        return response;
    }

    @Override
    @Transactional
    public void updatePlatformSettings(Map<String, Object> request) {
        ensureDefaultSettings();

        PlatformSettings platform = platformSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Platform settings not found"));

        if (request.containsKey("customDomain")) {
            platform.setCustomDomain((String) request.get("customDomain"));
        }
        if (request.containsKey("cloudflare")) {
            platform.setCloudflare(Boolean.valueOf(request.get("cloudflare").toString()));
        }
        if (request.containsKey("currency")) {
            platform.setCurrency((String) request.get("currency"));
        }
        if (request.containsKey("taxType")) {
            platform.setTaxType((String) request.get("taxType"));
        }
        if (request.containsKey("foreignPricing")) {
            platform.setForeignPricing(Boolean.valueOf(request.get("foreignPricing").toString()));
        }
        if (request.containsKey("taxId")) {
            platform.setTaxId((String) request.get("taxId"));
        }
        if (request.containsKey("bankAccount")) {
            platform.setBankAccount((String) request.get("bankAccount"));
        }
        if (request.containsKey("ifsc")) {
            platform.setIfsc((String) request.get("ifsc"));
        }
        if (request.containsKey("enableInvoices")) {
            platform.setEnableInvoices(Boolean.valueOf(request.get("enableInvoices").toString()));
        }
        if (request.containsKey("legalName")) {
            platform.setLegalName((String) request.get("legalName"));
        }
        if (request.containsKey("taxIdLabel")) {
            platform.setTaxIdLabel((String) request.get("taxIdLabel"));
        }
        if (request.containsKey("address")) {
            platform.setAddress((String) request.get("address"));
        }
        if (request.containsKey("prefix")) {
            platform.setPrefix((String) request.get("prefix"));
        }
        if (request.containsKey("serial")) {
            platform.setSerial(Long.valueOf(request.get("serial").toString()));
        }
        if (request.containsKey("footerNote")) {
            platform.setFooterNote((String) request.get("footerNote"));
        }

        platformSettingsRepository.save(platform);
    }

    @Override
    @Transactional
    public void updateSecuritySettings(Map<String, Object> request) {
        ensureDefaultSettings();

        TenantSecuritySettings security = tenantSecuritySettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Tenant security settings not found"));

        if (request.containsKey("maxDevices")) {
            security.setMaxDevices(Long.valueOf(request.get("maxDevices").toString()));
        }
        if (request.containsKey("watermarking")) {
            security.setWatermarking(Boolean.valueOf(request.get("watermarking").toString()));
        }
        if (request.containsKey("showEmail")) {
            security.setShowEmail(Boolean.valueOf(request.get("showEmail").toString()));
        }
        if (request.containsKey("showPhone")) {
            security.setShowPhone(Boolean.valueOf(request.get("showPhone").toString()));
        }
        if (request.containsKey("showIp")) {
            security.setShowIp(Boolean.valueOf(request.get("showIp").toString()));
        }
        if (request.containsKey("admin2fa")) {
            security.setAdmin2fa(Boolean.valueOf(request.get("admin2fa").toString()));
        }
        if (request.containsKey("googleLogin")) {
            security.setGoogleLogin(Boolean.valueOf(request.get("googleLogin").toString()));
        }
        if (request.containsKey("passwordPolicy")) {
            security.setPasswordPolicy((String) request.get("passwordPolicy"));
        }
        if (request.containsKey("doubleOptIn")) {
            security.setDoubleOptIn(Boolean.valueOf(request.get("doubleOptIn").toString()));
        }

        tenantSecuritySettingsRepository.save(security);
    }

    @Override
    @Transactional
    public void updateCommunicationSettings(Map<String, Object> request) {
        ensureDefaultSettings();

        TenantCommunicationSettings communication = tenantCommunicationSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Tenant communication settings not found"));

        if (request.containsKey("verification")) {
            communication.setVerification(Boolean.valueOf(request.get("verification").toString()));
        }
        if (request.containsKey("communication")) {
            communication.setCommunication(Boolean.valueOf(request.get("communication").toString()));
        }
        if (request.containsKey("welcomeMessage")) {
            communication.setWelcomeMessage((String) request.get("welcomeMessage"));
        }
        if (request.containsKey("senderName")) {
            communication.setSenderName((String) request.get("senderName"));
        }
        if (request.containsKey("replyTo")) {
            communication.setReplyTo((String) request.get("replyTo"));
        }

        tenantCommunicationSettingsRepository.save(communication);
    }

    @Override
    @Transactional
    public void updateGeneralSettings(Map<String, Object> request) {
        ensureDefaultSettings();

        GeneralSettings general = generalSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("General settings not found"));

        if (request.containsKey("logo")) {
            general.setLogo((String) request.get("logo"));
        }
        if (request.containsKey("siteName")) {
            general.setSiteName((String) request.get("siteName"));
        }
        if (request.containsKey("language")) {
            general.setLanguage((String) request.get("language"));
        }
        if (request.containsKey("timezone")) {
            general.setTimezone((String) request.get("timezone"));
        }

        generalSettingsRepository.save(general);
    }

    @Override
    @Transactional
    public void replaceCustomFields(List<Map<String, Object>> customFields) {
        ensureDefaultSettings();

        customUserFieldRepository.deleteAll();

        long order = 1;
        for (Map<String, Object> fieldMap : customFields) {
            CustomUserField field = new CustomUserField();
            field.setFieldKey(fieldMap.get("id") == null ? null : fieldMap.get("id").toString());
            field.setLabel(fieldMap.get("label") == null ? null : fieldMap.get("label").toString());
            field.setType(fieldMap.get("type") == null ? null : fieldMap.get("type").toString());
            field.setIcon(fieldMap.get("icon") == null ? null : fieldMap.get("icon").toString());
            field.setDisplayOrder(order++);
            customUserFieldRepository.save(field);
        }
    }
}