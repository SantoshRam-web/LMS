package com.lms.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.www.model.Address;

@Repository
public interface AddressRepository
        extends JpaRepository<Address, Long> {
}
