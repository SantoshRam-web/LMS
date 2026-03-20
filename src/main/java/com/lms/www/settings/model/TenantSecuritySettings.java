package com.lms.www.settings.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenant_security_settings")
@Getter
@Setter
public class TenantSecuritySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_security_setting_id")
    private Long tenantSecuritySettingId;

    @Column(name = "max_devices")
    private Long maxDevices;

    @Column(name = "watermarking")
    private Boolean watermarking;

    @Column(name = "show_email")
    private Boolean showEmail;

    @Column(name = "show_phone")
    private Boolean showPhone;

    @Column(name = "show_ip")
    private Boolean showIp;

    @Column(name = "admin_2fa")
    private Boolean admin2fa;

    @Column(name = "google_login")
    private Boolean googleLogin;

    @Column(name = "password_policy")
    private String passwordPolicy;

    @Column(name = "double_opt_in")
    private Boolean doubleOptIn;
}