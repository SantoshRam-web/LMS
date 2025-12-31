package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Instructor;
import com.lms.www.model.User;

@Repository
public interface InstructorRepository
        extends JpaRepository<Instructor, Long> {

    List<Instructor> findByUser(User user);
}
