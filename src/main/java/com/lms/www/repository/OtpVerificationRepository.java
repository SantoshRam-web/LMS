package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.OtpVerification;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    // ✅ already added
    Optional<OtpVerification> findByEmail(String email);
    
    Optional<OtpVerification> findByEmailAndPurpose(String email, String purpose);

    // ✅ REQUIRED for SUPER ADMIN FLOW
    Optional<OtpVerification> findByEmailAndPurposeAndVerifiedFalse(
            String email,
            String purpose
    );
}
