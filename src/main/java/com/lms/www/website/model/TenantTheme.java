package com.lms.www.website.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tenant_themes")
@Getter
@Setter
public class TenantTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_theme_id")
    private Long tenantThemeId;

    @Column(name = "theme_template_id",nullable = false)
    private Long themeTemplateId; // reference to master_db theme

    @Column(name = "status", nullable = false)
    private String status; // DRAFT or LIVE

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "header_config", columnDefinition = "JSON")
    private String headerConfig;
    
    @Column(name = "footer_config", columnDefinition = "JSON")
    private String footerConfig;
}
