package com.lms.www.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @GetMapping("/test")
    public String testInstructorAccess() {
        return "INSTRUCTOR ACCESS OK";
    }
}
