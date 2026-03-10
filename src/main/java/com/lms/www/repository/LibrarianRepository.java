package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Librarian;
import com.lms.www.model.User;

@Repository
public interface LibrarianRepository
        extends JpaRepository<Librarian, Long> {

    List<Librarian> findByUser(User user);
}
