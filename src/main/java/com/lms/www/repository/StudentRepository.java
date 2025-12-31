package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Student;
import com.lms.www.model.User;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    List<Student> findByUser(User user);
}
