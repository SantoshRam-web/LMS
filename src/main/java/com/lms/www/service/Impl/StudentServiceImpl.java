package com.lms.www.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.www.model.Student;
import com.lms.www.repository.StudentRepository;
import com.lms.www.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
}
