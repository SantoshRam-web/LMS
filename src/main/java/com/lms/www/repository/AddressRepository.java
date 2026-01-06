package com.lms.www.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.www.model.Address;
import com.lms.www.model.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByUser(User user);
}
