-- Tenant DB template (RBAC removed, user_permissions enabled)
-- SAFE FOR Spring ScriptUtils

SET FOREIGN_KEY_CHECKS = 0;

-- =====================
-- users
-- =====================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `enabled` TINYINT(1) DEFAULT 1,
  `first_name` VARCHAR(255) DEFAULT NULL,
  `last_name` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `role_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_users_email` (`email`),
  UNIQUE KEY `uk_users_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- user_permissions
-- =====================
DROP TABLE IF EXISTS `user_permissions`;
CREATE TABLE `user_permissions` (
  `user_permission_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `permission_name` VARCHAR(150) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_permission_id`),
  UNIQUE KEY `uk_user_permission` (`user_id`, `permission_name`),
  CONSTRAINT `fk_user_permissions_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- address
-- =====================
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `address_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `pin_code` BIGINT DEFAULT NULL,
  `district` VARCHAR(255) DEFAULT NULL,
  `mandal` VARCHAR(255) DEFAULT NULL,
  `city` VARCHAR(255) DEFAULT NULL,
  `village` VARCHAR(255) DEFAULT NULL,
  `d_no` BIGINT DEFAULT NULL,
  PRIMARY KEY (`address_id`),
  UNIQUE KEY `uk_address_user` (`user_id`),
  CONSTRAINT `fk_address_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- admin
-- =====================
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_admin_user` (`user_id`),
  CONSTRAINT `fk_admin_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- parents
-- =====================
DROP TABLE IF EXISTS `parents`;
CREATE TABLE `parents` (
  `parent_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`parent_id`),
  UNIQUE KEY `uk_parent_user` (`user_id`),
  CONSTRAINT `fk_parent_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- students
-- =====================
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `student_id` BIGINT NOT NULL AUTO_INCREMENT,
  `dob` DATE DEFAULT NULL,
  `gender` VARCHAR(255) DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `uk_student_user` (`user_id`),
  CONSTRAINT `fk_student_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- parent_student_relation
-- =====================
DROP TABLE IF EXISTS `parent_student_relation`;
CREATE TABLE `parent_student_relation` (
  `rel_id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NOT NULL,
  `student_id` BIGINT NOT NULL,
  PRIMARY KEY (`rel_id`),
  CONSTRAINT `fk_ps_parent`
    FOREIGN KEY (`parent_id`)
    REFERENCES `parents` (`parent_id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_ps_student`
    FOREIGN KEY (`student_id`)
    REFERENCES `students` (`student_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- instructor
-- =====================
DROP TABLE IF EXISTS `instructor`;
CREATE TABLE `instructor` (
  `instructor_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`instructor_id`),
  UNIQUE KEY `uk_instructor_user` (`user_id`),
  CONSTRAINT `fk_instructor_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- driver
-- =====================
DROP TABLE IF EXISTS `driver`;
CREATE TABLE `driver` (
  `driver_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `uk_driver_user` (`user_id`),
  CONSTRAINT `fk_driver_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- conductor
-- =====================
DROP TABLE IF EXISTS `conductor`;
CREATE TABLE `conductor` (
  `conductor_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`conductor_id`),
  UNIQUE KEY `uk_conductor_user` (`user_id`),
  CONSTRAINT `fk_conductor_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- accountant
-- =====================
DROP TABLE IF EXISTS `accountant`;
CREATE TABLE `accountant` (
  `accountant_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`accountant_id`),
  UNIQUE KEY `uk_accountant_user` (`user_id`),
  CONSTRAINT `fk_accountant_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- affiliate
-- =====================
DROP TABLE IF EXISTS `affiliate`;
CREATE TABLE `affiliate` (
  `affiliate_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`affiliate_id`),
  UNIQUE KEY `uk_affiliate_user` (`user_id`),
  CONSTRAINT `fk_affiliate_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- community_manager
-- =====================
DROP TABLE IF EXISTS `community_manager`;
CREATE TABLE `community_manager` (
  `community_manager_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`community_manager_id`),
  UNIQUE KEY `uk_community_manager_user` (`user_id`),
  CONSTRAINT `fk_community_manager_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- department_head
-- =====================
DROP TABLE IF EXISTS `department_head`;
CREATE TABLE `department_head` (
  `department_head_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`department_head_id`),
  UNIQUE KEY `uk_department_head_user` (`user_id`),
  CONSTRAINT `fk_department_head_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- evaluator
-- =====================
DROP TABLE IF EXISTS `evaluator`;
CREATE TABLE `evaluator` (
  `evaluator_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`evaluator_id`),
  UNIQUE KEY `uk_evaluator_user` (`user_id`),
  CONSTRAINT `fk_evaluator_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- inventory_manager
-- =====================
DROP TABLE IF EXISTS `inventory_manager`;
CREATE TABLE `inventory_manager` (
  `inventory_manager_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`inventory_manager_id`),
  UNIQUE KEY `uk_inventory_manager_user` (`user_id`),
  CONSTRAINT `fk_inventory_manager_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- librarian
-- =====================
DROP TABLE IF EXISTS `librarian`;
CREATE TABLE `librarian` (
  `librarian_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`librarian_id`),
  UNIQUE KEY `uk_librarian_user` (`user_id`),
  CONSTRAINT `fk_librarian_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- marketing_manager
-- =====================
DROP TABLE IF EXISTS `marketing_manager`;
CREATE TABLE `marketing_manager` (
  `marketing_manager_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`marketing_manager_id`),
  UNIQUE KEY `uk_marketing_manager_user` (`user_id`),
  CONSTRAINT `fk_marketing_manager_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- mentor
-- =====================
DROP TABLE IF EXISTS `mentor`;
CREATE TABLE `mentor` (
  `mentor_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`mentor_id`),
  UNIQUE KEY `uk_mentor_user` (`user_id`),
  CONSTRAINT `fk_mentor_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- transport_manager
-- =====================
DROP TABLE IF EXISTS `transport_manager`;
CREATE TABLE `transport_manager` (
  `transport_manager_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`transport_manager_id`),
  UNIQUE KEY `uk_transport_manager_user` (`user_id`),
  CONSTRAINT `fk_transport_manager_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- warden
-- =====================
DROP TABLE IF EXISTS `warden`;
CREATE TABLE `warden` (
  `warden_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`warden_id`),
  UNIQUE KEY `uk_warden_user` (`user_id`),
  CONSTRAINT `fk_warden_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- audit_logs
-- =====================
DROP TABLE IF EXISTS `audit_logs`;
CREATE TABLE `audit_logs` (
  `audit_id` BIGINT NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(255) DEFAULT NULL,
  `entity_name` VARCHAR(255) DEFAULT NULL,
  `entity_id` BIGINT DEFAULT NULL,
  `user_id` BIGINT DEFAULT NULL,
  `ip_address` VARCHAR(255) DEFAULT NULL,
  `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_id`),
  CONSTRAINT `fk_audit_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- login_history
-- =====================
DROP TABLE IF EXISTS `login_history`;
CREATE TABLE `login_history` (
  `login_id` BIGINT NOT NULL AUTO_INCREMENT,
  `device` VARCHAR(255) DEFAULT NULL,
  `ip_address` VARCHAR(255) DEFAULT NULL,
  `login_time` DATETIME DEFAULT NULL,
  `user_agent` VARCHAR(255) DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`login_id`),
  CONSTRAINT `fk_login_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- system_settings
-- =====================
DROP TABLE IF EXISTS `system_settings`;
CREATE TABLE `system_settings` (
  `setting_key` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `max_login_attempts` BIGINT NOT NULL,
  `acc_lock_duration` BIGINT NOT NULL,
  `pass_expiry_days` BIGINT NOT NULL,
  `pass_length` BIGINT NOT NULL,
  `jwt_expiry_mins` BIGINT DEFAULT NULL,
  `session_timeout` BIGINT DEFAULT NULL,
  `multi_session` TINYINT(1) DEFAULT 0,
  `enable_login_audit` TINYINT(1) DEFAULT 1,
  `enable_audit_log` TINYINT(1) DEFAULT 1,
  `password_last_updated_at` TIMESTAMP NULL DEFAULT NULL,
  `updated_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`setting_key`),
  UNIQUE KEY `uk_system_user` (`user_id`),
  CONSTRAINT `fk_system_settings_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- password_reset_tokens
-- =====================
DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE `password_reset_tokens` (
  `token_id` BIGINT NOT NULL AUTO_INCREMENT,
  `reset_token` VARCHAR(255) NOT NULL,
  `user_id` BIGINT NOT NULL,
  `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token_id`),
  CONSTRAINT `fk_reset_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- failed_login_attempts
-- =====================
DROP TABLE IF EXISTS `failed_login_attempts`;
CREATE TABLE `failed_login_attempts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `attempt_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `ip_address` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_failed_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- user_sessions
-- =====================
DROP TABLE IF EXISTS `user_sessions`;
CREATE TABLE `user_sessions` (
  `session_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `token` LONGTEXT,
  `login_time` TIMESTAMP NULL,
  `logout_time` TIMESTAMP NULL,
  `last_activity_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`session_id`),
  CONSTRAINT `fk_session_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- otp_verification
-- =====================
DROP TABLE IF EXISTS `otp_verification`;
CREATE TABLE `otp_verification` (
  `otp_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `otp` VARCHAR(10) NOT NULL,
  `purpose` VARCHAR(50) NOT NULL,
  `expires_at` DATETIME NOT NULL,
  `attempts` INT DEFAULT 0,
  `max_attempts` INT DEFAULT 3,
  `verified` TINYINT(1) DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`otp_id`),
  UNIQUE KEY `uq_active_otp` (`email`, `purpose`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tenant_themes`;
CREATE TABLE `tenant_themes` (
 `tenant_theme_id` BIGINT NOT NULL AUTO_INCREMENT,
 `theme_template_id` BIGINT NOT NULL,
 `status` VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 `header_config` JSON NULL,
 `default_header_config` JSON NULL,
 `footer_config` JSON NULL,
 `seo_config` JSON NULL,
 `robots_txt` TEXT,
 `sitemap_path` VARCHAR(500),
 PRIMARY KEY (`tenant_theme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tenant_pages`;
CREATE TABLE tenant_pages (
  tenant_page_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tenant_theme_id BIGINT NOT NULL,
  page_key VARCHAR(50) NOT NULL,
  slug VARCHAR(150) NOT NULL,
  custom_title VARCHAR(255),
  is_published BOOLEAN DEFAULT 0,
  last_modified_at TIMESTAMP NULL,
  FOREIGN KEY (tenant_theme_id) REFERENCES tenant_themes(tenant_theme_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `tenant_sections`;
CREATE TABLE tenant_sections (
    tenant_section_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_page_id BIGINT NOT NULL,
    template_section_id BIGINT NOT NULL,
    section_type VARCHAR(50) NOT NULL,
    section_config JSON,
    display_order INT,
    FOREIGN KEY (tenant_page_id)
        REFERENCES tenant_pages(tenant_page_id)
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS `tenant_settings`;
CREATE TABLE tenant_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    site_name VARCHAR(255),
    logo_path VARCHAR(500),
    favicon_path VARCHAR(500),
    footfall_enabled BOOLEAN DEFAULT FALSE,
    store_view_type VARCHAR(50),
    store_config JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tenant_headers (
    tenant_header_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    header_config JSON NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tenant_custom_pages (
    tenant_custom_page_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    meta_title VARCHAR(255),
    meta_description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tenant_custom_page_sections (
    tenant_custom_page_section_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_custom_page_id BIGINT NOT NULL,
    section_type VARCHAR(100) NOT NULL,
    section_config JSON,
    display_order INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_custom_page
        FOREIGN KEY (tenant_custom_page_id)
        REFERENCES tenant_custom_pages(tenant_custom_page_id)
        ON DELETE CASCADE
);

CREATE TABLE community_spaces (
space_id BIGINT AUTO_INCREMENT PRIMARY KEY,
space_name VARCHAR(255) NOT NULL,
course_id BIGINT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NULL
);

CREATE TABLE community_channels (
channel_id BIGINT AUTO_INCREMENT PRIMARY KEY,
space_id BIGINT NOT NULL,
channel_name VARCHAR(255) NOT NULL,
channel_type VARCHAR(50),
description TEXT,
admins_only BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_threads (
thread_id BIGINT AUTO_INCREMENT PRIMARY KEY,
channel_id BIGINT NOT NULL,
title VARCHAR(255),
content TEXT,
author_id BIGINT,
author_name VARCHAR(255),
author_role VARCHAR(50),
status VARCHAR(20) DEFAULT 'OPEN',
is_pinned BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_replies (
reply_id BIGINT AUTO_INCREMENT PRIMARY KEY,
thread_id BIGINT,
parent_reply_id BIGINT,
content TEXT,
author_id BIGINT,
author_name VARCHAR(255),
author_role VARCHAR(50),
is_verified BOOLEAN DEFAULT FALSE,
is_answer BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_reactions (
reaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
thread_id BIGINT,
reply_id BIGINT,
user_id BIGINT,
reaction_type VARCHAR(50),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_bookmarks (
bookmark_id BIGINT AUTO_INCREMENT PRIMARY KEY,
thread_id BIGINT,
user_id BIGINT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_mentions (
mention_id BIGINT AUTO_INCREMENT PRIMARY KEY,
thread_id BIGINT,
reply_id BIGINT,
mentioned_user_id BIGINT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_notifications (
notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
user_id BIGINT,
thread_id BIGINT,
reply_id BIGINT,
type VARCHAR(50),
is_read BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_reports (
report_id BIGINT AUTO_INCREMENT PRIMARY KEY,
thread_id BIGINT,
reply_id BIGINT,
reported_by BIGINT,
reason TEXT,
status VARCHAR(20) DEFAULT 'OPEN',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE community_channel_members (
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    channel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    joined_at DATETIME DEFAULT NULL,
    PRIMARY KEY (member_id),
    UNIQUE KEY unique_member (channel_id, user_id),
    KEY idx_channel_id (channel_id),
    KEY idx_user_id (user_id)
);

CREATE TABLE platform_settings (
    platform_setting_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    custom_domain VARCHAR(255),
    cloudflare TINYINT(1) DEFAULT 0,
    currency VARCHAR(50) NOT NULL DEFAULT 'USD ($)',
    tax_type VARCHAR(50) NOT NULL DEFAULT 'None',
    foreign_pricing TINYINT(1) DEFAULT 0,
    tax_id VARCHAR(100),
    bank_account VARCHAR(100),
    ifsc VARCHAR(100),
    enable_invoices TINYINT(1) DEFAULT 1,
    legal_name VARCHAR(255),
    tax_id_label VARCHAR(100) DEFAULT 'GSTIN',
    address TEXT,
    prefix VARCHAR(50) DEFAULT 'INV-',
    serial BIGINT DEFAULT 1001,
    footer_note TEXT
);

CREATE TABLE tenant_security_settings (
    tenant_security_setting_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    max_devices BIGINT NOT NULL DEFAULT 2,
    watermarking TINYINT(1) DEFAULT 1,
    show_email TINYINT(1) DEFAULT 1,
    show_phone TINYINT(1) DEFAULT 1,
    show_ip TINYINT(1) DEFAULT 0,
    admin_2fa TINYINT(1) DEFAULT 0,
    google_login TINYINT(1) DEFAULT 1,
    password_policy VARCHAR(100) NOT NULL DEFAULT 'Standard',
    double_opt_in TINYINT(1) DEFAULT 0
);

CREATE TABLE tenant_communication_settings (
    tenant_communication_setting_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    verification TINYINT(1) DEFAULT 1,
    communication TINYINT(1) DEFAULT 1,
    welcome_message TEXT,
    sender_name VARCHAR(255) DEFAULT 'LMS Academy Team',
    reply_to VARCHAR(255)
);

CREATE TABLE general_settings (
    general_setting_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    logo LONGTEXT,
    site_name VARCHAR(255),
    language VARCHAR(20) DEFAULT 'en',
    timezone VARCHAR(50) DEFAULT 'UTC'
);

CREATE TABLE custom_user_fields (
    custom_field_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    field_key VARCHAR(100),
    label VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    icon VARCHAR(100),
    display_order BIGINT
);

-- =========================
-- AFFILIATES
-- =========================
CREATE TABLE `affiliates` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `user_id` BIGINT UNIQUE,
  `type` VARCHAR(50) NOT NULL,

  `referral_code` VARCHAR(20) NOT NULL UNIQUE,
  `name` VARCHAR(100) NOT NULL,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `mobile` VARCHAR(15) NOT NULL UNIQUE,

  `status` VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
  `commission_type` VARCHAR(50) NOT NULL DEFAULT 'PERCENTAGE',

  `commission_value` DECIMAL(19,4) NOT NULL,
  `student_discount_value` DECIMAL(19,4),

  `cookie_days` INT,
  `min_payout` DECIMAL(19,4),

  `withdrawal_enabled` BOOLEAN DEFAULT TRUE,

  `bank_name` VARCHAR(100),
  `account_number` VARCHAR(50),
  `ifsc_code` VARCHAR(20),
  `account_holder_name` VARCHAR(100),
  `upi_id` VARCHAR(100),

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,

  KEY `idx_affiliate_user_id` (`user_id`),
  KEY `idx_affiliate_email` (`email`),
  KEY `idx_affiliate_username` (`username`),
  KEY `idx_affiliate_status` (`status`),
  KEY `idx_affiliate_type` (`type`)
);

-- =========================
-- AFFILIATE CLICKS
-- =========================
CREATE TABLE `affiliate_clicks` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `clicked_code` VARCHAR(100) NOT NULL,
  `affiliate_code` VARCHAR(100) NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `ip_address` VARCHAR(100),
  `user_agent` VARCHAR(255),
  `clicked_at` DATETIME,

  KEY `idx_click_affiliate` (`affiliate_code`),
  KEY `idx_click_clicked_code` (`clicked_code`),
  KEY `idx_batch_click` (`batch_id`)
);

-- =========================
-- AFFILIATE LEADS
-- =========================
CREATE TABLE `affiliate_leads` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `name` VARCHAR(100) NOT NULL,
  `mobile` VARCHAR(20) NOT NULL,
  `email` VARCHAR(100) NOT NULL,

  `course_id` BIGINT NOT NULL,
  `affiliate_id` BIGINT,

  `student_id` BIGINT,
  `batch_id` BIGINT NOT NULL,
  `link_id` BIGINT,

  `referral_code` VARCHAR(50),
  `lead_source` VARCHAR(50),
  `status` VARCHAR(50),
  `rejection_reason` VARCHAR(255),

  `expires_at` DATETIME,
  `ip_address` VARCHAR(100),

  `created_at` DATETIME,
  `updated_at` DATETIME,

  UNIQUE KEY `uk_mobile_batch` (`mobile`, `batch_id`),
  KEY `idx_lead_affiliate` (`affiliate_id`),

  CONSTRAINT `fk_lead_affiliate`
    FOREIGN KEY (`affiliate_id`)
    REFERENCES `affiliates` (`id`)
    ON DELETE SET NULL
);

-- =========================
-- AFFILIATE LINKS
-- =========================
CREATE TABLE `affiliate_links` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `affiliate_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `referral_code` VARCHAR(20) NOT NULL UNIQUE,
  `status` VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',

  `expires_at` DATETIME,

  `commission_value` DECIMAL(19,4) NOT NULL,
  `student_discount_value` DECIMAL(19,4) NOT NULL,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,

  UNIQUE KEY `uk_affiliate_batch` (`affiliate_id`, `batch_id`),

  KEY `idx_aff_link_affiliate` (`affiliate_id`),
  KEY `idx_aff_link_batch` (`batch_id`),
  KEY `idx_aff_link_referral_code` (`referral_code`),

  CONSTRAINT `fk_affiliate_link_affiliate`
    FOREIGN KEY (`affiliate_id`)
    REFERENCES `affiliates` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- AFFILIATE PAYOUTS
-- =========================
CREATE TABLE `affiliate_payouts` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `affiliate_id` BIGINT NOT NULL,

  `amount` DECIMAL(19,4),
  `status` VARCHAR(50),

  `payment_reference` VARCHAR(255),

  `paid_at` DATETIME,
  `created_at` DATETIME,

  KEY `idx_payout_affiliate` (`affiliate_id`),

  CONSTRAINT `fk_payout_affiliate`
    FOREIGN KEY (`affiliate_id`)
    REFERENCES `affiliates` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- AFFILIATE SALES
-- =========================
CREATE TABLE `affiliate_sales` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `affiliate_id` BIGINT NOT NULL,

  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `order_id` VARCHAR(100) NOT NULL UNIQUE,

  `lead_id` BIGINT,
  `student_id` BIGINT,

  `original_amount` DECIMAL(19,4) NOT NULL,
  `discount_amount` DECIMAL(19,4),
  `order_amount` DECIMAL(19,4) NOT NULL,
  `commission_amount` DECIMAL(19,4) NOT NULL,

  `status` VARCHAR(50),

  `payout_id` BIGINT,

  `created_at` DATETIME,
  `approved_at` DATETIME,
  `paid_at` DATETIME,

  KEY `idx_sale_affiliate` (`affiliate_id`),
  KEY `idx_batch_sale` (`batch_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_sale_status` (`status`),

  CONSTRAINT `fk_sale_affiliate`
    FOREIGN KEY (`affiliate_id`)
    REFERENCES `affiliates` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- AFFILIATE WALLET
-- =========================
CREATE TABLE `affiliate_wallets` (
  `id` BIGINT PRIMARY KEY,

  `affiliate_id` BIGINT NOT NULL UNIQUE,

  `balance` DECIMAL(19,4) NOT NULL DEFAULT 0,
  `total_earned` DECIMAL(19,4) NOT NULL DEFAULT 0,
  `total_paid` DECIMAL(19,4) NOT NULL DEFAULT 0,

  `updated_at` DATETIME NOT NULL,

  CONSTRAINT `fk_wallet_affiliate`
    FOREIGN KEY (`affiliate_id`)
    REFERENCES `affiliates` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- AFFILIATE WALLET TRANSACTIONS
-- =========================
CREATE TABLE `affiliate_wallet_transactions` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `affiliate_id` BIGINT NOT NULL,

  `type` VARCHAR(20) NOT NULL,
  `amount` DECIMAL(19,4) NOT NULL,

  `sale_id` BIGINT,
  `description` VARCHAR(255),

  `created_at` DATETIME,

  KEY `idx_wallet_tx_affiliate` (`affiliate_id`),

  CONSTRAINT `fk_wallet_tx_affiliate`
    FOREIGN KEY (`affiliate_id`)
    REFERENCES `affiliates` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- COMMISSION RULES
-- =========================
CREATE TABLE `commission_rules` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `course_id` BIGINT NOT NULL,

  `commission_percent` DECIMAL(19,4) NOT NULL DEFAULT 0,

  `is_bonus` BOOLEAN NOT NULL,
  `active` BOOLEAN NOT NULL,

  `created_at` DATETIME,

  KEY `idx_course_rule` (`course_id`)
);

-- =========================
-- LEAD NOTES
-- =========================
CREATE TABLE `lead_notes` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `lead_id` BIGINT NOT NULL,
  `note` TEXT NOT NULL,
  `created_by` VARCHAR(255) NOT NULL,

  `created_at` DATETIME
);

-- =========================
-- LEAD STATUS HISTORY
-- =========================
CREATE TABLE `lead_status_history` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `lead_id` BIGINT NOT NULL,

  `old_status` VARCHAR(50),
  `new_status` VARCHAR(50) NOT NULL,

  `changed_by` VARCHAR(255),
  `notes` VARCHAR(255),

  `timestamp` DATETIME
);

