package com.lms.www.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_Id")
    private Long auditId;

    private String action;
    
    @Column(name = "entity_name")
    private String entityName;
    
    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "ip_Address")
    private String ipAddress;
}


