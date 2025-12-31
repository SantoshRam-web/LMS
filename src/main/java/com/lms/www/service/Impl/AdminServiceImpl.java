package com.lms.www.service.Impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.www.controller.request.*;
import com.lms.www.model.*;
import com.lms.www.repository.*;
import com.lms.www.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final ParentRepository parentRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminServiceImpl(
            UserRepository userRepository,
            StudentRepository studentRepository,
            InstructorRepository instructorRepository,
            ParentRepository parentRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.parentRepository = parentRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createStudent(StudentRequest request) {

        User user = createUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), request.getPhone());

        Student student = new Student();
        student.setUser(user);
        studentRepository.save(student);

        assignRole(user, "ROLE_STUDENT");
    }

    @Override
    public void createInstructor(InstructorRequest request) {

        User user = createUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), request.getPhone());

        Instructor instructor = new Instructor();
        instructor.setUser(user);
        instructorRepository.save(instructor);

        assignRole(user, "ROLE_INSTRUCTOR");
    }

    @Override
    public void createParent(ParentRequest request) {

        User user = createUser(request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPassword(), request.getPhone());

        Parent parent = new Parent();
        parent.setUser(user);
        parentRepository.save(parent);

        assignRole(user, "ROLE_PARENT");
    }

    private User createUser(
            String firstName,
            String lastName,
            String email,
            String rawPassword,
            String phone) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPhone(phone);
        user.setEnabled(true);

        return userRepository.save(user);
    }


    private void assignRole(User user, String roleName) {

        Role role = roleRepository.findByRoleName(roleName).orElseThrow();

        UserRole ur = new UserRole();
        ur.setUser(user);
        ur.setRole(role);

        userRoleRepository.save(ur);
    }
}
