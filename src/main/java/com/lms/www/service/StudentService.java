package com.lms.www.service;

import java.util.List;

import com.lms.www.model.Student;

public interface StudentService {

    List<Student> getAllStudents();

    Student createStudent(Student student);
}
