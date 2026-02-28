package com.lms.www.website.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_custom_page_sections")
@Getter
@Setter
public class TenantCustomPageSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_custom_page_section_id")
    private Long tenantCustomPageSectionId;

    @ManyToOne
    @JoinColumn(name = "tenant_custom_page_id", nullable = false)
    private TenantCustomPage tenantCustomPage;

    @Column(name = "section_type", nullable = false)
    private String sectionType;

    @Column(name = "section_config", columnDefinition = "JSON")
    private String sectionConfig;
    
    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}