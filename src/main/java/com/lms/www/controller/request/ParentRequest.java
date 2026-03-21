package com.lms.www.controller.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    // ✅ REQUIRED
    private String roleName;
    
    private List<String> permissions;
    
    private Long studentId;
}