-- =========================
-- WALLET CONFIG
-- =========================
CREATE TABLE `wallet_configs` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `default_min_payout_amount` DECIMAL(19,4) NOT NULL,

  `student_withdrawal_enabled` BOOLEAN NOT NULL DEFAULT FALSE,

  `updated_at` DATETIME NOT NULL
);

-- =========================
-- CAMPAIGNS
-- =========================
DROP TABLE IF EXISTS `campaigns`;
CREATE TABLE `campaigns` (
  `campaign_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `campaign_name` VARCHAR(150) NOT NULL,
  `subject` VARCHAR(200),
  `campaign_type` VARCHAR(50),

  `start_date` DATE,
  `end_date` DATE,

  `budget` DECIMAL(19,2) NOT NULL,

  `status` VARCHAR(20),
  `description` VARCHAR(500),

  `channel` VARCHAR(50) NOT NULL,
  `target_audience` VARCHAR(50) NOT NULL,

  `audience_filters` TEXT,
  `archived_at` DATETIME
);

-- =========================
-- CAMPAIGN PERFORMANCE
-- =========================
DROP TABLE IF EXISTS `campaign_performance`;
CREATE TABLE `campaign_performance` (
  `performance_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `campaign_id` BIGINT NOT NULL,

  `impressions` BIGINT DEFAULT 0,
  `clicks` BIGINT DEFAULT 0,
  `conversions` BIGINT DEFAULT 0,

  `cost` DECIMAL(19,4) DEFAULT 0,
  `revenue` DECIMAL(19,4) DEFAULT 0,

  `recorded_date` DATE NOT NULL,

  KEY `idx_perf_campaign_date` (`campaign_id`, `recorded_date`),

  CONSTRAINT `fk_perf_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE CASCADE
);

-- =========================
-- CONTENT
-- =========================
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `content_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `content_title` VARCHAR(150) NOT NULL,
  `content_type` VARCHAR(50),
  `platform` VARCHAR(50),

  `content_url` VARCHAR(255),
  `created_date` DATE,

  `campaign_id` BIGINT,

  CONSTRAINT `fk_content_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- COUPONS
-- =========================
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `code` VARCHAR(50) NOT NULL UNIQUE,
  `discount_type` VARCHAR(20) NOT NULL,

  `discount_value` DOUBLE NOT NULL,
  `discount_cap` DOUBLE,

  `expiry_date` DATETIME,

  `max_usage` INT,
  `used_count` INT DEFAULT 0,

  `min_purchase_amount` DOUBLE DEFAULT 0,
  `per_user_limit` INT DEFAULT 1,

  `is_first_order_only` BOOLEAN DEFAULT FALSE,
  `auto_apply` BOOLEAN DEFAULT FALSE,

  `affiliate_id` BIGINT,
  `learner_id` BIGINT,

  `status` VARCHAR(20),
  `deleted` BOOLEAN DEFAULT FALSE,

  `campaign_id` BIGINT,

  `created_by` VARCHAR(255),
  `created_at` DATETIME,

  KEY `idx_coupon_code` (`code`),

  CONSTRAINT `fk_coupon_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- COUPON COURSES
-- =========================
DROP TABLE IF EXISTS `coupon_courses`;
CREATE TABLE `coupon_courses` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `coupon_id` BIGINT,
  `course_id` BIGINT,

  CONSTRAINT `fk_coupon_course`
    FOREIGN KEY (`coupon_id`)
    REFERENCES `coupons` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- COUPON USAGE
-- =========================
DROP TABLE IF EXISTS `coupon_usage`;
CREATE TABLE `coupon_usage` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `coupon_id` BIGINT NOT NULL,
  `learner_id` BIGINT NOT NULL,

  `usage_count` INT DEFAULT 0,
  `order_id` BIGINT,

  `used_at` DATETIME NOT NULL,

  UNIQUE KEY `uk_coupon_user` (`coupon_id`, `learner_id`)
);
-- =========================
-- EMAIL CAMPAIGNS
-- =========================
DROP TABLE IF EXISTS `email_campaigns`;
CREATE TABLE `email_campaigns` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `title` VARCHAR(255) NOT NULL,
  `subject` VARCHAR(255) NOT NULL,
  `content` TEXT,

  `status` VARCHAR(25) DEFAULT 'DRAFT',

  `channel` VARCHAR(50),
  `campaign_type` VARCHAR(50),
  `trigger_event` VARCHAR(100),

  `from_name` VARCHAR(255),
  `from_email` VARCHAR(255),
  `reply_to` VARCHAR(255),

  `total_recipients` INT DEFAULT 0,
  `success_count` INT DEFAULT 0,
  `failed_count` INT DEFAULT 0,

  `core_campaign_id` BIGINT,

  `created_at` DATETIME,
  `scheduled_at` DATETIME,
  `last_executed_at` DATETIME,

  CONSTRAINT `fk_email_campaign_core`
    FOREIGN KEY (`core_campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- EMAIL RECIPIENTS
-- =========================
DROP TABLE IF EXISTS `email_recipients`;
CREATE TABLE `email_recipients` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `email` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255),

  `status` VARCHAR(25) DEFAULT 'PENDING',

  `sent_at` DATETIME,
  `failure_reason` TEXT,

  `campaign_id` BIGINT NOT NULL,

  `created_at` DATETIME,

  UNIQUE KEY `uk_campaign_email` (`campaign_id`, `email`),

  CONSTRAINT `fk_email_recipient_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `email_campaigns` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- INTERACTIONS
-- =========================
DROP TABLE IF EXISTS `interactions`;
CREATE TABLE `interactions` (
  `interaction_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `campaign_id` BIGINT NOT NULL,
  `customer_email` VARCHAR(255) NOT NULL,

  `content_id` BIGINT,
  `action_type` VARCHAR(20),

  `timestamp` DATETIME,

  KEY `idx_interaction_campaign` (`campaign_id`),
  KEY `idx_interaction_content` (`content_id`),
  KEY `idx_interaction_date` (`timestamp`),

  CONSTRAINT `fk_interaction_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE CASCADE
);

-- =========================
-- LANDING PAGES
-- =========================
DROP TABLE IF EXISTS `landing_pages`;
CREATE TABLE `landing_pages` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `slug` VARCHAR(255) NOT NULL UNIQUE,
  `title` VARCHAR(255) NOT NULL,

  `headline` VARCHAR(255),
  `subtitle` VARCHAR(255),

  `price` DECIMAL(19,4),
  `ad_budget` DECIMAL(19,4),

  `video_url` VARCHAR(255),
  `cta_text` VARCHAR(255),

  `created_at` DATETIME
);

