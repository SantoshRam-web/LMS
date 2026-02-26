package com.lms.www.website.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenant_pages")
@Getter
@Setter
public class TenantPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tenant_page_id")
    private Long tenantPageId;

    @ManyToOne
    @JoinColumn(name = "tenant_theme_id")
    private TenantTheme tenantTheme;

    @Column(name = "page_key", nullable = false)
    private String pageKey;
    
    @Column(name = "slug", nullable = false)
    private String slug;
    
    @Column(name = "custom_title")
    private String customTitle;
    
    @Column(name = "is_published")
    private Boolean isPublished = false;
    
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;
    
    public void touch() {
        this.lastModifiedAt = LocalDateTime.now();
    }
}
