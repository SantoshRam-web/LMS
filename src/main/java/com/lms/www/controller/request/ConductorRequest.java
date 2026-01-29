package com.lms.www.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConductorRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    // ✅ REQUIRED
    private String roleName;
}
