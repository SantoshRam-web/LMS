package com.lms.www.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "user_permissions",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "permission_name"}
    )
)
@Getter
@Setter
public class UserPermission {

    // 🔑 PRIMARY KEY (row identity, NOT user)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_permission_id")
    private Long userPermissionId;

    // 🔗 FOREIGN KEY → users.user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔐 ACTUAL PERMISSION
    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
