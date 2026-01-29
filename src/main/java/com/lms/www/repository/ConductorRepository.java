package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Conductor;
import com.lms.www.model.User;

@Repository
public interface ConductorRepository
        extends JpaRepository<Conductor, Long> {

    List<Conductor> findByUser(User user);
}
