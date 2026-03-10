package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Mentor;
import com.lms.www.model.User;

@Repository
public interface MentorRepository
        extends JpaRepository<Mentor, Long> {

    List<Mentor> findByUser(User user);
}
