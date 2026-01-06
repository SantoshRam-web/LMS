package com.lms.www.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String phone;
    private Boolean enabled = true;

    // 🔴 DO NOT expose address in User APIs
    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Address address;
}
