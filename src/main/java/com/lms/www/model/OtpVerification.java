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
    private Long otpId;

    private String email;
    private String phone;

    private String otp;
    private String purpose;

    private LocalDateTime expiresAt;

    private Integer attempts;
    private Integer maxAttempts;

    private Boolean verified;

    private LocalDateTime createdAt;
}