-- =========================
-- LANDING PAGE FEATURES
-- =========================
DROP TABLE IF EXISTS `landing_page_features`;
CREATE TABLE `landing_page_features` (
  `landing_page_id` BIGINT,
  `feature` VARCHAR(255),

  CONSTRAINT `fk_lp_feature`
    FOREIGN KEY (`landing_page_id`)
    REFERENCES `landing_pages` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- MARKETING LEADS
-- =========================
DROP TABLE IF EXISTS `marketing_leads`;
CREATE TABLE `marketing_leads` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `tracked_link_id` VARCHAR(100),
  `session_id` VARCHAR(100),

  `name` VARCHAR(255),
  `email` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(50),

  `course_interest` VARCHAR(255),

  `course_id` BIGINT,
  `batch_id` BIGINT,
  `student_id` BIGINT,

  `referral_code` VARCHAR(100),
  `affiliate_id` BIGINT,

  `source` VARCHAR(100),

  `utm_source` VARCHAR(100),
  `utm_campaign` VARCHAR(100),
  `utm_medium` VARCHAR(100),
  `utm_content` VARCHAR(100),

  `ip_address` VARCHAR(100),

  `campaign_id` BIGINT,

  `status` VARCHAR(20),
  `rejection_reason` VARCHAR(255),

  `assigned_to_user_id` BIGINT,

  `conversion_value` DECIMAL(19,4),

  `last_activity_at` DATETIME,
  `expires_at` DATETIME,
  `created_at` DATETIME,
  `updated_at` DATETIME,

  KEY `idx_lead_email` (`email`),
  KEY `idx_lead_campaign` (`campaign_id`),

  CONSTRAINT `fk_marketing_lead_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- ORDERS
-- =========================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `campaign_id` BIGINT,
  `campaign_id_snapshot` BIGINT,

  `utm_source` VARCHAR(100),
  `utm_medium` VARCHAR(100),
  `utm_campaign` VARCHAR(100),

  `user_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,

  `total_amount` DECIMAL(19,4) NOT NULL,
  `wallet_used` DECIMAL(19,4) DEFAULT 0,
  `coupon_discount` DECIMAL(19,4) DEFAULT 0,

  `coupon_code` VARCHAR(100),

  `final_paid_amount` DECIMAL(19,4) NOT NULL,

  `status` VARCHAR(20) DEFAULT 'PENDING',

  `created_at` DATETIME,

  CONSTRAINT `fk_order_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- PUSH SUBSCRIPTIONS
-- =========================
DROP TABLE IF EXISTS `push_subscriptions`;
CREATE TABLE `push_subscriptions` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `learner_id` BIGINT NOT NULL,

  `device_token` VARCHAR(255) NOT NULL UNIQUE,
  `platform` VARCHAR(20),

  `created_at` DATETIME
);

-- =========================
-- PUSH NOTIFICATIONS
-- =========================
DROP TABLE IF EXISTS `push_notifications`;
CREATE TABLE `push_notifications` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `title` VARCHAR(255) NOT NULL,
  `body` TEXT NOT NULL,

  `target_channel` VARCHAR(100),
  `recipients_count` INT,

  `sent_at` DATETIME,

  `link` VARCHAR(255),

  `scheduled_at` DATETIME,
  `created_at` DATETIME,

  `status` VARCHAR(20) DEFAULT 'PENDING'
);

-- =========================
-- REFERRAL CODES
-- =========================
DROP TABLE IF EXISTS `referral_codes`;
CREATE TABLE `referral_codes` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `user_id` BIGINT NOT NULL UNIQUE,
  `code` VARCHAR(100) NOT NULL UNIQUE,

  `created_at` DATETIME
);

-- =========================
-- REFERRAL USAGE
-- =========================
DROP TABLE IF EXISTS `referral_usage`;
CREATE TABLE `referral_usage` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `referral_code_id` BIGINT,
  `referrer_user_id` BIGINT,
  `referred_user_id` BIGINT,
  `course_id` BIGINT,

  `reward_amount` DECIMAL(19,4),
  `reward_status` VARCHAR(20),

  `created_at` DATETIME,

  UNIQUE KEY `uk_ref_user_course` (`referred_user_id`, `course_id`),

  CONSTRAINT `fk_referral_usage_code`
    FOREIGN KEY (`referral_code_id`)
    REFERENCES `referral_codes` (`id`)
    ON DELETE SET NULL
);

-- =========================
-- TRACKED LINKS
-- =========================
DROP TABLE IF EXISTS `tracked_links`;
CREATE TABLE `tracked_links` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `tracked_link_id` VARCHAR(50) NOT NULL UNIQUE,
  `landing_slug` VARCHAR(255) NOT NULL,

  `source` VARCHAR(100),
  `medium` VARCHAR(100),
  `campaign` VARCHAR(100),

  `generated_link` VARCHAR(2048) NOT NULL,

  `ad_budget` DECIMAL(19,4),

  `timestamp` DATETIME
);

-- =========================
-- TRAFFIC EVENTS
-- =========================
DROP TABLE IF EXISTS `traffic_events`;
CREATE TABLE `traffic_events` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `session_id` VARCHAR(100) NOT NULL,
  `user_id` BIGINT,

  `event_type` VARCHAR(50) NOT NULL,
  `tracked_link_id` VARCHAR(100),

  `tracked_link_ref_id` BIGINT,

  `source` VARCHAR(100),

  `utm_source` VARCHAR(100),
  `utm_campaign` VARCHAR(100),
  `utm_medium` VARCHAR(100),

  `page` VARCHAR(255),
  `metadata_json` TEXT,

  `timestamp` DATETIME,

  KEY `idx_traffic_session` (`session_id`),
  KEY `idx_traffic_user` (`user_id`),
  KEY `idx_traffic_event` (`event_type`),

  CONSTRAINT `fk_traffic_tracked_link`
    FOREIGN KEY (`tracked_link_ref_id`)
    REFERENCES `tracked_links` (`id`)
    ON DELETE SET NULL
);
-- =========================
-- EMAIL CAMPAIGNS
-- =========================
DROP TABLE IF EXISTS `email_campaigns`;
CREATE TABLE `email_campaigns` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `title` VARCHAR(255) NOT NULL,
  `subject` VARCHAR(255) NOT NULL,
  `content` TEXT,

  `status` VARCHAR(25) DEFAULT 'DRAFT',

  `channel` VARCHAR(50),
  `campaign_type` VARCHAR(50),
  `trigger_event` VARCHAR(100),

  `from_name` VARCHAR(255),
  `from_email` VARCHAR(255),
  `reply_to` VARCHAR(255),

  `total_recipients` INT DEFAULT 0,
  `success_count` INT DEFAULT 0,
  `failed_count` INT DEFAULT 0,

  `core_campaign_id` BIGINT,

  `created_at` DATETIME,
  `scheduled_at` DATETIME,
  `last_executed_at` DATETIME,

  CONSTRAINT `fk_email_campaign_core`
    FOREIGN KEY (`core_campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- EMAIL RECIPIENTS
-- =========================
DROP TABLE IF EXISTS `email_recipients`;
CREATE TABLE `email_recipients` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `email` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255),

  `status` VARCHAR(25) DEFAULT 'PENDING',

  `sent_at` DATETIME,
  `failure_reason` TEXT,

  `campaign_id` BIGINT NOT NULL,

  `created_at` DATETIME,

  UNIQUE KEY `uk_campaign_email` (`campaign_id`, `email`),

  CONSTRAINT `fk_email_recipient_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `email_campaigns` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- INTERACTIONS
-- =========================
DROP TABLE IF EXISTS `interactions`;
CREATE TABLE `interactions` (
  `interaction_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `campaign_id` BIGINT NOT NULL,
  `customer_email` VARCHAR(255) NOT NULL,

  `content_id` BIGINT,
  `action_type` VARCHAR(20),

  `timestamp` DATETIME,

  KEY `idx_interaction_campaign` (`campaign_id`),
  KEY `idx_interaction_content` (`content_id`),
  KEY `idx_interaction_date` (`timestamp`),

  CONSTRAINT `fk_interaction_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE CASCADE
);

-- =========================
-- LANDING PAGES
-- =========================
DROP TABLE IF EXISTS `landing_pages`;
CREATE TABLE `landing_pages` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `slug` VARCHAR(255) NOT NULL UNIQUE,
  `title` VARCHAR(255) NOT NULL,

  `headline` VARCHAR(255),
  `subtitle` VARCHAR(255),

  `price` DECIMAL(19,4),
  `ad_budget` DECIMAL(19,4),

  `video_url` VARCHAR(255),
  `cta_text` VARCHAR(255),

  `created_at` DATETIME
);

-- =========================
-- LANDING PAGE FEATURES
-- =========================
DROP TABLE IF EXISTS `landing_page_features`;
CREATE TABLE `landing_page_features` (
  `landing_page_id` BIGINT,
  `feature` VARCHAR(255),

  CONSTRAINT `fk_lp_feature`
    FOREIGN KEY (`landing_page_id`)
    REFERENCES `landing_pages` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- MARKETING LEADS
-- =========================
DROP TABLE IF EXISTS `marketing_leads`;
CREATE TABLE `marketing_leads` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `tracked_link_id` VARCHAR(100),
  `session_id` VARCHAR(100),

  `name` VARCHAR(255),
  `email` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(50),

  `course_interest` VARCHAR(255),

  `course_id` BIGINT,
  `batch_id` BIGINT,
  `student_id` BIGINT,

  `referral_code` VARCHAR(100),
  `affiliate_id` BIGINT,

  `source` VARCHAR(100),

  `utm_source` VARCHAR(100),
  `utm_campaign` VARCHAR(100),
  `utm_medium` VARCHAR(100),
  `utm_content` VARCHAR(100),

  `ip_address` VARCHAR(100),

  `campaign_id` BIGINT,

  `status` VARCHAR(20),
  `rejection_reason` VARCHAR(255),

  `assigned_to_user_id` BIGINT,

  `conversion_value` DECIMAL(19,4),

  `last_activity_at` DATETIME,
  `expires_at` DATETIME,
  `created_at` DATETIME,
  `updated_at` DATETIME,

  KEY `idx_lead_email` (`email`),
  KEY `idx_lead_campaign` (`campaign_id`),

  CONSTRAINT `fk_marketing_lead_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- ORDERS
