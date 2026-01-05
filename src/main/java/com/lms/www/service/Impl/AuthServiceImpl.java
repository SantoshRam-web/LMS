package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.FailedLoginAttempt;
import com.lms.www.model.RolePermission;
import com.lms.www.model.User;
import com.lms.www.model.UserRole;
import com.lms.www.model.UserSession;
import com.lms.www.repository.FailedLoginAttemptRepository;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserRoleRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.service.AuthService;
import com.lms.www.service.EmailService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserSessionRepository userSessionRepository;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;


    public AuthServiceImpl(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            UserSessionRepository userSessionRepository,
            FailedLoginAttemptRepository failedLoginAttemptRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            EmailService emailService

    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userSessionRepository = userSessionRepository;
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;

    }

    @Override
    public String login(String email, String password, String ipAddress) {

        User user = userRepository.findByEmail(email).orElse(null);

        // ❌ USER NOT FOUND
        if (user == null) {
            saveFailedAttempt(null, ipAddress);
            emailService.sendLoginFailedMail(email, ipAddress, LocalDateTime.now());
            throw new RuntimeException("Invalid credentials");
        }


        // ❌ USER DISABLED
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        // ❌ PASSWORD WRONG
        if (!passwordEncoder.matches(password, user.getPassword())) {
            saveFailedAttempt(user.getUserId(), ipAddress);
            emailService.sendLoginFailedMail(user.getEmail(), ipAddress, LocalDateTime.now());
            throw new RuntimeException("Invalid credentials");
        }


        // ✅ SUCCESS LOGIN
        List<UserRole> userRoles = userRoleRepository.findByUser(user);

        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRole().getRoleName())
                .toList();

        Set<String> permissions = new HashSet<>();

        for (UserRole ur : userRoles) {
            List<RolePermission> rolePermissions =
                    rolePermissionRepository.findByRole(ur.getRole());

            rolePermissions.forEach(rp ->
                    permissions.add(rp.getPermission().getPermissionName())
            );
        }

        String token = jwtUtil.generateToken(
                user.getUserId(),
                user.getEmail(),
                roles,
                permissions.stream().toList()
        );

        UserSession session = new UserSession();
        session.setUser(user);
        session.setToken(token);
        session.setLoginTime(LocalDateTime.now());

        userSessionRepository.save(session);
        
        emailService.sendLoginSuccessMail(
                user,
                ipAddress,
                LocalDateTime.now()
        );


        return token;
    }

    private void saveFailedAttempt(Long userId, String ipAddress) {
        FailedLoginAttempt attempt = new FailedLoginAttempt();
        attempt.setUserId(userId);
        attempt.setAttemptTime(LocalDateTime.now().toString());
        attempt.setIpAddress(ipAddress);
        failedLoginAttemptRepository.save(attempt);
    }
}
