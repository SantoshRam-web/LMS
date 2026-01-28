package com.lms.www.model;

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
@Table(name = "system_settings")
@Getter
@Setter
public class SystemSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_key")
    private Long settingKey;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    // ---------- SECURITY ----------
    @Column(name = "max_login_attempts", nullable = false)
    private Long maxLoginAttempts;

    @Column(name = "acc_lock_duration", nullable = false)
    private Long accLockDuration; // minutes

    @Column(name = "pass_expiry_days", nullable = false)
    private Long passExpiryDays;

    @Column(name = "pass_length", nullable = false)
    private Long passLength;

    @Column(name = "password_last_updated_at", nullable = false)
    private LocalDateTime passwordLastUpdatedAt;

    // ---------- SESSION ----------
    @Column(name = "jwt_expiry_mins")
    private Long jwtExpiryMins;

    @Column(name = "session_timeout")
    private Long sessionTimeout;

    @Column(name = "multi_session")
    private Boolean multiSession;

    // ---------- AUDIT FLAGS ----------
    @Column(name = "enable_login_audit")
    private Boolean enableLoginAudit;

    @Column(name = "enable_audit_log")
    private Boolean enableAuditLog;

    // ---------- META ----------
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
}

