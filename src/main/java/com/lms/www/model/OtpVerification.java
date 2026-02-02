package com.lms.www.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otp_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Long otpId;
    
    @Column(name = "email")
    private String email;
    
    private String phone;
    
    private String otp;
    
    private String purpose;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    private Integer attempts;
    
    @Column(name = "max_attempts")
    private Integer maxAttempts;
    
    private Boolean verified;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
