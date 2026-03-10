package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departmenthead")
public class DepartmentHeadController {

    @GetMapping("/test")
    public String testDepartmentHeadAccess() {
        return "DEPARTMENT HEAD ACCESS OK";
    }
}
