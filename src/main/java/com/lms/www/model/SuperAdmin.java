package com.lms.www.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "super_admins")
@Getter
@Setter
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "super_admin_id")
    private Long superAdminId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;
    
    @Column
    private Boolean enabled = true;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
