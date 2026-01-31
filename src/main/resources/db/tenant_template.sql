-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)

/*!40101 SET FOREIGN_KEY_CHECKS=0 */;

-- =====================
-- 1. permissions
-- =====================
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `permission_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `permission_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 3. users (CORE TABLE)
-- =====================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role_name` varchar(50) NOT NULL,
  `permission_id` bigint DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_users_email` (`email`),
  UNIQUE KEY `uk_users_phone` (`phone`),
  KEY `fk_users_permission` (`permission_id`),
  CONSTRAINT `fk_users_permission`
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 5. address
-- =====================
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `address_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `pin_code` bigint DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `mandal` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `village` varchar(255) DEFAULT NULL,
  `d_no` bigint DEFAULT NULL,
  PRIMARY KEY (`address_id`),
  UNIQUE KEY `uk_address_user` (`user_id`),
  CONSTRAINT `fk_address_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 6. admin
-- =====================
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`admin_id`),
  KEY `fk_admin_user` (`user_id`),
  CONSTRAINT `fk_admin_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 7. audit_logs
-- =====================
DROP TABLE IF EXISTS `audit_logs`;
CREATE TABLE `audit_logs` (
  `audit_id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `entity_id` bigint DEFAULT NULL,
  `entity_name` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`audit_id`),
  KEY `fk_audit_user` (`user_id`),
  CONSTRAINT `fk_audit_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 12. parents
-- =====================
DROP TABLE IF EXISTS `parents`;
CREATE TABLE `parents` (
  `parent_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`parent_id`),
  UNIQUE KEY `uk_parent_user` (`user_id`),
  CONSTRAINT `fk_parent_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 13. students
-- =====================
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `student_id` bigint NOT NULL AUTO_INCREMENT,
  `dob` date DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  KEY `fk_student_user` (`user_id`),
  CONSTRAINT `fk_student_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 14. instructor / driver / conductor
-- =====================
DROP TABLE IF EXISTS `instructor`;
CREATE TABLE `instructor` (
  `instructor_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`instructor_id`),
  KEY `fk_instructor_user` (`user_id`),
  CONSTRAINT `fk_instructor_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `driver`;
CREATE TABLE `driver` (
  `driver_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`driver_id`),
  UNIQUE KEY `uk_driver_user` (`user_id`),
  CONSTRAINT `fk_driver_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `conductor`;
CREATE TABLE `conductor` (
  `conductor_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`conductor_id`),
  UNIQUE KEY `uk_conductor_user` (`user_id`),
  CONSTRAINT `fk_conductor_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 9. login_history
-- =====================
DROP TABLE IF EXISTS `login_history`;
CREATE TABLE `login_history` (
  `login_id` bigint NOT NULL AUTO_INCREMENT,
  `device` varchar(255) DEFAULT NULL,
  `ip_address` varchar(255) DEFAULT NULL,
  `login_time` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`login_id`),
  KEY `fk_login_user` (`user_id`),
  CONSTRAINT `fk_login_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 4. system_settings
-- =====================
DROP TABLE IF EXISTS `system_settings`;
CREATE TABLE `system_settings` (
  `setting_key` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `max_login_attempts` bigint NOT NULL,
  `acc_lock_duration` bigint NOT NULL,
  `pass_expiry_days` bigint NOT NULL,
  `pass_length` bigint NOT NULL,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `jwt_expiry_mins` bigint DEFAULT NULL,
  `session_timeout` bigint DEFAULT NULL,
  `multi_session` tinyint(1) DEFAULT 0,
  `enable_login_audit` tinyint(1) DEFAULT 1,
  `enable_audit_log` tinyint(1) DEFAULT 1,
  `password_last_updated_at` timestamp NULL DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`setting_key`),
  KEY `fk_system_settings_user` (`user_id`),
  CONSTRAINT `fk_system_settings_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 2. otp_verification
-- =====================
DROP TABLE IF EXISTS `otp_verification`;
CREATE TABLE `otp_verification` (
  `otp_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `otp` varchar(10) NOT NULL,
  `purpose` varchar(50) NOT NULL,
  `expires_at` datetime NOT NULL,
  `attempts` int DEFAULT 0,
  `max_attempts` int DEFAULT 3,
  `verified` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`otp_id`),
  UNIQUE KEY `uq_active_otp` (`email`,`purpose`,`verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 8. failed_login_attempts
-- =====================
DROP TABLE IF EXISTS `failed_login_attempts`;
CREATE TABLE `failed_login_attempts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `attempt_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ip_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_failed_user` (`user_id`),
  CONSTRAINT `fk_failed_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 10. password_reset_tokens
-- =====================
DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE `password_reset_tokens` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `reset_token` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`token_id`),
  KEY `fk_reset_user` (`user_id`),
  CONSTRAINT `fk_reset_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 11. user_sessions
-- =====================
DROP TABLE IF EXISTS `user_sessions`;
CREATE TABLE `user_sessions` (
  `session_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `token` longtext,
  `login_time` timestamp NULL DEFAULT NULL,
  `logout_time` timestamp NULL DEFAULT NULL,
  `last_activity_time` datetime DEFAULT NULL,
  PRIMARY KEY (`session_id`),
  KEY `fk_session_user` (`user_id`),
  CONSTRAINT `fk_session_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 15. parent_student_relation
-- =====================
DROP TABLE IF EXISTS `parent_student_relation`;
CREATE TABLE `parent_student_relation` (
  `rel_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`rel_id`),
  KEY `fk_ps_parent` (`parent_id`),
  KEY `fk_ps_student` (`student_id`),
  CONSTRAINT `fk_ps_parent`
    FOREIGN KEY (`parent_id`) REFERENCES `parents` (`parent_id`),
  CONSTRAINT `fk_ps_student`
    FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- 16. role_permissions
-- =====================
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions` (
  `role_permission_id` bigint NOT NULL AUTO_INCREMENT,
  `permission_id` bigint DEFAULT NULL,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_permission_id`),
  KEY `fk_role_permission_permission` (`permission_id`),
  CONSTRAINT `fk_role_permission_permission`
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET FOREIGN_KEY_CHECKS=1 */;

-- Dump completed on 2026-01-31 16:36:43
