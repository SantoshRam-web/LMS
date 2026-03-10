package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.TransportManager;
import com.lms.www.model.User;

@Repository
public interface TransportManagerRepository
        extends JpaRepository<TransportManager, Long> {

    List<TransportManager> findByUser(User user);
}
