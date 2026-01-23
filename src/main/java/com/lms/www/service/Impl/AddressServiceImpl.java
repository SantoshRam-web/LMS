package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.UserAuthorizationUtil;
import com.lms.www.model.Address;
import com.lms.www.model.AuditLog;
import com.lms.www.model.User;
import com.lms.www.repository.AddressRepository;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.AddressService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            UserRepository userRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public Address addAddress(Long userId, Address address, User admin, HttpServletRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (addressRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Address already exists for user");
        }
        
        UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(
                admin,
                user
        );

        address.setUser(user);
        Address saved = addressRepository.save(address);

        audit("CREATE", saved.getAddressId(), admin, request);
        return saved;
    }

    @Override
    public Address getAddress(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    // 🔴 PATCH (null-safe)
    @Override
    public Address updateAddress(Long userId, Address newAddress, User admin, HttpServletRequest request) {

    	
        Address existing = getAddress(userId);
        
        User targetUser = existing.getUser();

        UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(
                admin,
                targetUser
        );

        if (newAddress.getPinCode() != null)
            existing.setPinCode(newAddress.getPinCode());

        if (newAddress.getDistrict() != null)
            existing.setDistrict(newAddress.getDistrict());

        if (newAddress.getMandal() != null)
            existing.setMandal(newAddress.getMandal());

        if (newAddress.getCity() != null)
            existing.setCity(newAddress.getCity());

        if (newAddress.getVillage() != null)
            existing.setVillage(newAddress.getVillage());

        if (newAddress.getDNo() != null)
            existing.setDNo(newAddress.getDNo());

        audit("UPDATE", existing.getAddressId(), admin, request);
        return existing;
    }

    @Override
    public void deleteAddress(Long userId, User admin, HttpServletRequest request) {

        Address address = getAddress(userId);
        
        User targetUser = address.getUser();

        UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(
                admin,
                targetUser
        );


        addressRepository.delete(address); // 🔴 REAL DELETE

        audit("DELETE", address.getAddressId(), admin, request);
    }

    private void audit(String action, Long entityId, User admin, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName("ADDRESS");
        log.setEntityId(entityId);
        log.setUserId(admin.getUserId());
        log.setIpAddress(request.getRemoteAddr());
        log.setCreatedTime(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
