package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.model.AuditLog;
import com.lms.www.model.Instructor;
import com.lms.www.model.Parent;
import com.lms.www.model.ParentStudentRelation;
import com.lms.www.model.Role;
import com.lms.www.model.Student;
import com.lms.www.model.User;
import com.lms.www.model.UserRole;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.InstructorRepository;
import com.lms.www.repository.LoginHistoryRepository;
import com.lms.www.repository.ParentRepository;
import com.lms.www.repository.ParentStudentRelationRepository;
import com.lms.www.repository.RoleRepository;
import com.lms.www.repository.StudentRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserRoleRepository;
import com.lms.www.service.AdminService;
import com.lms.www.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final ParentRepository parentRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginHistoryRepository loginHistoryRepository;
    private final ParentStudentRelationRepository parentStudentRelationRepository;
    private final EmailService emailService;


    public AdminServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            StudentRepository studentRepository,
            InstructorRepository instructorRepository,
            ParentRepository parentRepository,
            AuditLogRepository auditLogRepository,
            LoginHistoryRepository loginHistoryRepository,
            ParentStudentRelationRepository parentStudentRelationRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.parentRepository = parentRepository;
        this.auditLogRepository = auditLogRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.passwordEncoder = passwordEncoder;
        this.parentStudentRelationRepository = parentStudentRelationRepository;
        this.emailService = emailService;
        
    }


    // ---------- COMMON ----------
    private User createBaseUser(
            String firstName,
            String lastName,
            String email,
            String password,
            String phone
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private void assignRole(User user, String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
    }

    private void audit(
            String action,
            String entity,
            Long entityId,
            User admin,
            HttpServletRequest request
    ) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entity);
        log.setEntityId(entityId);
        log.setPerformedBy(admin);
        log.setCreatedTime(LocalDateTime.now());
        log.setIpAddress(request.getRemoteAddr());

        auditLogRepository.save(log);
    }

    // ---------- CREATE ----------
    @Override
    public void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest) {
        User user = createBaseUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );

        assignRole(user, "ROLE_STUDENT");

        Student student = new Student();
        student.setUser(user);
        student.setDob(request.getDob());
        student.setGender(request.getGender());

        studentRepository.save(student);
        audit("CREATE", "STUDENT", user.getUserId(), admin, httpRequest);
        emailService.sendRegistrationMail(user, "STUDENT");

    }

    @Override
    public void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest) {
        User user = createBaseUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );

        assignRole(user, "ROLE_INSTRUCTOR");

        Instructor instructor = new Instructor();
        instructor.setUser(user);

        instructorRepository.save(instructor);
        audit("CREATE", "INSTRUCTOR", user.getUserId(), admin, httpRequest);
        emailService.sendRegistrationMail(user, "INSTRUCTOR");

    }

    @Override
    public void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest) {
        User user = createBaseUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );

        assignRole(user, "ROLE_PARENT");

        Parent parent = new Parent();
        parent.setUser(user);

        parentRepository.save(parent);
        audit("CREATE", "PARENT", user.getUserId(), admin, httpRequest);
        emailService.sendRegistrationMail(user, "PARENT");

    }

    // ---------- READ ----------
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }
    
    @Override
    public Student getStudentByStudentId(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // force load parents
        parentStudentRelationRepository.findByStudent(student)
                .forEach(r -> r.getParent().getUser().getEmail());

        return student;
    }
    
    @Override
    public Parent getParentByParentId(Long parentId) {

        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        parentStudentRelationRepository.findByParent(parent)
                .forEach(r -> r.getStudent().getUser().getEmail());

        return parent;
    }
    
    @Override
    public Instructor getInstructorByInstructorId(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
    }
    
    @Override
    public void mapParentToStudent(
            Long parentId,
            Long studentId,
            User admin,
            HttpServletRequest request
    ) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ParentStudentRelation rel = new ParentStudentRelation();
        rel.setParent(parent);
        rel.setStudent(student);

        parentStudentRelationRepository.save(rel);

        audit("MAP", "PARENT_STUDENT", rel.getRelId(), admin, request);
    }


    // ---------- DELETE ----------
    @Override
    public void deleteUser(Long userId, User admin, HttpServletRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Delete login history
        loginHistoryRepository.deleteByUser(user);

        // 2️⃣ Delete role mappings
        userRoleRepository.deleteByUser(user);

        // 3️⃣ Delete role-specific tables
        studentRepository.findByUser(user)
                .forEach(studentRepository::delete);

        instructorRepository.findByUser(user)
                .forEach(instructorRepository::delete);

        parentRepository.findAll()
                .stream()
                .filter(p -> p.getUser().getUserId().equals(userId))
                .forEach(parentRepository::delete);

        // 4️⃣ Delete user
        userRepository.delete(user);

        // 5️⃣ Audit
        audit("DELETE", "USER", userId, admin, request);
    }

    
    @Override
    public void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request) {

        User existing = getUserByUserId(userId);

        if (updatedUser.getFirstName() != null)
            existing.setFirstName(updatedUser.getFirstName());

        if (updatedUser.getLastName() != null)
            existing.setLastName(updatedUser.getLastName());

        if (updatedUser.getPhone() != null)
            existing.setPhone(updatedUser.getPhone());

        userRepository.save(existing);

        audit("UPDATE", "USER", userId, admin, request);
    }
    
    @Override
    public void setUserEnabled(
            Long userId,
            boolean enabled,
            User admin,
            HttpServletRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(enabled);
        userRepository.save(user);

        audit(
                enabled ? "ENABLE" : "DISABLE",
                "USER",
                userId,
                admin,
                request
        );
    }


}
