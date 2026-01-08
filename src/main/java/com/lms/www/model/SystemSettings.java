package com.lms.www.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "setting_key")
    private Long settingKey;

    @Column(name = "max_login_attempts")
    private Long maxLoginAttempts;

    @Column(name = "acc_lock_duration")
    private Long accLockDuration; // minutes

    @Column(name = "pass_expiry_days")
    private Long passExpiryDays;

    @Column(name = "pass_length")
    private Long passLength;
    
    @Column(name = "enable_audit_log")
    private Boolean enableAuditLog;

}
