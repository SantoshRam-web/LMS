package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Accountant;
import com.lms.www.model.User;

@Repository
public interface AccountantRepository
        extends JpaRepository<Accountant, Long> {

    List<Accountant> findByUser(User user);
}
