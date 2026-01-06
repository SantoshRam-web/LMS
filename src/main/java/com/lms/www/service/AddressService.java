package com.lms.www.service;

import com.lms.www.model.Address;
import com.lms.www.model.User;

import jakarta.servlet.http.HttpServletRequest;

public interface AddressService {

    Address addAddress(Long userId, Address address, User admin, HttpServletRequest request);

    Address getAddress(Long userId);

    Address updateAddress(Long userId, Address address, User admin, HttpServletRequest request);

    void deleteAddress(Long userId, User admin, HttpServletRequest request);
}
