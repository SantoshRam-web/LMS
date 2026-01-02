package com.lms.www.service;

import com.lms.www.controller.request.*;
import com.lms.www.model.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AdminService {

    void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest);
    void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest);
    void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest);

    List<User> getAllUsers();
    User getUserByUserId(Long userId);
    User getUserByEmail(String email);

    void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request);

    void deleteUser(Long userId, User admin, HttpServletRequest request);
}
