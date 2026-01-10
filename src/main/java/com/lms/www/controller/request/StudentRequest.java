package com.lms.www.controller.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    // ✅ REQUIRED
    private String roleName;

    private LocalDate dob;
    private String gender;
}