-- =========================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `campaign_id` BIGINT,
  `campaign_id_snapshot` BIGINT,

  `utm_source` VARCHAR(100),
  `utm_medium` VARCHAR(100),
  `utm_campaign` VARCHAR(100),

  `user_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,

  `total_amount` DECIMAL(19,4) NOT NULL,
  `wallet_used` DECIMAL(19,4) DEFAULT 0,
  `coupon_discount` DECIMAL(19,4) DEFAULT 0,

  `coupon_code` VARCHAR(100),

  `final_paid_amount` DECIMAL(19,4) NOT NULL,

  `status` VARCHAR(20) DEFAULT 'PENDING',

  `created_at` DATETIME,

  CONSTRAINT `fk_order_campaign`
    FOREIGN KEY (`campaign_id`)
    REFERENCES `campaigns` (`campaign_id`)
    ON DELETE SET NULL
);

-- =========================
-- PUSH SUBSCRIPTIONS
-- =========================
DROP TABLE IF EXISTS `push_subscriptions`;
CREATE TABLE `push_subscriptions` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `learner_id` BIGINT NOT NULL,

  `device_token` VARCHAR(255) NOT NULL UNIQUE,
  `platform` VARCHAR(20),

  `created_at` DATETIME
);

-- =========================
-- PUSH NOTIFICATIONS
-- =========================
DROP TABLE IF EXISTS `push_notifications`;
CREATE TABLE `push_notifications` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `title` VARCHAR(255) NOT NULL,
  `body` TEXT NOT NULL,

  `target_channel` VARCHAR(100),
  `recipients_count` INT,

  `sent_at` DATETIME,

  `link` VARCHAR(255),

  `scheduled_at` DATETIME,
  `created_at` DATETIME,

  `status` VARCHAR(20) DEFAULT 'PENDING'
);

-- =========================
-- REFERRAL CODES
-- =========================
DROP TABLE IF EXISTS `referral_codes`;
CREATE TABLE `referral_codes` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `user_id` BIGINT NOT NULL UNIQUE,
  `code` VARCHAR(100) NOT NULL UNIQUE,

  `created_at` DATETIME
);

-- =========================
-- REFERRAL USAGE
-- =========================
DROP TABLE IF EXISTS `referral_usage`;
CREATE TABLE `referral_usage` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `referral_code_id` BIGINT,
  `referrer_user_id` BIGINT,
  `referred_user_id` BIGINT,
  `course_id` BIGINT,

  `reward_amount` DECIMAL(19,4),
  `reward_status` VARCHAR(20),

  `created_at` DATETIME,

  UNIQUE KEY `uk_ref_user_course` (`referred_user_id`, `course_id`),

  CONSTRAINT `fk_referral_usage_code`
    FOREIGN KEY (`referral_code_id`)
    REFERENCES `referral_codes` (`id`)
    ON DELETE SET NULL
);

-- =========================
-- TRACKED LINKS
-- =========================
DROP TABLE IF EXISTS `tracked_links`;
CREATE TABLE `tracked_links` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `tracked_link_id` VARCHAR(50) NOT NULL UNIQUE,
  `landing_slug` VARCHAR(255) NOT NULL,

  `source` VARCHAR(100),
  `medium` VARCHAR(100),
  `campaign` VARCHAR(100),

  `generated_link` VARCHAR(2048) NOT NULL,

  `ad_budget` DECIMAL(19,4),

  `timestamp` DATETIME
);

-- =========================
-- TRAFFIC EVENTS
-- =========================
DROP TABLE IF EXISTS `traffic_events`;
CREATE TABLE `traffic_events` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `session_id` VARCHAR(100) NOT NULL,
  `user_id` BIGINT,

  `event_type` VARCHAR(50) NOT NULL,
  `tracked_link_id` VARCHAR(100),

  `tracked_link_ref_id` BIGINT,

  `source` VARCHAR(100),

  `utm_source` VARCHAR(100),
  `utm_campaign` VARCHAR(100),
  `utm_medium` VARCHAR(100),

  `page` VARCHAR(255),
  `metadata_json` TEXT,

  `timestamp` DATETIME,

  KEY `idx_traffic_session` (`session_id`),
  KEY `idx_traffic_user` (`user_id`),
  KEY `idx_traffic_event` (`event_type`),

  CONSTRAINT `fk_traffic_tracked_link`
    FOREIGN KEY (`tracked_link_ref_id`)
    REFERENCES `tracked_links` (`id`)
    ON DELETE SET NULL
);

-- =========================
-- EARLY PAYMENTS
-- =========================
DROP TABLE IF EXISTS `early_payments`;
CREATE TABLE `early_payments` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `student_id` BIGINT NOT NULL,

  `total_original_amount` DECIMAL(12,2) NOT NULL,
  `discount_type` VARCHAR(20) NOT NULL,
  `discount_value` DECIMAL(12,2) NOT NULL,
  `final_amount` DECIMAL(12,2) NOT NULL,

  `cashfree_order_id` VARCHAR(255),
  `payment_session_id` VARCHAR(255),

  `link_created_at` DATETIME,
  `link_expiry` DATETIME,

  `status` VARCHAR(20) NOT NULL DEFAULT 'CREATED'
);

-- =========================
-- EARLY PAYMENT INSTALLMENTS
-- =========================
DROP TABLE IF EXISTS `early_payment_installments`;
CREATE TABLE `early_payment_installments` (
  `early_payment_id` BIGINT,
  `installment_id` BIGINT,

  CONSTRAINT `fk_early_payment_installment`
    FOREIGN KEY (`early_payment_id`)
    REFERENCES `early_payments` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- FEE AUDIT LOGS
-- =========================
DROP TABLE IF EXISTS `fee_audit_logs`;
CREATE TABLE `fee_audit_logs` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `module` VARCHAR(50) NOT NULL,
  `entity_name` VARCHAR(100) NOT NULL,
  `entity_id` BIGINT NOT NULL,

  `action` VARCHAR(20) NOT NULL,

  `old_value` TEXT,
  `new_value` TEXT,

  `performed_by` BIGINT,
  `ip_address` VARCHAR(50),

  `performed_at` DATETIME
);

-- =========================
-- FEE DISCOUNTS
-- =========================
DROP TABLE IF EXISTS `fee_discounts`;
CREATE TABLE `fee_discounts` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `user_id` BIGINT,
  `fee_structure_id` BIGINT NOT NULL,

  `discount_name` VARCHAR(100) NOT NULL,
  `discount_type` VARCHAR(20) NOT NULL,

  `discount_value` DECIMAL(10,2) NOT NULL,
  `base_amount` DECIMAL(10,2) NOT NULL,
  `discount_amount` DECIMAL(10,2) NOT NULL,
  `final_amount` DECIMAL(10,2) NOT NULL,

  `reason` VARCHAR(255),

  `approved_by` BIGINT,
  `approved_date` DATE,

  `is_active` BOOLEAN DEFAULT TRUE,

  `created_at` DATETIME,
  `updated_at` DATETIME,

  `admission_fee` DECIMAL(10,2) NOT NULL,

  `discount_scope` VARCHAR(20) NOT NULL,
  `scope_id` BIGINT NOT NULL,

  `installment_count` INT DEFAULT 1,

  `gst_percent` DECIMAL(5,2) NOT NULL,
  `gst_amount` DECIMAL(10,2) NOT NULL,
  `payable_amount` DECIMAL(10,2) NOT NULL
);

-- =========================
-- FEE STRUCTURES
-- =========================
DROP TABLE IF EXISTS `fee_structures`;
CREATE TABLE `fee_structures` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `structure_name` VARCHAR(255) NOT NULL,
  `academic_year` VARCHAR(50) NOT NULL,

  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT,

  `total_amount` DECIMAL(12,2) NOT NULL,
  `base_amount` DECIMAL(12,2) NOT NULL,

  `active` BOOLEAN NOT NULL,
  `is_active` BOOLEAN NOT NULL,

  `fee_type_id` BIGINT,
  `batch_name` VARCHAR(255),
  `course_name` VARCHAR(255),

  `currency` VARCHAR(10),

  `admission_fee_amount` DECIMAL(12,2),
  `admission_non_refundable` BOOLEAN,

  `gst_included_in_fee` BOOLEAN,

  `installment_count` INT,
  `duration_months` INT NOT NULL,

  `start_date` DATE,
  `end_date` DATE,

  `grace_days` INT,

  `gst_applicable` BOOLEAN,
  `gst_percent` DECIMAL(5,2),

  `penalty_type` VARCHAR(20),
  `max_penalty_cap` DECIMAL(12,2),
  `penalty_percentage` DECIMAL(5,2),
  `fixed_penalty_amount` DECIMAL(12,2),

  `discount_type` VARCHAR(20),
  `discount_value` DECIMAL(12,2),

  `created_at` DATETIME,
  `updated_at` DATETIME
);

-- =========================
-- FEE STRUCTURE COMPONENTS
-- =========================
DROP TABLE IF EXISTS `fee_structure_components`;
CREATE TABLE `fee_structure_components` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `fee_structure_id` BIGINT NOT NULL,

  `fee_type_id` BIGINT,

  `amount` DECIMAL(12,2) NOT NULL,
  `name` VARCHAR(255),

  `due_date` DATE,
  `base_amount` DECIMAL(12,2),

  `active` BOOLEAN,
  `is_active` BOOLEAN,

  `fixed_flag` BOOLEAN,
  `is_fixed_flag` BOOLEAN,

  `refundable` BOOLEAN,
  `is_refundable` BOOLEAN,

  `non_refundable_flag` BOOLEAN,

  `installment_allowed` BOOLEAN,
  `mandatory` BOOLEAN,
  `is_mandatory` BOOLEAN,

  CONSTRAINT `fk_fee_structure_component`
    FOREIGN KEY (`fee_structure_id`)
    REFERENCES `fee_structures` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- FEE TYPES
-- =========================
DROP TABLE IF EXISTS `fee_types`;
CREATE TABLE `fee_types` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `name` VARCHAR(100) NOT NULL UNIQUE,
  `description` VARCHAR(255),

  `is_active` BOOLEAN,
  `is_mandatory` BOOLEAN,
  `is_refundable` BOOLEAN,
  `is_one_time` BOOLEAN,

  `display_order` INT,

  `created_at` DATETIME,
  `updated_at` DATETIME
);

-- =========================
-- GLOBAL CONFIGS
-- =========================
DROP TABLE IF EXISTS `global_configs`;
CREATE TABLE `global_configs` (
  `config_key` VARCHAR(100) PRIMARY KEY,
  `config_value` VARCHAR(255),
  `description` VARCHAR(255)
);

-- =========================
-- LATE FEE CONFIG
-- =========================
DROP TABLE IF EXISTS `late_fee_config`;
CREATE TABLE `late_fee_config` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `payment_schedule` VARCHAR(20),
  `period_count` INT,

  `penalty_amount` DECIMAL(10,2),

  `is_active` BOOLEAN,

  `effective_from` DATE,
  `effective_to` DATE,

  `created_by` BIGINT
);

-- =========================
-- LATE FEE PENALTIES
-- =========================
DROP TABLE IF EXISTS `late_fee_penalties`;
CREATE TABLE `late_fee_penalties` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `student_installment_plan_id` BIGINT NOT NULL,

  `penalty_amount` DECIMAL(10,2) NOT NULL,
  `penalty_date` DATE NOT NULL,

  `reason` VARCHAR(255),

  `is_waived` BOOLEAN,

  `waived_by` BIGINT,
  `waived_date` DATE,

  `created_at` DATETIME,
  `updated_at` DATETIME
);

-- =========================
-- LATE FEE SLABS
-- =========================
DROP TABLE IF EXISTS `late_fee_slabs`;
CREATE TABLE `late_fee_slabs` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `config_id` BIGINT NOT NULL,

  `from_day` INT,
  `to_day` INT,

  `fine_per_day` DECIMAL(10,2),

  CONSTRAINT `fk_late_fee_config`
    FOREIGN KEY (`config_id`)
    REFERENCES `late_fee_config` (`id`)
    ON DELETE CASCADE
);
-- =========================
-- MASTER SETTINGS
-- =========================
DROP TABLE IF EXISTS `master_settings`;
CREATE TABLE `master_settings` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `type` VARCHAR(50) NOT NULL,
  `key_name` VARCHAR(255) NOT NULL,
  `setting_value` VARCHAR(255) NOT NULL,

  `description` VARCHAR(255),
  `active` BOOLEAN NOT NULL DEFAULT TRUE
);

-- =========================
-- PAYMENT NOTIFICATIONS
-- =========================
DROP TABLE IF EXISTS `payment_notifications`;
CREATE TABLE `payment_notifications` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `user_id` BIGINT NOT NULL,
  `notification_type` VARCHAR(50) NOT NULL,

  `message` TEXT NOT NULL,

  `email` VARCHAR(255),
  `phone` VARCHAR(20),

  `sent_at` DATETIME,
  `delivery_status` VARCHAR(20) DEFAULT 'PENDING',

  `created_at` DATETIME
);

-- =========================
-- FEE PENALTY SLABS
-- =========================
DROP TABLE IF EXISTS `fee_penalty_slabs`;
CREATE TABLE `fee_penalty_slabs` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `fee_structure_id` BIGINT NOT NULL,

  `from_day` INT NOT NULL,
  `to_day` INT NOT NULL,

  `penalty_amount` DECIMAL(12,2) NOT NULL,

  `active` BOOLEAN NOT NULL,
  `is_active` BOOLEAN NOT NULL,

  `days_overdue` INT NOT NULL,

  CONSTRAINT `fk_fee_penalty_structure`
    FOREIGN KEY (`fee_structure_id`)
    REFERENCES `fee_structures` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- REFUND RULES
-- =========================
DROP TABLE IF EXISTS `refund_rules`;
CREATE TABLE `refund_rules` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `rule_name` VARCHAR(255) NOT NULL,
  `days_before_start` INT,

  `refund_percentage` DECIMAL(5,2),

  `is_active` BOOLEAN DEFAULT TRUE,

  `created_at` DATETIME,
  `updated_at` DATETIME
);

-- =========================
-- REMINDER JOBS
-- =========================
DROP TABLE IF EXISTS `reminder_jobs`;
CREATE TABLE `reminder_jobs` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `installment_id` BIGINT NOT NULL,
  `reminder_offset` INT NOT NULL,

  `scheduled_date` DATE NOT NULL,

  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',

  `retry_count` INT NOT NULL DEFAULT 0,
  `next_retry_time` DATETIME,

  `created_at` DATETIME,

  UNIQUE KEY `uk_installment_reminder` (`installment_id`, `reminder_offset`)
);

-- =========================
-- STUDENT FEES
-- =========================
DROP TABLE IF EXISTS `student_fees`;
CREATE TABLE `student_fees` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `student_id` BIGINT NOT NULL,
  `fee_structure_id` BIGINT NOT NULL,

  `discount_type` VARCHAR(20),
  `discount_value` DECIMAL(12,2),
  `discount_reason` VARCHAR(255),
  `approved_by` VARCHAR(255),

  `total_payable` DECIMAL(12,2),
  `total_paid` DECIMAL(12,2) DEFAULT 0,

  `status` VARCHAR(20),

  `assigned_at` DATETIME,

  CONSTRAINT `fk_student_fee_structure`
    FOREIGN KEY (`fee_structure_id`)
    REFERENCES `fee_structures` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- STUDENT INSTALLMENTS
-- =========================
DROP TABLE IF EXISTS `student_installments`;
CREATE TABLE `student_installments` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `student_fee_id` BIGINT NOT NULL,

  `installment_number` INT,
  `amount` DECIMAL(12,2),

  `paid_amount` DECIMAL(12,2) DEFAULT 0,
  `due_date` DATE,

  `status` VARCHAR(20),
  `is_admission_fee` BOOLEAN,

  CONSTRAINT `fk_student_installment_fee`
    FOREIGN KEY (`student_fee_id`)
    REFERENCES `student_fees` (`id`)
    ON DELETE CASCADE
);

-- =========================
-- STUDENT FEE ALLOCATIONS
-- =========================
DROP TABLE IF EXISTS `student_fee_allocations`;
CREATE TABLE `student_fee_allocations` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `user_id` BIGINT NOT NULL,
  `fee_structure_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,

  `student_name` VARCHAR(255),
  `student_email` VARCHAR(255),

  `course_name` VARCHAR(255),
  `batch_name` VARCHAR(255),

  `original_amount` DECIMAL(12,2) NOT NULL,

  `admin_discount` DECIMAL(12,2),
  `additional_discount` DECIMAL(12,2),
  `total_discount` DECIMAL(12,2),

  `one_time_amount` DECIMAL(12,2),
  `installment_amount` DECIMAL(12,2),

  `admission_fee_amount` DECIMAL(12,2),

  `gst_rate` DECIMAL(5,2),
  `gst_amount` DECIMAL(12,2),

  `payable_amount` DECIMAL(12,2) NOT NULL,
  `advance_payment` DECIMAL(12,2),

  `remaining_amount` DECIMAL(12,2) NOT NULL,

  `currency` VARCHAR(10),

  `applied_promo_code` VARCHAR(100),
  `promo_discount` DECIMAL(12,2),

  `installment_count` INT,
  `duration_months` INT,

  `status` VARCHAR(20),

  `allocation_date` DATE,

  `created_at` DATETIME,
  `updated_at` DATETIME,

  UNIQUE KEY `uk_user_batch` (`user_id`, `batch_id`)
);

-- =========================
-- STUDENT FEE PAYMENTS
-- =========================
DROP TABLE IF EXISTS `student_fee_payments`;
CREATE TABLE `student_fee_payments` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `student_fee_allocation_id` BIGINT NOT NULL,
  `student_installment_plan_id` BIGINT,

  `paid_amount` DECIMAL(12,2) NOT NULL,
  `discount_amount` DECIMAL(12,2),

  `payment_date` DATETIME NOT NULL,

  `payment_mode` VARCHAR(50),
  `payment_status` VARCHAR(50),

  `transaction_reference` VARCHAR(255) NOT NULL,
  `gateway_response` TEXT,

  `screenshot_url` VARCHAR(255),
  `currency` VARCHAR(10),

  `recorded_by` BIGINT,

  `installment_total` DECIMAL(12,2),
  `discount_percentage` DECIMAL(5,2),
  `penalty_amount` DECIMAL(12,2),
  `overdue_remaining` DECIMAL(12,2),

  `installment_ids` TEXT,
  `remarks` TEXT,

  `cashfree_order_id` VARCHAR(255),
  `payment_session_id` VARCHAR(255),

  `gateway_payment_status` VARCHAR(50),
  `signature_verified` BOOLEAN,

  `raw_gateway_response` TEXT,
  `gateway_amount` DECIMAL(12,2),
  `gateway_payment_time` DATETIME,

  UNIQUE KEY `uk_transaction_reference` (`transaction_reference`),
  UNIQUE KEY `uk_cashfree_order` (`cashfree_order_id`)
);

-- =========================
-- STUDENT INSTALLMENT PLANS
-- =========================
DROP TABLE IF EXISTS `student_installment_plans`;
CREATE TABLE `student_installment_plans` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `student_fee_allocation_id` BIGINT NOT NULL,
  `user_id` BIGINT,

  `installment_number` INT NOT NULL,

  `installment_amount` DECIMAL(12,2) NOT NULL,
  `due_date` DATE NOT NULL,

  `paid_amount` DECIMAL(12,2) DEFAULT 0,

  `status` VARCHAR(50) NOT NULL,

  `cashfree_order_id` VARCHAR(255),
  `payment_session_id` VARCHAR(255),

  `link_created_at` DATETIME,
  `link_expiry` DATETIME,

  `label` VARCHAR(255),

  UNIQUE KEY `uk_installment_number` (`student_fee_allocation_id`, `installment_number`)
);

-- =========================
-- STUDENT FEE REFUNDS
-- =========================
DROP TABLE IF EXISTS `student_fee_refunds`;
CREATE TABLE `student_fee_refunds` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `allocation_id` BIGINT NOT NULL,

  `refund_amount` DECIMAL(12,2) NOT NULL,

  `refund_type` VARCHAR(20) NOT NULL,
  `refund_mode` VARCHAR(20) NOT NULL,
  `refund_status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',

  `refund_reason` VARCHAR(255),
  `rejection_reason` VARCHAR(255),

  `approved_by` BIGINT,

  `request_date` DATETIME,
  `processed_date` DATETIME
);

-- =========================
-- SYSTEM SETTINGS
-- =========================
DROP TABLE IF EXISTS `fee_system_settings`;
CREATE TABLE `fee_system_settings` (
  `setting_key` VARCHAR(100) PRIMARY KEY,
  `setting_value` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255)
);
-- =========================
-- BOOK CATEGORIES
-- =========================
DROP TABLE IF EXISTS `book_categories`;
CREATE TABLE `book_categories` (
  `category_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `category_name` VARCHAR(255) NOT NULL UNIQUE,
  `description` VARCHAR(255),

  `status` VARCHAR(20) NOT NULL,
  `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL
);

-- =========================
-- BOOKS
-- =========================
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
  `book_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `title` VARCHAR(255) NOT NULL,
  `author` VARCHAR(255) NOT NULL,

  `publisher` VARCHAR(255),

  `category_id` BIGINT NOT NULL,

  `edition` VARCHAR(100),
  `year` VARCHAR(4),

  `language` VARCHAR(50),

  `access_url` VARCHAR(255),
  `format` VARCHAR(50),
  `digital_type` VARCHAR(50),

  `license_expiry` DATE,
  `usage_limit` INT,

  `type` VARCHAR(20) NOT NULL,

  `isbn` VARCHAR(100) UNIQUE,

  `shelf_location` VARCHAR(255),

  `total_copies` INT NOT NULL,
  `available_copies` INT NOT NULL,

  `status` VARCHAR(20) NOT NULL,

  `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  CONSTRAINT `fk_book_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `book_categories` (`category_id`)
    ON DELETE RESTRICT
);

-- =========================
-- BOOK BARCODES
-- =========================
DROP TABLE IF EXISTS `book_barcodes`;
CREATE TABLE `book_barcodes` (
  `barcode_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `book_id` BIGINT NOT NULL,

  `barcode_value` VARCHAR(255) NOT NULL UNIQUE,

  `is_issued` BOOLEAN NOT NULL DEFAULT FALSE,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  CONSTRAINT `fk_barcode_book`
    FOREIGN KEY (`book_id`)
    REFERENCES `books` (`book_id`)
    ON DELETE CASCADE
);

-- =========================
-- BOOK ISSUE RECORDS
-- =========================
DROP TABLE IF EXISTS `book_issue_records`;
CREATE TABLE `book_issue_records` (
  `issue_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `book_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,

  `user_category` VARCHAR(50),

  `issue_date` DATE NOT NULL,
  `due_date` DATE NOT NULL,
  `return_date` DATE,

  `barcode_value` VARCHAR(255),

  `status` VARCHAR(20) NOT NULL,

  `remarks` VARCHAR(255),

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  CONSTRAINT `fk_issue_book`
    FOREIGN KEY (`book_id`)
    REFERENCES `books` (`book_id`)
    ON DELETE CASCADE
);

-- =========================
-- BOOK RESERVATIONS
-- =========================
DROP TABLE IF EXISTS `book_reservations`;
CREATE TABLE `book_reservations` (
  `reservation_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `book_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,

  `reserved_at` DATE NOT NULL,
  `reservation_date` DATE,
  `reserve_until` DATE,

  `admin_hold_from` DATE,
  `admin_hold_until` DATE,

  `status` VARCHAR(20) NOT NULL,

  `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  CONSTRAINT `fk_reservation_book`
    FOREIGN KEY (`book_id`)
    REFERENCES `books` (`book_id`)
    ON DELETE CASCADE
);

