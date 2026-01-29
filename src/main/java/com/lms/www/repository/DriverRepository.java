package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Driver;
import com.lms.www.model.User;

@Repository
public interface DriverRepository
        extends JpaRepository<Driver, Long> {

    List<Driver> findByUser(User user);
}
