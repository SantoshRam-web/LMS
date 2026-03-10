package com.lms.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Affiliate;
import com.lms.www.model.User;

@Repository
public interface AffiliateRepository
        extends JpaRepository<Affiliate, Long> {

    List<Affiliate> findByUser(User user);
}
