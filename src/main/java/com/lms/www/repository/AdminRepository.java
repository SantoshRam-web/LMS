package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Admin;
import com.lms.www.model.User;

@Repository
public interface AdminRepository
        extends JpaRepository<Admin, Long> {

    List<Admin> findByUser(User user);
}
