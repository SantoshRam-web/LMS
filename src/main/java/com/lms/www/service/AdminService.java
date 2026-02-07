package com.lms.www.service;

import java.util.List;

import com.lms.www.controller.request.ConductorRequest;
import com.lms.www.controller.request.DriverRequest;
import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.model.Conductor;
import com.lms.www.model.Driver;
import com.lms.www.model.Instructor;
import com.lms.www.model.Parent;
import com.lms.www.model.Student;
import com.lms.www.model.User;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {

    void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest);
    void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest);
    void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest);
    void createDriver(DriverRequest request, User admin, HttpServletRequest httpRequest);
    void createConductor(ConductorRequest request, User admin, HttpServletRequest httpRequest);

    List<User> getAllUsers();
    User getUserByUserId(Long userId);
    User getUserByEmail(String email);

    List<Student> getAllStudents();
    List<Parent> getAllParents();
    List<Instructor> getAllInstructors();
    List<Driver> getAllDrivers();
    List<Conductor> getAllConductors();

    Student getStudentByStudentId(Long studentId);
    Parent getParentByParentId(Long parentId);
    Instructor getInstructorByInstructorId(Long instructorId);
    Driver getDriverByDriverId(Long driverId);
    Conductor getConductorByConductorId(Long conductorId);

    void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request);
    void deleteUser(Long userId, User admin, HttpServletRequest request);

    void mapParentToStudent(
            Long parentId,
            Long studentId,
            User admin,
            HttpServletRequest request
    );

    void setUserEnabled(
            Long userId,
            boolean enabled,
            User admin,
            HttpServletRequest request
    );
    
    void updateMultiSessionAccess(
            Long userId,
            boolean allowMultiSession,
            User admin,
            HttpServletRequest request
    );
    
    void updateMultiSessionAccessByRole(
            String roleName,
            boolean allowMultiSession,
            User admin,
            HttpServletRequest request
    );


}
