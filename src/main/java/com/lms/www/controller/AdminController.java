package com.lms.www.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;


import com.lms.www.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(
            @RequestBody StudentRequest studentRequest) {

        adminService.createStudent(studentRequest);
        return ResponseEntity.ok("Student created successfully");
    }

    @PostMapping("/instructors")
    public ResponseEntity<String> addInstructor(
            @RequestBody InstructorRequest instructorRequest) {

        adminService.createInstructor(instructorRequest);
        return ResponseEntity.ok("Instructor created successfully");
    }

    @PostMapping("/parents")
    public ResponseEntity<String> addParent(
            @RequestBody ParentRequest parentRequest) {

        adminService.createParent(parentRequest);
        return ResponseEntity.ok("Parent created successfully");
    }

    @GetMapping("/test")
    public String testAdminAccess() {
        return "ADMIN ACCESS OK";
    }
}
