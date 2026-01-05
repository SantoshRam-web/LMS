package com.lms.www.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.model.User;
import com.lms.www.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------- CREATE ----------
    @PostMapping("/students")
    public ResponseEntity<String> createStudent(
            @RequestBody StudentRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createStudent(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Student created successfully");
    }

    @PostMapping("/instructors")
    public ResponseEntity<String> createInstructor(
            @RequestBody InstructorRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createInstructor(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Instructor created successfully");
    }

    @PostMapping("/parents")
    public ResponseEntity<String> createParent(
            @RequestBody ParentRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createParent(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Parent created successfully");
    }

    // ---------- READ ----------
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserByUserId(userId));
    }

    // ---------- DELETE ----------
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        adminService.deleteUser(userId, getLoggedInUser(), request);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ---------- HELPER ----------
    private User getLoggedInUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return adminService.getUserByEmail(email);
    }
    
    @PatchMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody User updatedUser,
            HttpServletRequest request
    ) {
        adminService.updateUser(userId, updatedUser, getLoggedInUser(), request);
        return ResponseEntity.ok("User updated");
    }
    
 // ---------- STUDENTS ----------
    @GetMapping("/getstudents")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @GetMapping("/getstudents/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(adminService.getStudentByStudentId(studentId));
    }

    // ---------- PARENTS ----------
    @GetMapping("/getparents")
    public ResponseEntity<?> getAllParents() {
        return ResponseEntity.ok(adminService.getAllParents());
    }

    @GetMapping("/getparents/{parentId}")
    public ResponseEntity<?> getParentById(@PathVariable Long parentId) {
        return ResponseEntity.ok(adminService.getParentByParentId(parentId));
    }

    // ---------- INSTRUCTORS ----------
    @GetMapping("/getinstructors")
    public ResponseEntity<?> getAllInstructors() {
        return ResponseEntity.ok(adminService.getAllInstructors());
    }

    @GetMapping("/getinstructors/{instructorId}")
    public ResponseEntity<?> getInstructorById(@PathVariable Long instructorId) {
        return ResponseEntity.ok(adminService.getInstructorByInstructorId(instructorId));
    }

    // ---------- MAP ----------
    @PostMapping("/parent-student-map")
    public ResponseEntity<?> mapParentStudent(
            @RequestBody Map<String, Long> body,
            HttpServletRequest request
    ) {
        adminService.mapParentToStudent(
                body.get("parentId"),
                body.get("studentId"),
                getLoggedInUser(),
                request
        );
        return ResponseEntity.ok("Parent mapped to student");
    }
    
 // ---------- ENABLE / DISABLE USER ----------

    @PatchMapping("/users/{userId}/disable")
    public ResponseEntity<?> disableUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        adminService.setUserEnabled(userId, false, getLoggedInUser(), request);
        return ResponseEntity.ok("User disabled");
    }

    @PatchMapping("/users/{userId}/enable")
    public ResponseEntity<?> enableUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        adminService.setUserEnabled(userId, true, getLoggedInUser(), request);
        return ResponseEntity.ok("User enabled");
    }



}

