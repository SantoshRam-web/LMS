package com.lms.www.website.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenant_settings")
@Getter
@Setter
public class TenantSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "favicon_path")
    private String faviconPath;

    @Column(name = "footfall_enabled")
    private Boolean footfallEnabled = false;

    @Column(name = "store_view_type")
    private String storeViewType;

    @Column(name = "store_config", columnDefinition = "JSON")
    private String storeConfig;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
