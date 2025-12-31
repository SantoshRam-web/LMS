package com.lms.www.service;

import com.lms.www.controller.request.*;

public interface AdminService {

    void createStudent(StudentRequest request);

    void createInstructor(InstructorRequest request);

    void createParent(ParentRequest request);
}