-- =========================
-- LIBRARY SETTINGS
-- =========================
DROP TABLE IF EXISTS `library_settings`;
CREATE TABLE `library_settings` (
  `setting_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `max_books` INT,
  `issue_duration_days` INT,

  `reservation_duration_days` INT,
  `member_role` VARCHAR(50),

  `is_deleted` BOOLEAN DEFAULT FALSE,

  `created_at` DATETIME,
  `updated_at` DATETIME
);

-- =========================
-- FINE SLABS
-- =========================
DROP TABLE IF EXISTS `fine_slabs`;
CREATE TABLE `fine_slabs` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `library_settings_id` BIGINT NOT NULL,

  `member_role` VARCHAR(50),

  `from_day` INT NOT NULL,
  `to_day` INT NOT NULL,

  `fine_per_day` DOUBLE NOT NULL,

  `slab_order` INT,

  CONSTRAINT `fk_fine_library_settings`
    FOREIGN KEY (`library_settings_id`)
    REFERENCES `library_settings` (`setting_id`)
    ON DELETE CASCADE
);

-- =========================
-- LIBRARY FINES
-- =========================
DROP TABLE IF EXISTS `library_fines`;
CREATE TABLE `library_fines` (
  `fine_id` BIGINT AUTO_INCREMENT PRIMARY KEY,

  `issue_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,

  `fine_amount` DOUBLE,

  `paid_status` VARCHAR(20),

  `is_deleted` BOOLEAN DEFAULT FALSE,

  CONSTRAINT `fk_fine_issue`
    FOREIGN KEY (`issue_id`)
    REFERENCES `book_issue_records` (`issue_id`)
    ON DELETE CASCADE
);

-- =========================================
-- DOCUMENT CATEGORIES
-- =========================================
CREATE TABLE IF NOT EXISTS document_categories (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    is_deleted BIT(1) DEFAULT b'0',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id),
    UNIQUE KEY uk_document_categories_category_name (category_name)
);

-- =========================================
-- DOCUMENTS
-- =========================================
CREATE TABLE IF NOT EXISTS documents (
    document_id BIGINT NOT NULL AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    title VARCHAR(255),
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    file_type VARCHAR(100),
    file_size BIGINT,
    uploaded_by BIGINT,
    owner_user_id BIGINT,
    access_level VARCHAR(20) DEFAULT 'PRIVATE',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    is_deleted BIT(1) DEFAULT b'0',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (document_id),
    KEY idx_documents_category_id (category_id),
    CONSTRAINT fk_documents_category
        FOREIGN KEY (category_id) REFERENCES document_categories(category_id)
);

-- =========================================
-- DOCUMENT VERSIONS
-- =========================================
CREATE TABLE IF NOT EXISTS document_versions (
    version_id BIGINT NOT NULL AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    version_number INT,
    file_name VARCHAR(255),
    file_path VARCHAR(500),
    file_size BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (version_id),
    UNIQUE KEY uk_document_versions_doc_version (document_id, version_number),
    KEY idx_document_versions_document_id (document_id),
    CONSTRAINT fk_document_versions_document
        FOREIGN KEY (document_id) REFERENCES documents(document_id)
);

-- =========================================
-- DOCUMENT ACCESS LOGS
-- =========================================
CREATE TABLE IF NOT EXISTS document_access_logs (
    log_id BIGINT NOT NULL AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    user_id BIGINT,
    action VARCHAR(20),
    accessed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (log_id),
    KEY idx_document_access_logs_document_id (document_id),
    CONSTRAINT fk_document_access_logs_document
        FOREIGN KEY (document_id) REFERENCES documents(document_id)
);

-- =========================================
-- DOCUMENT SHARES
-- =========================================
CREATE TABLE IF NOT EXISTS document_shares (
    share_id BIGINT NOT NULL AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    shared_by BIGINT,
    shared_with_user_id BIGINT,
    expiry_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (share_id),
    KEY idx_document_shares_document_id (document_id),
    CONSTRAINT fk_document_shares_document
        FOREIGN KEY (document_id) REFERENCES documents(document_id)
);

-- =========================================
-- HOSTEL
-- =========================================
CREATE TABLE IF NOT EXISTS hostel (
    hostel_id BIGINT NOT NULL AUTO_INCREMENT,
    hostel_name VARCHAR(255) NOT NULL,
    hostel_type VARCHAR(20),
    total_blocks INT DEFAULT 0,
    total_rooms INT DEFAULT 0,
    warden_name VARCHAR(255),
    contact_number VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    is_deleted BIT(1) DEFAULT b'0',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (hostel_id),
    UNIQUE KEY uk_hostel_hostel_name (hostel_name)
);

-- =========================================
-- HOSTEL ROOMS
-- =========================================
CREATE TABLE IF NOT EXISTS hostel_rooms (
    room_id BIGINT NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(100) NOT NULL,
    sharing_type VARCHAR(20) NOT NULL,
    status VARCHAR(30) DEFAULT 'AVAILABLE',
    currently_occupied INT,
    is_deleted BIT(1) DEFAULT b'0',
    PRIMARY KEY (room_id)
);

-- =========================================
-- HOSTEL ATTENDANCE
-- =========================================
CREATE TABLE IF NOT EXISTS hostel_attendance (
    attendance_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT,
    student_name VARCHAR(255) NOT NULL,
    room_id BIGINT NOT NULL,
    room_number VARCHAR(100),
    attendance_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    marked_at DATETIME NOT NULL,
    PRIMARY KEY (attendance_id),
    KEY idx_hostel_attendance_room_id (room_id),
    CONSTRAINT fk_hostel_attendance_room
        FOREIGN KEY (room_id) REFERENCES hostel_rooms(room_id)
);

-- =========================================
-- HOSTEL COMPLAINTS
-- =========================================
CREATE TABLE IF NOT EXISTS hostel_complaints (
    complaint_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT,
    student_name VARCHAR(255) NOT NULL,
    hostel_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    hostel_name VARCHAR(255),
    room_number VARCHAR(100),
    issue_category VARCHAR(30),
    priority VARCHAR(20),
    description VARCHAR(1000),
    reported_date DATE,
    status VARCHAR(30) DEFAULT 'OPEN',
    admin_remarks VARCHAR(1000),
    PRIMARY KEY (complaint_id),
    KEY idx_hostel_complaints_hostel_id (hostel_id),
    KEY idx_hostel_complaints_room_id (room_id),
    CONSTRAINT fk_hostel_complaints_hostel
        FOREIGN KEY (hostel_id) REFERENCES hostel(hostel_id),
    CONSTRAINT fk_hostel_complaints_room
        FOREIGN KEY (room_id) REFERENCES hostel_rooms(room_id)
);

-- =========================================
-- MESS DAY MENU
-- =========================================
CREATE TABLE IF NOT EXISTS mess_day_menu (
    menu_id BIGINT NOT NULL AUTO_INCREMENT,
    day VARCHAR(20),
    breakfast VARCHAR(255),
    lunch VARCHAR(255),
    dinner VARCHAR(255),
    PRIMARY KEY (menu_id)
);

-- =========================================
-- STUDENT HEALTH INCIDENTS
-- =========================================
CREATE TABLE IF NOT EXISTS student_health_incidents (
    incident_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT,
    student_name VARCHAR(255),
    student_phone VARCHAR(50),
    parent_phone VARCHAR(50),
    hostel_id BIGINT,
    room_id BIGINT,
    hostel_name VARCHAR(255),
    room_number VARCHAR(100),
    complaint_nature VARCHAR(30) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    current_status VARCHAR(30),
    reported_date DATE NOT NULL,
    clinical_notes VARCHAR(255),
    is_deleted BIT(1) DEFAULT b'0',
    PRIMARY KEY (incident_id),
    KEY idx_student_health_incidents_hostel_id (hostel_id),
    KEY idx_student_health_incidents_room_id (room_id),
    CONSTRAINT fk_student_health_incidents_hostel
        FOREIGN KEY (hostel_id) REFERENCES hostel(hostel_id),
    CONSTRAINT fk_student_health_incidents_room
        FOREIGN KEY (room_id) REFERENCES hostel_rooms(room_id)
);

-- =========================================
-- STUDENT HOSTEL ALLOCATIONS
-- =========================================
CREATE TABLE IF NOT EXISTS student_hostel_allocations (
    allocation_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    student_name VARCHAR(255),
    student_email VARCHAR(255),
    parent_name VARCHAR(255),
    parent_phone VARCHAR(50),
    hostel_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    hostel_name VARCHAR(255),
    room_number VARCHAR(100),
    join_date DATE,
    leave_date DATE,
    status VARCHAR(30) DEFAULT 'ACTIVE',
    PRIMARY KEY (allocation_id),
    KEY idx_student_hostel_allocations_hostel_id (hostel_id),
    KEY idx_student_hostel_allocations_room_id (room_id),
    CONSTRAINT fk_student_hostel_allocations_hostel
        FOREIGN KEY (hostel_id) REFERENCES hostel(hostel_id),
    CONSTRAINT fk_student_hostel_allocations_room
        FOREIGN KEY (room_id) REFERENCES hostel_rooms(room_id)
);

-- =========================================
-- STUDENT HOSTEL FEES
-- =========================================
CREATE TABLE IF NOT EXISTS student_Hostel_fees (
    fee_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT,
    student_name VARCHAR(255) NOT NULL,
    monthly_fee DOUBLE NOT NULL,
    total_fee DOUBLE NOT NULL,
    amount_paid DOUBLE NOT NULL,
    due_amount DOUBLE NOT NULL,
    last_payment_date DATE,
    status VARCHAR(30) NOT NULL,
    PRIMARY KEY (fee_id)
);

-- =========================================
-- STUDENT VISIT ENTRY
-- =========================================
CREATE TABLE IF NOT EXISTS student_visit_entry (
    visit_id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT,
    student_name VARCHAR(255) NOT NULL,
    visitor_name VARCHAR(255) NOT NULL,
    relationship VARCHAR(50),
    visitor_contact VARCHAR(50),
    visit_date DATE NOT NULL,
    visit_time TIME NOT NULL,
    purpose_of_visit VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    created_at DATE DEFAULT (CURRENT_DATE),
    PRIMARY KEY (visit_id)
);

-- =========================================
-- BOOK CATEGORIES
-- =========================================
CREATE TABLE IF NOT EXISTS book_categories (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_deleted BIT(1) NOT NULL DEFAULT b'0',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id)
);

-- =========================================
-- BOOKS
-- =========================================
CREATE TABLE IF NOT EXISTS books (
    book_id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    category_id BIGINT NOT NULL,
    edition VARCHAR(255),
    year VARCHAR(4),
    language VARCHAR(100),
    access_url VARCHAR(500),
    format VARCHAR(100),
    digital_type VARCHAR(100),
    type VARCHAR(20) NOT NULL DEFAULT 'PHYSICAL',
    isbn VARCHAR(255),
    shelf_location VARCHAR(255),
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    is_deleted BIT(1) NOT NULL DEFAULT b'0',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (book_id),
    UNIQUE KEY uk_books_isbn (isbn),
    KEY idx_books_category_id (category_id),
    CONSTRAINT fk_books_category
        FOREIGN KEY (category_id) REFERENCES book_categories(category_id)
);

-- =========================================
-- BOOK BARCODES
-- =========================================
CREATE TABLE IF NOT EXISTS book_barcodes (
    barcode_id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    barcode_value VARCHAR(255) NOT NULL,
    is_issued BIT(1) NOT NULL DEFAULT b'0',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (barcode_id),
    KEY idx_book_barcodes_book_id (book_id),
    CONSTRAINT fk_book_barcodes_book
        FOREIGN KEY (book_id) REFERENCES books(book_id)
);

-- =========================================
-- BOOK ISSUE RECORDS
-- =========================================
CREATE TABLE IF NOT EXISTS book_issue_records (
    issue_id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    user_category VARCHAR(100),
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    barcode_value VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ISSUED',
    remarks VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (issue_id),
    KEY idx_book_issue_records_book_id (book_id),
    CONSTRAINT fk_book_issue_records_book
        FOREIGN KEY (book_id) REFERENCES books(book_id)
);

-- =========================================
-- BOOK RESERVATIONS
-- =========================================
CREATE TABLE IF NOT EXISTS book_reservations (
    reservation_id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reserved_at DATE NOT NULL,
    reservation_date DATE,
    reserve_until DATE,
    admin_hold_from DATE,
    admin_hold_until DATE,
    status VARCHAR(20) NOT NULL,
    is_deleted BIT(1) NOT NULL DEFAULT b'0',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_id),
    KEY idx_book_reservations_book_id (book_id),
    CONSTRAINT fk_book_reservations_book
        FOREIGN KEY (book_id) REFERENCES books(book_id)
);

CREATE TABLE IF NOT EXISTS library_settings (
    setting_id BIGINT NOT NULL AUTO_INCREMENT,
    max_books INT,
    issue_duration_days INT,
    reservation_duration_days INT,
    member_role VARCHAR(100),
    is_deleted BIT(1) DEFAULT b'0',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (setting_id)
);

CREATE TABLE IF NOT EXISTS fine_slabs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    library_settings_id BIGINT NOT NULL,
    member_role VARCHAR(100),
    from_day INT NOT NULL,
    to_day INT NOT NULL,
    fine_per_day DOUBLE NOT NULL,
    slab_order INT,
    PRIMARY KEY (id),
    KEY idx_fine_slabs_library_settings_id (library_settings_id),
    CONSTRAINT fk_fine_slabs_library_settings
        FOREIGN KEY (library_settings_id) REFERENCES library_settings(setting_id)
);

CREATE TABLE IF NOT EXISTS library_members (
    member_id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255) NOT NULL,
    is_deleted BIT(1) DEFAULT b'0',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    member_type VARCHAR(50) NOT NULL,
    student_id VARCHAR(255),
    department VARCHAR(255),
    max_books_allowed INT DEFAULT 3,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    PRIMARY KEY (member_id),
    UNIQUE KEY uk_library_members_email (email),
    UNIQUE KEY uk_library_members_mobile_number (mobile_number),
    UNIQUE KEY uk_library_members_student_id (student_id)
);

CREATE TABLE IF NOT EXISTS library_fines (
    fine_id BIGINT NOT NULL AUTO_INCREMENT,
    issue_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    fine_amount DOUBLE,
    paid_status VARCHAR(20) DEFAULT 'UNPAID',
    is_deleted BIT(1) DEFAULT b'0',
    PRIMARY KEY (fine_id),
    KEY idx_library_fines_issue_id (issue_id),
    CONSTRAINT fk_library_fines_issue_record
        FOREIGN KEY (issue_id) REFERENCES book_issue_records(issue_id)
);

CREATE TABLE IF NOT EXISTS automation_rules (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    trigger_type VARCHAR(50),
    action_type VARCHAR(50),
    delay_in_minutes INT,
    active BIT(1) DEFAULT b'1',
    created_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS blogs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    content TEXT,
    short_description VARCHAR(255),
    thumbnail_url VARCHAR(500),
    meta_title VARCHAR(255),
    meta_description VARCHAR(255),
    meta_keywords VARCHAR(255),
    status VARCHAR(20) DEFAULT 'DRAFT',
    publish_at DATETIME,
    featured BIT(1) DEFAULT b'0',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_blogs_slug (slug)
);

