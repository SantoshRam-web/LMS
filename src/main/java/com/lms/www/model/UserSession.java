package com.lms.www.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")   // 🔥 EXACT DB COLUMN
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", length = 1000, nullable = false)
    private String token;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;
}
