package com.lms.www.service;

import java.util.List;

import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.model.Address;
import com.lms.www.model.Instructor;
import com.lms.www.model.Parent;
import com.lms.www.model.Student;
import com.lms.www.model.User;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {

    // ---------- CREATE ----------
    void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest);
    void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest);
    void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest);

    // ---------- READ ----------
    List<User> getAllUsers();
    User getUserByUserId(Long userId);
    User getUserByEmail(String email);

    List<Student> getAllStudents();
    List<Parent> getAllParents();
    List<Instructor> getAllInstructors();

    Student getStudentByStudentId(Long studentId);
    Parent getParentByParentId(Long parentId);
    Instructor getInstructorByInstructorId(Long instructorId);

    // ---------- UPDATE ----------
    void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request);

    // ---------- DELETE ----------
    void deleteUser(Long userId, User admin, HttpServletRequest request);

    // ---------- MAP ----------
    void mapParentToStudent(
            Long parentId,
            Long studentId,
            User admin,
            HttpServletRequest request
    );

    // ---------- ENABLE / DISABLE ----------
    void setUserEnabled(
            Long userId,
            boolean enabled,
            User admin,
            HttpServletRequest request
    );

    // ---------- ADDRESS (ADDED) ----------
    Address getAddressByEmail(String email);
    Address updateAddress(Long userId, Address address, User admin, HttpServletRequest request);
}
