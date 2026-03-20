package com.lms.www.settings.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "platform_settings")
@Getter
@Setter
public class PlatformSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_setting_id")
    private Long platformSettingId;

    @Column(name = "custom_domain")
    private String customDomain;

    @Column(name = "cloudflare")
    private Boolean cloudflare;

    @Column(name = "currency")
    private String currency;

    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "foreign_pricing")
    private Boolean foreignPricing;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "ifsc")
    private String ifsc;

    @Column(name = "enable_invoices")
    private Boolean enableInvoices;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "tax_id_label")
    private String taxIdLabel;

    @Column(name = "address")
    private String address;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "serial")
    private Long serial;

    @Column(name = "footer_note")
    private String footerNote;
}