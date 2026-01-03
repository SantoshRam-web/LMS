package com.lms.www.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.www.config.JwtUtil;
import com.lms.www.model.RolePermission;
import com.lms.www.model.User;
import com.lms.www.model.UserRole;
import com.lms.www.repository.RolePermissionRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserRoleRepository;
import com.lms.www.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

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

        return jwtUtil.generateToken(
                user.getUserId(),
                user.getEmail(),
                roles,
                permissions.stream().toList()
        );
    }
}