CREATE TABLE IF NOT EXISTS blog_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    home_head_script TEXT,
    home_body_script TEXT,
    blog_head_script TEXT,
    blog_body_script TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS marketing_campaign (
    id BIGINT NOT NULL AUTO_INCREMENT,
    campaign_name VARCHAR(255) NOT NULL,
    channel VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(50) NOT NULL,
    country VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS marketing_content (
    content_id INT NOT NULL AUTO_INCREMENT,
    content_title VARCHAR(150) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    content_url VARCHAR(255),
    created_date DATE,
    campaign_id BIGINT NOT NULL,
    PRIMARY KEY (content_id),
    KEY idx_marketing_content_campaign_id (campaign_id),
    CONSTRAINT fk_marketing_content_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaign(id)
);

CREATE TABLE IF NOT EXISTS messenger_schedules (
    id BIGINT NOT NULL AUTO_INCREMENT,
    campaign_id BIGINT NOT NULL,
    audience_type VARCHAR(50) NOT NULL,
    send_type VARCHAR(30) NOT NULL,
    send_date DATE,
    send_time TIME,
    recurring_type VARCHAR(20),
    notification_title VARCHAR(255) NOT NULL,
    message VARCHAR(3000) NOT NULL,
    custom_icon VARCHAR(500),
    custom_image_url VARCHAR(500),
    target_link VARCHAR(1000),
    schedule_id BIGINT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_messenger_schedules_campaign_id (campaign_id),
    KEY idx_messenger_schedules_schedule_id (schedule_id),
    CONSTRAINT fk_messenger_schedules_campaign
        FOREIGN KEY (campaign_id) REFERENCES marketing_campaign(id),
    CONSTRAINT fk_messenger_schedules_self
        FOREIGN KEY (schedule_id) REFERENCES messenger_schedules(id)
);

CREATE TABLE IF NOT EXISTS campaign_analytics (
    id BIGINT NOT NULL AUTO_INCREMENT,
    campaign_id BIGINT,
    total_sent BIGINT,
    total_opened BIGINT,
    total_clicked BIGINT,
    total_converted BIGINT,
    revenue_generated DECIMAL(19,2),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS click_tracking (
    id BIGINT NOT NULL AUTO_INCREMENT,
    campaign_id BIGINT,
    user_id BIGINT,
    clicked_url VARCHAR(1000),
    clicked_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS customer (
    customer_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(15),
    password VARCHAR(255) NOT NULL,
    location VARCHAR(100),
    created_date DATE,
    PRIMARY KEY (customer_id),
    UNIQUE KEY uk_customer_email (email)
);

CREATE TABLE IF NOT EXISTS delivery_logs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    campaign_id BIGINT,
    user_id BIGINT,
    delivered BIT(1),
    opened BIT(1),
    clicked BIT(1),
    sent_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS promo_codes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    product_id BIGINT,
    discount_type VARCHAR(30),
    discount_value DECIMAL(19,2),
    maximum_discount_value DECIMAL(19,2),
    minimum_product_value DECIMAL(19,2),
    max_use_count INT,
    used_count INT DEFAULT 0,
    eligible_for_enrolled_only BIT(1) DEFAULT b'0',
    valid_till DATE,
    active BIT(1) DEFAULT b'1',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_promo_codes_code (code)
);

CREATE TABLE IF NOT EXISTS promo_eligible_courses (
    promo_id BIGINT NOT NULL,
    course_id BIGINT,
    KEY idx_promo_eligible_courses_promo_id (promo_id),
    CONSTRAINT fk_promo_eligible_courses_promo
        FOREIGN KEY (promo_id) REFERENCES promo_codes(id)
);

CREATE TABLE IF NOT EXISTS referral_fraud_logs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    ip_address VARCHAR(255),
    suspicious BIT(1),
    detected_at DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS referral_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    enabled BIT(1) DEFAULT b'0',
    max_referral_count INT,
    credit_validity_days INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS route_way (
    id BIGINT NOT NULL AUTO_INCREMENT,
    route_code BIGINT,
    route_name VARCHAR(255) NOT NULL,
    distance_km DOUBLE,
    estimated_time_minutes INT,
    active BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS route_pickup_points (
    route_id BIGINT NOT NULL,
    pickup_point VARCHAR(255),
    KEY idx_route_pickup_points_route_id (route_id),
    CONSTRAINT fk_route_pickup_points_route
        FOREIGN KEY (route_id) REFERENCES route_way(id)
);

CREATE TABLE IF NOT EXISTS route_drop_points (
    route_id BIGINT NOT NULL,
    drop_point VARCHAR(255),
    KEY idx_route_drop_points_route_id (route_id),
    CONSTRAINT fk_route_drop_points_route
        FOREIGN KEY (route_id) REFERENCES route_way(id)
);

CREATE TABLE IF NOT EXISTS Vehicles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vehicle_number VARCHAR(255) NOT NULL,
    vehicletype VARCHAR(20),
    capacity INT,
    occupied_seats INT,
    vehicle_status VARCHAR(30),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    route_id BIGINT,
    gps_enabled BIT(1),
    PRIMARY KEY (id),
    UNIQUE KEY uk_vehicles_vehicle_number (vehicle_number),
    KEY idx_vehicles_route_id (route_id),
    CONSTRAINT fk_vehicles_route
        FOREIGN KEY (route_id) REFERENCES route_way(id)
);

CREATE TABLE IF NOT EXISTS conductors (
    conductor_id BIGINT NOT NULL AUTO_INCREMENT,
    conductor_name VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255) NOT NULL,
    experience_years INT NOT NULL,
    route_id BIGINT,
    vehicle_id BIGINT,
    verification_status VARCHAR(30) NOT NULL,
    active BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (conductor_id),
    UNIQUE KEY uk_conductors_contact_number (contact_number),
    KEY idx_conductors_route_id (route_id),
    KEY idx_conductors_vehicle_id (vehicle_id),
    CONSTRAINT fk_conductors_route
        FOREIGN KEY (route_id) REFERENCES route_way(id),
    CONSTRAINT fk_conductors_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES Vehicles(id)
);

CREATE TABLE IF NOT EXISTS drivers (
    driver_id BIGINT NOT NULL AUTO_INCREMENT,
    fullname VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255) NOT NULL,
    license_number VARCHAR(255) NOT NULL,
    license_expiry_date DATE NOT NULL,
    role VARCHAR(30),
    experience_category VARCHAR(50),
    background_verified BIT(1) NOT NULL DEFAULT b'0',
    shift VARCHAR(20),
    experience_years INT NOT NULL,
    license_validity_status VARCHAR(30),
    verification_status VARCHAR(30) NOT NULL,
    active BIT(1) NOT NULL DEFAULT b'1',
    vehicle_id BIGINT,
    route_id BIGINT,
    conductor_id BIGINT,
    PRIMARY KEY (driver_id),
    UNIQUE KEY uk_drivers_contact_number (contact_number),
    UNIQUE KEY uk_drivers_license_number (license_number),
    KEY idx_drivers_vehicle_id (vehicle_id),
    KEY idx_drivers_route_id (route_id),
    KEY idx_drivers_conductor_id (conductor_id),
    CONSTRAINT fk_drivers_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES Vehicles(id),
    CONSTRAINT fk_drivers_route
        FOREIGN KEY (route_id) REFERENCES route_way(id),
    CONSTRAINT fk_drivers_conductor
        FOREIGN KEY (conductor_id) REFERENCES conductors(conductor_id)
);

CREATE TABLE IF NOT EXISTS fuel_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vehicle_id VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    quantity DOUBLE NOT NULL,
    cost DOUBLE NOT NULL,
    odo BIGINT NOT NULL,
    station VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS student_transport_assignment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    route_id BIGINT,
    pickup_point VARCHAR(100) NOT NULL,
    drop_point VARCHAR(100) NOT NULL,
    shift VARCHAR(20),
    PRIMARY KEY (id),
    KEY idx_student_transport_assignment_vehicle_id (vehicle_id),
    KEY idx_student_transport_assignment_route_id (route_id),
    CONSTRAINT fk_student_transport_assignment_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES Vehicles(id),
    CONSTRAINT fk_student_transport_assignment_route
        FOREIGN KEY (route_id) REFERENCES route_way(id)
);

CREATE TABLE IF NOT EXISTS transport_attendance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    marked_by VARCHAR(20) NOT NULL,
    route_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    pickup_status VARCHAR(20) NOT NULL,
    drop_status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_transport_attendance_route_id (route_id),
    KEY idx_transport_attendance_vehicle_id (vehicle_id),
    CONSTRAINT fk_transport_attendance_route
        FOREIGN KEY (route_id) REFERENCES route_way(id),
    CONSTRAINT fk_transport_attendance_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES Vehicles(id)
);

CREATE TABLE IF NOT EXISTS transport_fee_structure (
    id BIGINT NOT NULL AUTO_INCREMENT,
    route_id BIGINT NOT NULL,
    annual_fee DECIMAL(10,2) NOT NULL,
    academic_year VARCHAR(10),
    PRIMARY KEY (id),
    UNIQUE KEY uk_transport_fee_structure_route_id (route_id)
);

CREATE TABLE IF NOT EXISTS transport_payments (
    id VARCHAR(255) NOT NULL,
    student_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_mode VARCHAR(50) NOT NULL,
    reference_no VARCHAR(50),
    remarks TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transport_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    key_name VARCHAR(255),
    value VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_transport_settings_key_name (key_name)
);

CREATE TABLE IF NOT EXISTS vehicle_gps (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vehicle_id BIGINT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    speed DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_vehicle_timestamp (vehicle_id, timestamp),
    CONSTRAINT fk_vehicle_gps_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES Vehicles(id)
);

CREATE TABLE IF NOT EXISTS vehicle_maintenance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    vehicle_id VARCHAR(255) NOT NULL,
    type VARCHAR(30) NOT NULL,
    date DATE NOT NULL,
    cost DOUBLE NOT NULL,
    description VARCHAR(500),
    status VARCHAR(30) NOT NULL,
    next_due DATE,
    PRIMARY KEY (id)
);

-- =====================
-- approvals
-- =====================
DROP TABLE IF EXISTS `approvals`;
CREATE TABLE `approvals` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `request_type` VARCHAR(100) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `requested_by` VARCHAR(255) DEFAULT NULL,
  `status` VARCHAR(50) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- attendance_alert_flag
