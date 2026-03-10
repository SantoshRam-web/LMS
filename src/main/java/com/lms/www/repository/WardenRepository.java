package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.User;
import com.lms.www.model.Warden;

@Repository
public interface WardenRepository
        extends JpaRepository<Warden, Long> {

    List<Warden> findByUser(User user);
}
