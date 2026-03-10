package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.InventoryManager;
import com.lms.www.model.User;

@Repository
public interface InventoryManagerRepository
        extends JpaRepository<InventoryManager, Long> {

    List<InventoryManager> findByUser(User user);
}
