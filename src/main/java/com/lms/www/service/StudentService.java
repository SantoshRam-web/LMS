package com.lms.www.service;

import com.lms.www.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student createStudent(Student student);
}