-- =====================
DROP TABLE IF EXISTS `attendance_alert_flag`;
CREATE TABLE `attendance_alert_flag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `student_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,
  `flag_type` VARCHAR(50) NOT NULL,

  `status` VARCHAR(20) NOT NULL,
  `message` VARCHAR(500) DEFAULT NULL,

  `created_at` DATETIME NOT NULL,
  `resolved_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_attendance_alert_flag`
  (`student_id`, `course_id`, `batch_id`, `flag_type`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- attendance_config
-- =====================
DROP TABLE IF EXISTS `attendance_config`;
CREATE TABLE `attendance_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `exam_eligibility_percent` INT NOT NULL,
  `at_risk_percent` INT NOT NULL,

  `late_grace_minutes` INT NOT NULL,
  `min_presence_minutes` INT NOT NULL,
  `auto_absent_minutes` INT NOT NULL,

  `early_exit_action` VARCHAR(50) NOT NULL,

  `allow_offline` TINYINT(1) NOT NULL,
  `allow_manual_override` TINYINT(1) NOT NULL,
  `require_override_reason` TINYINT(1) NOT NULL,
  `notify_parents` TINYINT(1) NOT NULL,
  `one_device_per_session` TINYINT(1) NOT NULL,
  `log_ip_address` TINYINT(1) NOT NULL,
  `strict_start` TINYINT(1) NOT NULL,
  `qr_code_mode` VARCHAR(50) NOT NULL,

  `grace_period_minutes` INT NOT NULL,
  `consecutive_absence_limit` INT NOT NULL,

  `created_by` BIGINT NOT NULL,
  `updated_by` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_attendance_config`
  (`course_id`, `batch_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- attendance_offline_queue
-- =====================
DROP TABLE IF EXISTS `attendance_offline_queue`;
CREATE TABLE `attendance_offline_queue` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `session_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,
  `student_id` BIGINT NOT NULL,

  `status` VARCHAR(50) NOT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,

  `queued_at` DATETIME NOT NULL,
  `synced` TINYINT(1) NOT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- attendance_record
-- =====================
DROP TABLE IF EXISTS `attendance_record`;
CREATE TABLE `attendance_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `attendance_session_id` BIGINT NOT NULL,
  `batch_id` BIGINT DEFAULT NULL,

  `status` VARCHAR(50) NOT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,
  `late_minutes` INT DEFAULT NULL,
  `left_at` DATETIME DEFAULT NULL,

  `marked_by` BIGINT NOT NULL,
  `marked_at` DATETIME NOT NULL,

  `attendance_date` DATE NOT NULL,
  `source` VARCHAR(50) NOT NULL,

  `student_id` BIGINT DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_attendance_record`
  (`batch_id`, `student_id`, `attendance_date`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- attendance_session
-- =====================
DROP TABLE IF EXISTS `attendance_session`;
CREATE TABLE `attendance_session` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `session_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,

  `status` VARCHAR(50) NOT NULL,

  `started_at` DATETIME NOT NULL,
  `ended_at` DATETIME DEFAULT NULL,

  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_attendance_session`
  (`session_id`, `batch_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- attendance_upload_job
-- =====================
DROP TABLE IF EXISTS `attendance_upload_job`;
CREATE TABLE `attendance_upload_job` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,
  `session_id` BIGINT DEFAULT NULL,

  `attendance_date` DATE NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,

  `status` VARCHAR(50) NOT NULL,

  `uploaded_by` BIGINT NOT NULL,
  `uploaded_at` DATETIME NOT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- automation_controls
-- =====================
DROP TABLE IF EXISTS `automation_controls`;
CREATE TABLE `automation_controls` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `rule_name` VARCHAR(255) NOT NULL,
  `is_enabled` TINYINT(1) DEFAULT NULL,
  `last_run_date` DATETIME DEFAULT NULL,
  `action_count` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_name` (`rule_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- batch
-- =====================
DROP TABLE IF EXISTS `batch`;
CREATE TABLE `batch` (
  `batch_id` BIGINT NOT NULL AUTO_INCREMENT,

  `course_id` BIGINT NOT NULL,
  `batch_name` VARCHAR(255) NOT NULL,
  `trainer_id` BIGINT NOT NULL,
  `trainer_name` VARCHAR(255) NOT NULL,

  `start_date` DATE NOT NULL,
  `end_date` DATE DEFAULT NULL,

  `max_students` INT DEFAULT NULL,

  `free_batch` TINYINT(1) NOT NULL,
  `fee` DOUBLE DEFAULT NULL,

  `content_access` TINYINT(1) NOT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`batch_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- certificate
-- =====================
DROP TABLE IF EXISTS `certificate`;
CREATE TABLE `certificate` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `certificate_id` VARCHAR(255) NOT NULL,
  `verification_token` VARCHAR(255) NOT NULL,

  `user_id` BIGINT NOT NULL,
  `student_name` VARCHAR(255) NOT NULL,
  `student_email` VARCHAR(255) NOT NULL,

  `target_type` VARCHAR(50) NOT NULL,
  `target_id` BIGINT NOT NULL,

  `event_title` VARCHAR(255) NOT NULL,

  `template_id` BIGINT DEFAULT NULL,

  `score` DOUBLE DEFAULT NULL,

  `issued_date` DATETIME NOT NULL,
  `expiry_date` DATETIME DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `revoked_reason` VARCHAR(255) DEFAULT NULL,
  `revoked_at` DATETIME DEFAULT NULL,

  `version` INT NOT NULL,

  `pdf_url` VARCHAR(255) DEFAULT NULL,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uq_certificate_id` (`certificate_id`),
  UNIQUE KEY `uq_verification_token` (`verification_token`),
  UNIQUE KEY `uq_user_target` (`user_id`, `target_type`, `target_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- certificate_audit_log
-- =====================
DROP TABLE IF EXISTS `certificate_audit_log`;
CREATE TABLE `certificate_audit_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `certificate_id` BIGINT NOT NULL,
  `action` VARCHAR(50) NOT NULL,
  `performed_by` BIGINT DEFAULT NULL,
  `action_date` DATETIME NOT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- certificate_progress
-- =====================
DROP TABLE IF EXISTS `certificate_progress`;
CREATE TABLE `certificate_progress` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `user_id` BIGINT NOT NULL,
  `target_type` VARCHAR(50) NOT NULL,
  `target_id` BIGINT NOT NULL,

  `completion_percent` DOUBLE DEFAULT NULL,
  `score` DOUBLE DEFAULT NULL,
  `attendance_percent` DOUBLE DEFAULT NULL,
  `submission_completed` TINYINT(1) DEFAULT NULL,

  `eligibility_status` VARCHAR(50) NOT NULL,
  `updated_at` DATETIME NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uq_user_target_progress`
  (`user_id`, `target_type`, `target_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- certificate_renewal
-- =====================
DROP TABLE IF EXISTS `certificate_renewal`;
CREATE TABLE `certificate_renewal` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `certificate_id` BIGINT NOT NULL,
  `previous_expiry` DATETIME DEFAULT NULL,
  `new_expiry` DATETIME NOT NULL,
  `renewed_on` DATETIME NOT NULL,
  `renewed_by` BIGINT DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- certificate_requests
-- =====================
DROP TABLE IF EXISTS `certificate_requests`;
CREATE TABLE `certificate_requests` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `user_id` BIGINT NOT NULL,
  `student_name` VARCHAR(255) NOT NULL,
  `student_email` VARCHAR(255) NOT NULL,

  `target_type` VARCHAR(50) NOT NULL,
  `target_id` BIGINT NOT NULL,
  `event_title` VARCHAR(255) NOT NULL,

  `score` DOUBLE DEFAULT NULL,

  `status` VARCHAR(50) NOT NULL,
  `request_date` DATETIME NOT NULL,

  `reviewed_by` BIGINT DEFAULT NULL,
  `reviewed_at` DATETIME DEFAULT NULL,

  `certificate_id` BIGINT DEFAULT NULL,

  `admin_comment` TEXT,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- certificate_rule
-- =====================
DROP TABLE IF EXISTS `certificate_rule`;
CREATE TABLE `certificate_rule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `target_type` VARCHAR(50) NOT NULL,
  `target_id` BIGINT NOT NULL,

  `score_required` TINYINT(1) DEFAULT NULL,
  `required_score` DECIMAL(5,2) DEFAULT NULL,

  `attendance_required` TINYINT(1) DEFAULT NULL,
  `min_attendance` DECIMAL(5,2) DEFAULT NULL,

  `is_enabled` TINYINT(1) DEFAULT NULL,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uq_rule`
  (`target_type`, `target_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- certificate_template
-- =====================
DROP TABLE IF EXISTS `certificate_template`;
CREATE TABLE `certificate_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `template_name` VARCHAR(255) NOT NULL,
  `template_type` VARCHAR(255) DEFAULT NULL,

  `target_type` VARCHAR(50) DEFAULT NULL,
  `target_id` BIGINT DEFAULT NULL,

  `template_file_url` TEXT,
  `logo_url` VARCHAR(255) DEFAULT NULL,
  `background_image_url` VARCHAR(255) DEFAULT NULL,
  `signature_url` VARCHAR(255) DEFAULT NULL,

  `is_active` TINYINT(1) NOT NULL,

  `layout_config_json` TEXT,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =====================
-- coding_execution_result
-- =====================
DROP TABLE IF EXISTS `coding_execution_result`;
CREATE TABLE `coding_execution_result` (
  `execution_id` BIGINT NOT NULL AUTO_INCREMENT,

  `response_id` BIGINT NOT NULL,
  `test_case_id` BIGINT NOT NULL,

  `actual_output` TEXT,

  `passed` TINYINT(1) NOT NULL,

  `execution_status` VARCHAR(20) NOT NULL,

  `execution_time_ms` BIGINT DEFAULT NULL,

  `error_message` TEXT,

  PRIMARY KEY (`execution_id`),

  KEY `idx_response` (`response_id`),
  KEY `idx_testcase` (`test_case_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- coding_test_case
-- =====================
DROP TABLE IF EXISTS `coding_test_case`;
CREATE TABLE `coding_test_case` (
  `test_case_id` BIGINT NOT NULL AUTO_INCREMENT,

  `question_id` BIGINT NOT NULL,

  `input_data` TEXT NOT NULL,
  `expected_output` TEXT NOT NULL,

  `hidden` TINYINT(1) NOT NULL,

  PRIMARY KEY (`test_case_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- courses
-- =====================
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses` (
  `course_id` BIGINT NOT NULL AUTO_INCREMENT,

  `course_name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `duration` VARCHAR(255) DEFAULT NULL,
  `tools_covered` VARCHAR(255) DEFAULT NULL,
  `course_fee` DOUBLE DEFAULT NULL,

  `certificate_provided` TINYINT(1) DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `show_validity` TINYINT(1) DEFAULT NULL,
  `validity_in_days` INT DEFAULT NULL,

  `allow_offline_mobile` TINYINT(1) DEFAULT NULL,
  `allow_bookmark` TINYINT(1) DEFAULT NULL,

  `course_image_url` VARCHAR(255) DEFAULT NULL,

  `share_code` VARCHAR(255) DEFAULT NULL,
  `share_enabled` TINYINT(1) DEFAULT NULL,

  `certificate_template_id` BIGINT DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  `course_title` VARCHAR(255) DEFAULT NULL,
  `course_description` VARCHAR(255) DEFAULT NULL,

  PRIMARY KEY (`course_id`),

  UNIQUE KEY `uk_course_name` (`course_name`),
  UNIQUE KEY `uk_share_code` (`share_code`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- course_batch_stats
-- =====================
DROP TABLE IF EXISTS `course_batch_stats`;
CREATE TABLE `course_batch_stats` (
  `course_id` BIGINT NOT NULL,

  `total_batches` INT DEFAULT NULL,
  `running_batches` INT DEFAULT NULL,
  `upcoming_batches` INT DEFAULT NULL,
  `completed_batches` INT DEFAULT NULL,
  `parallel_batches` INT DEFAULT NULL,
  `required_trainers` INT DEFAULT NULL,

  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`course_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- course_inventory_mapping
-- =====================
DROP TABLE IF EXISTS `course_inventory_mapping`;
CREATE TABLE `course_inventory_mapping` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `course_id` BIGINT DEFAULT NULL,
  `item_id` BIGINT DEFAULT NULL,

  `mandatory` TINYINT(1) DEFAULT NULL,
  `quantity_required` INT DEFAULT NULL,
  `auto_reserve` TINYINT(1) DEFAULT NULL,

  `price` DOUBLE DEFAULT NULL,
  `refundable` TINYINT(1) DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- digital_assets
-- =====================
DROP TABLE IF EXISTS `digital_assets`;
CREATE TABLE `digital_assets` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `software_name` VARCHAR(255) DEFAULT NULL,
  `license_key` VARCHAR(255) DEFAULT NULL,

  `total_licenses` INT DEFAULT NULL,
  `used_licenses` INT DEFAULT NULL,
  `available_licenses` INT DEFAULT NULL,

  `assigned_to` BIGINT DEFAULT NULL,
  `vendor_id` BIGINT DEFAULT NULL,

  `cost_per_license` DOUBLE DEFAULT NULL,

  `activation_date` DATE DEFAULT NULL,
  `expiry_date` DATE DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_license_key` (`license_key`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;






-- =====================
-- exam
-- =====================
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `exam_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_type` VARCHAR(50) NOT NULL,
  `title` VARCHAR(255) NOT NULL,

  `total_marks` INT NOT NULL,
  `pass_percentage` DOUBLE NOT NULL,
  `duration_minutes` INT NOT NULL,

  `status` VARCHAR(50) NOT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `is_deleted` TINYINT(1) NOT NULL,

  `created_by` BIGINT NOT NULL,

  `course_id` BIGINT DEFAULT NULL,
  `batch_id` BIGINT DEFAULT NULL,

  `start_time` DATETIME DEFAULT NULL,

  `certificate_template_id` BIGINT DEFAULT NULL,
  `certificate_enabled` TINYINT(1) NOT NULL,

  PRIMARY KEY (`exam_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- exam_attempt
-- =====================
DROP TABLE IF EXISTS `exam_attempt`;
CREATE TABLE `exam_attempt` (
  `attempt_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,
  `student_id` BIGINT NOT NULL,
  `attempt_number` INT NOT NULL,

  `start_time` DATETIME DEFAULT NULL,
  `end_time` DATETIME DEFAULT NULL,

  `status` VARCHAR(50) NOT NULL,
  `score` DOUBLE DEFAULT NULL,

  PRIMARY KEY (`attempt_id`),

  UNIQUE KEY `uk_exam_attempt`
  (`exam_id`, `student_id`, `attempt_number`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





-- =====================
-- exam_design
-- =====================
DROP TABLE IF EXISTS `exam_design`;
CREATE TABLE `exam_design` (
  `design_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,

  `orientation` VARCHAR(50) NOT NULL,

  `institute_logo_path` VARCHAR(255) DEFAULT NULL,
  `background_image_path` VARCHAR(255) DEFAULT NULL,

  `watermark_type` VARCHAR(50) DEFAULT NULL,
  `watermark_value` VARCHAR(255) DEFAULT NULL,
  `watermark_opacity` INT DEFAULT NULL,

  PRIMARY KEY (`design_id`),

  UNIQUE KEY `uk_exam_design`
  (`exam_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- exam_evaluation_log
-- =====================
DROP TABLE IF EXISTS `exam_evaluation_log`;
CREATE TABLE `exam_evaluation_log` (
  `log_id` BIGINT NOT NULL AUTO_INCREMENT,

  `attempt_id` BIGINT NOT NULL,
  `evaluator_id` BIGINT NOT NULL,

  `old_score` DOUBLE NOT NULL,
  `new_score` DOUBLE NOT NULL,

  `reason` VARCHAR(255) NOT NULL,

  `updated_at` DATETIME NOT NULL,

  PRIMARY KEY (`log_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- exam_grading
-- =====================
DROP TABLE IF EXISTS `exam_grading`;
CREATE TABLE `exam_grading` (
  `grading_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,

  `auto_evaluation` TINYINT(1) NOT NULL,
  `partial_marking` TINYINT(1) NOT NULL,
  `show_result` TINYINT(1) NOT NULL,
  `show_rank` TINYINT(1) NOT NULL,
  `show_percentile` TINYINT(1) NOT NULL,

  PRIMARY KEY (`grading_id`),

  UNIQUE KEY `uk_exam_grading`
  (`exam_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- exam_notification
-- =====================
DROP TABLE IF EXISTS `exam_notification`;
CREATE TABLE `exam_notification` (
  `notification_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,

  `scheduled_notification` TINYINT(1) NOT NULL,
  `reminder_before` VARCHAR(50) NOT NULL,
  `feedback_after_exam` TINYINT(1) NOT NULL,

  PRIMARY KEY (`notification_id`),

  UNIQUE KEY `uk_exam_notification`
  (`exam_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





-- =====================
-- exam_proctoring
-- =====================
DROP TABLE IF EXISTS `exam_proctoring`;
CREATE TABLE `exam_proctoring` (
  `proctoring_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,

  `enabled` TINYINT(1) NOT NULL,
  `camera_required` TINYINT(1) NOT NULL,
  `system_check_required` TINYINT(1) NOT NULL,
  `violation_limit` INT NOT NULL,

  PRIMARY KEY (`proctoring_id`),

  UNIQUE KEY `uk_exam_proctoring`
  (`exam_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;






-- =====================
-- exam_question
-- =====================
DROP TABLE IF EXISTS `exam_question`;
CREATE TABLE `exam_question` (
  `exam_question_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_section_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,

  `marks` DOUBLE NOT NULL,
  `question_order` INT NOT NULL,

  PRIMARY KEY (`exam_question_id`),

  UNIQUE KEY `uk_exam_question`
  (`exam_section_id`, `question_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =====================
-- exam_response
-- =====================
DROP TABLE IF EXISTS `exam_response`;
CREATE TABLE `exam_response` (
  `response_id` BIGINT NOT NULL AUTO_INCREMENT,

  `attempt_id` BIGINT NOT NULL,
  `exam_question_id` BIGINT NOT NULL,

  `selected_option_id` BIGINT DEFAULT NULL,

  `descriptive_answer` TEXT,

  `coding_submission_code` LONGTEXT,

  `marks_awarded` DOUBLE DEFAULT NULL,
  `evaluation_type` VARCHAR(50) DEFAULT NULL,

  PRIMARY KEY (`response_id`),

  UNIQUE KEY `uk_exam_response`
  (`attempt_id`, `exam_question_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- exam_schedule
-- =====================
DROP TABLE IF EXISTS `exam_schedule`;
CREATE TABLE `exam_schedule` (
  `schedule_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `start_time` DATETIME DEFAULT NULL,
  `end_time` DATETIME DEFAULT NULL,

  `is_active` TINYINT(1) NOT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`schedule_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- exam_section
-- =====================
DROP TABLE IF EXISTS `exam_section`;
CREATE TABLE `exam_section` (
  `exam_section_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,
  `section_id` BIGINT NOT NULL,

  `section_order` INT DEFAULT NULL,
  `shuffle_questions` TINYINT(1) DEFAULT NULL,

  PRIMARY KEY (`exam_section_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- exam_settings
-- =====================
DROP TABLE IF EXISTS `exam_settings`;
CREATE TABLE `exam_settings` (
  `settings_id` BIGINT NOT NULL AUTO_INCREMENT,

  `exam_id` BIGINT NOT NULL,

  `attempts_allowed` INT NOT NULL,

  `negative_marking` TINYINT(1) NOT NULL,
  `negative_mark_value` DOUBLE DEFAULT NULL,

  `shuffle_questions` TINYINT(1) NOT NULL,
  `shuffle_options` TINYINT(1) NOT NULL,

  `allow_late_entry` TINYINT(1) NOT NULL,

  `network_mode` VARCHAR(50) NOT NULL,

  PRIMARY KEY (`settings_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- exam_violation
-- =====================
DROP TABLE IF EXISTS `exam_violation`;
CREATE TABLE `exam_violation` (
  `violation_id` BIGINT NOT NULL AUTO_INCREMENT,

  `attempt_id` BIGINT NOT NULL,
  `violation_type` VARCHAR(50) NOT NULL,
  `violation_time` DATETIME NOT NULL,

  PRIMARY KEY (`violation_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- faqs
-- =====================
DROP TABLE IF EXISTS `faqs`;
CREATE TABLE `faqs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `question` VARCHAR(500) NOT NULL,
  `answer` TEXT NOT NULL,
  `category` VARCHAR(50) NOT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- inventory_stock
-- =====================
DROP TABLE IF EXISTS `inventory_stock`;
CREATE TABLE `inventory_stock` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `item_id` BIGINT NOT NULL,

  `total_stock` INT DEFAULT NULL,
  `available_stock` INT DEFAULT NULL,
  `reserved_stock` INT DEFAULT NULL,
  `damaged_stock` INT DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_inventory_stock_item`
  (`item_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- inventory_transactions
-- =====================
DROP TABLE IF EXISTS `inventory_transactions`;
CREATE TABLE `inventory_transactions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `item_id` BIGINT DEFAULT NULL,

  `transaction_type` VARCHAR(50) DEFAULT NULL,

  `quantity` INT DEFAULT NULL,

  `before_stock` INT DEFAULT NULL,
  `after_stock` INT DEFAULT NULL,

  `reference_type` VARCHAR(50) DEFAULT NULL,
  `reference_id` BIGINT DEFAULT NULL,

  `performed_by` BIGINT DEFAULT NULL,

  `remarks` VARCHAR(255) DEFAULT NULL,
  `stock_type` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- external_participants
-- =====================
DROP TABLE IF EXISTS `external_participants`;
CREATE TABLE `external_participants` (
  `participant_id` BIGINT NOT NULL AUTO_INCREMENT,

  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `organization` VARCHAR(255) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`participant_id`),

  UNIQUE KEY `uk_external_participants_email`
  (`email`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- items
-- =====================
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `sku_code` VARCHAR(255) NOT NULL,

  `item_name` VARCHAR(255) DEFAULT NULL,
  `category` VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,

  `unit` VARCHAR(255) DEFAULT NULL,
  `location` VARCHAR(255) DEFAULT NULL,

  `min_stock_level` INT DEFAULT NULL,
  `opening_stock` INT DEFAULT NULL,

  `price` DOUBLE DEFAULT NULL,
  `tax_percentage` DOUBLE DEFAULT NULL,

  `is_refundable` TINYINT(1) DEFAULT NULL,
  `is_trackable` TINYINT(1) DEFAULT NULL,
  `is_consumable` TINYINT(1) DEFAULT NULL,

  `linked_course_id` BIGINT DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_items_sku_code` (`sku_code`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- question
-- =====================
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `question_id` BIGINT NOT NULL AUTO_INCREMENT,

  `question_text` VARCHAR(1000) DEFAULT NULL,
  `question_image_url` VARCHAR(255) DEFAULT NULL,

  `content_type` VARCHAR(50) NOT NULL,
  `question_type` VARCHAR(50) NOT NULL,

  `programming_language` VARCHAR(50) DEFAULT NULL,

  PRIMARY KEY (`question_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- question_descriptive_answer
-- =====================
DROP TABLE IF EXISTS `question_descriptive_answer`;
CREATE TABLE `question_descriptive_answer` (
  `descriptive_answer_id` BIGINT NOT NULL AUTO_INCREMENT,

  `question_id` BIGINT NOT NULL,

  `answer_text` TEXT NOT NULL,
  `keywords` TEXT,
  `keyword_weight` DOUBLE DEFAULT NULL,
  `guidelines` TEXT,

  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`descriptive_answer_id`),

  UNIQUE KEY `uk_question_descriptive_answer`
  (`question_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- question_option
-- =====================
DROP TABLE IF EXISTS `question_option`;
CREATE TABLE `question_option` (
  `option_id` BIGINT NOT NULL AUTO_INCREMENT,

  `question_id` BIGINT NOT NULL,

  `option_text` TEXT,
  `option_image_url` VARCHAR(255) DEFAULT NULL,

  `is_correct` TINYINT(1) NOT NULL,

  PRIMARY KEY (`option_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- question_section
-- =====================
DROP TABLE IF EXISTS `question_section`;
CREATE TABLE `question_section` (
  `section_id` BIGINT NOT NULL AUTO_INCREMENT,

  `section_name` VARCHAR(255) NOT NULL,
  `section_description` TEXT,

  `shuffle_questions` TINYINT(1) NOT NULL,

  `created_at` DATETIME NOT NULL,

  PRIMARY KEY (`section_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





-- =====================
-- returns_damage
-- =====================
DROP TABLE IF EXISTS `returns_damage`;
CREATE TABLE `returns_damage` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `item_id` BIGINT DEFAULT NULL,

  `issued_ref_id` BIGINT DEFAULT NULL,
  `quantity` INT DEFAULT NULL,
  `returned_by` BIGINT DEFAULT NULL,

  `type` VARCHAR(50) DEFAULT NULL,
  `item_condition` VARCHAR(50) DEFAULT NULL,
  `action_required` VARCHAR(50) DEFAULT NULL,

  `penalty_fee` DOUBLE DEFAULT NULL,
  `remarks` VARCHAR(255) DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,
  `approved_by` BIGINT DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- section_question
-- =====================
DROP TABLE IF EXISTS `section_question`;
CREATE TABLE `section_question` (
  `section_question_id` BIGINT NOT NULL AUTO_INCREMENT,

  `section_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,

  `display_order` INT DEFAULT NULL,

  PRIMARY KEY (`section_question_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =====================
-- session
-- =====================
DROP TABLE IF EXISTS `session`;
CREATE TABLE `session` (
  `session_id` BIGINT NOT NULL AUTO_INCREMENT,

  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `session_name` VARCHAR(255) NOT NULL,

  `start_date` DATE NOT NULL,
  `start_time` TIME NOT NULL,

  `duration_minutes` INT NOT NULL,

  `days` VARCHAR(100) NOT NULL,
  `session_type` VARCHAR(50) NOT NULL,

  `meeting_link` VARCHAR(255) DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  `topic_id` BIGINT DEFAULT NULL,

  PRIMARY KEY (`session_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =====================
-- session_content
-- =====================
DROP TABLE IF EXISTS `session_content`;
CREATE TABLE `session_content` (
  `session_content_id` BIGINT NOT NULL AUTO_INCREMENT,

  `session_id` BIGINT NOT NULL,

  `title` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1000) DEFAULT NULL,

  `content_type` VARCHAR(50) NOT NULL,
  `file_url` VARCHAR(255) DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  `total_duration` INT DEFAULT NULL,

  PRIMARY KEY (`session_content_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- stock_inward
-- =====================
DROP TABLE IF EXISTS `stock_inward`;
CREATE TABLE `stock_inward` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `po_reference` VARCHAR(255) DEFAULT NULL,

  `vendor_id` BIGINT DEFAULT NULL,
  `item_id` BIGINT DEFAULT NULL,

  `quantity_received` INT DEFAULT NULL,

  `cost_per_unit` DOUBLE DEFAULT NULL,
  `tax_percent` DOUBLE DEFAULT NULL,
  `total_amount` DOUBLE DEFAULT NULL,

  `invoice_file` VARCHAR(255) DEFAULT NULL,

  `received_by` BIGINT DEFAULT NULL,
  `received_date` DATETIME DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- stock_outward
-- =====================
DROP TABLE IF EXISTS `stock_outward`;
CREATE TABLE `stock_outward` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `item_id` BIGINT DEFAULT NULL,

  `issued_to_id` BIGINT DEFAULT NULL,
  `recipient_type` VARCHAR(50) DEFAULT NULL,

  `reason` VARCHAR(255) DEFAULT NULL,
  `quantity` INT DEFAULT NULL,

  `returned_quantity` INT DEFAULT NULL,

  `returnable` TINYINT(1) DEFAULT NULL,

  `issued_by` BIGINT DEFAULT NULL,

  `status` VARCHAR(50) DEFAULT NULL,
  `reference` VARCHAR(255) DEFAULT NULL,

  `issue_date` DATETIME DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =====================
-- student_batch
-- =====================
DROP TABLE IF EXISTS `student_batch`;
CREATE TABLE `student_batch` (
  `student_batch_id` BIGINT NOT NULL AUTO_INCREMENT,

  `student_id` BIGINT NOT NULL,
  `student_name` VARCHAR(255) NOT NULL,
  `student_email` VARCHAR(255) NOT NULL,

  `course_id` BIGINT NOT NULL,
  `batch_id` BIGINT NOT NULL,

  `status` VARCHAR(50) NOT NULL,

  `joined_at` DATETIME NOT NULL,

  `user_id` BIGINT DEFAULT NULL,

  PRIMARY KEY (`student_batch_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- student_batch_transfer
-- =====================
DROP TABLE IF EXISTS `student_batch_transfer`;
CREATE TABLE `student_batch_transfer` (
  `transfer_id` BIGINT NOT NULL AUTO_INCREMENT,

  `student_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,

  `from_batch_id` BIGINT NOT NULL,
  `to_batch_id` BIGINT NOT NULL,

  `reason` VARCHAR(500) DEFAULT NULL,

  `transferred_by` VARCHAR(255) NOT NULL,
  `transferred_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`transfer_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- student_calendar_event
-- =====================
DROP TABLE IF EXISTS `student_calendar_event`;
CREATE TABLE `student_calendar_event` (
  `event_id` BIGINT NOT NULL AUTO_INCREMENT,

  `user_id` BIGINT NOT NULL,

  `title` VARCHAR(255) NOT NULL,
  `description` TEXT,

  `event_date` DATE NOT NULL,

  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,

  `event_type` VARCHAR(50) NOT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`event_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- student_video_progress
-- =====================
DROP TABLE IF EXISTS `student_video_progress`;
CREATE TABLE `student_video_progress` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `student_id` BIGINT NOT NULL,
  `session_content_id` BIGINT NOT NULL,
  `session_id` BIGINT NOT NULL,

  `watched_duration` BIGINT NOT NULL,
  `last_watched_position` BIGINT NOT NULL,
  `total_duration_snapshot` BIGINT NOT NULL,

  `status` VARCHAR(50) NOT NULL,
  `completed_at` DATETIME DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `last_updated_at` DATETIME DEFAULT NULL,

  `percentage_watched` DOUBLE DEFAULT NULL,
  `user_id` BIGINT DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_student_video_progress`
  (`student_id`, `session_content_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- support_tickets
-- =====================
DROP TABLE IF EXISTS `support_tickets`;
CREATE TABLE `support_tickets` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `ticket_code` VARCHAR(255) NOT NULL,

  `student_id` BIGINT NOT NULL,
  `subject` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,

  `category` VARCHAR(50) NOT NULL,
  `priority` VARCHAR(50) NOT NULL,
  `status` VARCHAR(50) NOT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_ticket_code` (`ticket_code`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- ticket_messages
-- =====================
DROP TABLE IF EXISTS `ticket_messages`;
CREATE TABLE `ticket_messages` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `ticket_id` BIGINT NOT NULL,
  `sender_type` VARCHAR(50) NOT NULL,
  `message` TEXT NOT NULL,
  `attachment_url` VARCHAR(255) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- topics
-- =====================
DROP TABLE IF EXISTS `topics`;
CREATE TABLE `topics` (
  `topic_id` BIGINT NOT NULL AUTO_INCREMENT,

  `topic_name` VARCHAR(255) NOT NULL,
  `topic_description` VARCHAR(255) DEFAULT NULL,
  `status` VARCHAR(50) DEFAULT NULL,

  `course_id` BIGINT NOT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  `sequence_order` INT DEFAULT NULL,

  PRIMARY KEY (`topic_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- topic_contents
-- =====================
DROP TABLE IF EXISTS `topic_contents`;
CREATE TABLE `topic_contents` (
  `content_id` BIGINT NOT NULL AUTO_INCREMENT,

  `content_type` VARCHAR(50) NOT NULL,
  `content_source` VARCHAR(50) NOT NULL,
  `content_title` VARCHAR(255) NOT NULL,
  `content_description` VARCHAR(1000) DEFAULT NULL,

  `file_url` VARCHAR(255) DEFAULT NULL,
  `content_order` INT DEFAULT NULL,

  `topic_id` BIGINT NOT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`content_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- vendors
-- =====================
DROP TABLE IF EXISTS `vendors`;
CREATE TABLE `vendors` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,

  `vendor_code` VARCHAR(255) NOT NULL,
  `vendor_name` VARCHAR(255) NOT NULL,

  `contact_person` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(255) DEFAULT NULL,

  `vendor_type` VARCHAR(100) DEFAULT NULL,
  `status` VARCHAR(50) DEFAULT NULL,

  `created_at` DATETIME DEFAULT NULL,

  `gst_number` VARCHAR(100) DEFAULT NULL,
  `pan_number` VARCHAR(100) DEFAULT NULL,

  `street_address` VARCHAR(255) DEFAULT NULL,
  `city` VARCHAR(100) DEFAULT NULL,
  `state` VARCHAR(100) DEFAULT NULL,
  `pincode` VARCHAR(20) DEFAULT NULL,

  `bank_name` VARCHAR(255) DEFAULT NULL,
  `account_number` VARCHAR(255) DEFAULT NULL,
  `ifsc_code` VARCHAR(100) DEFAULT NULL,
  `branch_name` VARCHAR(255) DEFAULT NULL,
  `payment_terms` VARCHAR(255) DEFAULT NULL,

  `gst_certificate_path` VARCHAR(255) DEFAULT NULL,
  `bank_proof_path` VARCHAR(255) DEFAULT NULL,
  `agreement_path` VARCHAR(255) DEFAULT NULL,

  PRIMARY KEY (`id`),

  UNIQUE KEY `uk_vendor_code` (`vendor_code`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- vendor_items
-- =====================
DROP TABLE IF EXISTS `vendor_items`;
CREATE TABLE `vendor_items` (
  `vendor_id` BIGINT NOT NULL,
  `item_id` BIGINT NOT NULL,

  PRIMARY KEY (`vendor_id`, `item_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar
-- =====================
DROP TABLE IF EXISTS `webinar`;
CREATE TABLE `webinar` (
  `webinar_id` BIGINT NOT NULL AUTO_INCREMENT,

  `title` VARCHAR(255) NOT NULL,
  `description` TEXT,

  `trainer_id` BIGINT NOT NULL,

  `mode` VARCHAR(50) NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  `status` VARCHAR(50) NOT NULL,

  `start_time` DATETIME NOT NULL,
  `duration_minutes` INT NOT NULL,
  `timezone` VARCHAR(100) NOT NULL,

  `max_participants` INT NOT NULL,
  `registered_count` INT NOT NULL,

  `meeting_link` VARCHAR(255) DEFAULT NULL,

  `venue_name` VARCHAR(255) DEFAULT NULL,
  `venue_address` VARCHAR(255) DEFAULT NULL,
  `venue_city` VARCHAR(100) DEFAULT NULL,
  `venue_country` VARCHAR(100) DEFAULT NULL,
  `map_link` VARCHAR(255) DEFAULT NULL,

  `price` DOUBLE DEFAULT NULL,
  `image_url` VARCHAR(255) DEFAULT NULL,

  `allow_external` TINYINT(1) NOT NULL,

  `created_at` DATETIME DEFAULT NULL,
  `updated_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`webinar_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_attendance
-- =====================
DROP TABLE IF EXISTS `webinar_attendance`;
CREATE TABLE `webinar_attendance` (
  `attendance_id` BIGINT NOT NULL AUTO_INCREMENT,

  `webinar_id` BIGINT NOT NULL,
  `registration_id` BIGINT NOT NULL,

  `join_time` DATETIME DEFAULT NULL,
  `leave_time` DATETIME DEFAULT NULL,

  `status` VARCHAR(50) NOT NULL,
  `mode` VARCHAR(50) NOT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`attendance_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_chat_messages
-- =====================
DROP TABLE IF EXISTS `webinar_chat_messages`;
CREATE TABLE `webinar_chat_messages` (
  `chat_id` BIGINT NOT NULL AUTO_INCREMENT,

  `webinar_id` BIGINT NOT NULL,

  `sender_id` BIGINT NOT NULL,
  `sender_name` VARCHAR(100) NOT NULL,

  `message` VARCHAR(1000) NOT NULL,

  `sent_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`chat_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




-- =====================
-- webinar_polls
-- =====================
DROP TABLE IF EXISTS `webinar_polls`;
CREATE TABLE `webinar_polls` (
  `poll_id` BIGINT NOT NULL AUTO_INCREMENT,

  `webinar_id` BIGINT NOT NULL,

  `question` VARCHAR(500) NOT NULL,

  `status` VARCHAR(50) NOT NULL,

  `created_by` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`poll_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_poll_options
-- =====================
DROP TABLE IF EXISTS `webinar_poll_options`;
CREATE TABLE `webinar_poll_options` (
  `poll_id` BIGINT NOT NULL,
  `option_text` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_poll_responses
-- =====================
DROP TABLE IF EXISTS `webinar_poll_responses`;
CREATE TABLE `webinar_poll_responses` (
  `response_id` BIGINT NOT NULL AUTO_INCREMENT,

  `poll_id` BIGINT NOT NULL,

  `user_id` BIGINT DEFAULT NULL,
  `participant_id` BIGINT DEFAULT NULL,

  `selected_option` VARCHAR(255) NOT NULL,

  `responded_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`response_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_questions
-- =====================
DROP TABLE IF EXISTS `webinar_questions`;
CREATE TABLE `webinar_questions` (
  `question_id` BIGINT NOT NULL AUTO_INCREMENT,

  `webinar_id` BIGINT NOT NULL,

  `sender_id` BIGINT NOT NULL,
  `sender_name` VARCHAR(100) NOT NULL,

  `question` VARCHAR(1000) NOT NULL,
  `answer` TEXT,

  `status` VARCHAR(50) NOT NULL,

  `asked_at` DATETIME DEFAULT NULL,
  `answered_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`question_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_recordings
-- =====================
DROP TABLE IF EXISTS `webinar_recordings`;
CREATE TABLE `webinar_recordings` (
  `recording_id` BIGINT NOT NULL AUTO_INCREMENT,

  `webinar_id` BIGINT NOT NULL,

  `recording_url` VARCHAR(255) NOT NULL,
  `duration_minutes` INT NOT NULL,

  `uploaded_by` BIGINT NOT NULL,

  `created_at` DATETIME DEFAULT NULL,

  PRIMARY KEY (`recording_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- =====================
-- webinar_registrations
-- =====================
DROP TABLE IF EXISTS `webinar_registrations`;
CREATE TABLE `webinar_registrations` (
  `registration_id` BIGINT NOT NULL AUTO_INCREMENT,

  `webinar_id` BIGINT NOT NULL,

  `user_id` BIGINT DEFAULT NULL,
  `participant_id` BIGINT DEFAULT NULL,

  `participant_type` VARCHAR(50) NOT NULL,
  `registration_status` VARCHAR(50) NOT NULL,
  `payment_status` VARCHAR(50) NOT NULL,

  `registration_time` DATETIME DEFAULT NULL,

  PRIMARY KEY (`registration_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


SET FOREIGN_KEY_CHECKS = 1;
