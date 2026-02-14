package com.lms.www.website.model;

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
@Table(name = "tenant_sections")
@Getter
@Setter
public class TenantSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tenant_section_id")
    private Long tenantSectionId;

    @ManyToOne
    @JoinColumn(name = "tenant_page_id")
    private TenantPage tenantPage;

    @Column(name="section_type",nullable = false)
    private String sectionType;

    @Column(name= "section_config", columnDefinition = "JSON")
    private String sectionConfig;

    @Column(name="display_order")
    private Integer displayOrder;
    
    @Column(name = "template_section_id")
    private Long templateSectionId;
}
