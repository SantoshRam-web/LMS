package com.lms.www.website.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenant_headers")
@Getter
@Setter
public class TenantHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_header_id")
    private Long tenantHeaderId;

    @Column(name = "header_config", columnDefinition = "JSON", nullable = false)
    private String headerConfig;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}