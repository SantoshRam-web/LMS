package com.lms.www.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "failed_login_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FailedLoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "attempt_time")
    private String attemptTime;

    @Column(name = "ip_address")
    private String ipAddress;
}
