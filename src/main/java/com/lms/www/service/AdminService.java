package com.lms.www.service;

import java.util.List;

import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.model.Instructor;
import com.lms.www.model.Parent;
import com.lms.www.model.Student;
import com.lms.www.model.User;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {

    void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest);
    void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest);
    void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest);

    List<User> getAllUsers();
    User getUserByUserId(Long userId);
    User getUserByEmail(String email);

    void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request);

    void deleteUser(Long userId, User admin, HttpServletRequest request);

    List<Student> getAllStudents();
    Student getStudentByStudentId(Long studentId);

    List<Parent> getAllParents();
    Parent getParentByParentId(Long parentId);

    List<Instructor> getAllInstructors();
    Instructor getInstructorByInstructorId(Long instructorId);

    void mapParentToStudent(Long parentId, Long studentId, User admin, HttpServletRequest request);

}
