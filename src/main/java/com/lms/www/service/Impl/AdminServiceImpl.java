package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.model.AuditLog;
import com.lms.www.model.Instructor;
import com.lms.www.model.Parent;
import com.lms.www.model.ParentStudentRelation;
import com.lms.www.model.Student;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.User;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.InstructorRepository;
import com.lms.www.repository.LoginHistoryRepository;
import com.lms.www.repository.ParentRepository;
import com.lms.www.repository.ParentStudentRelationRepository;
import com.lms.www.repository.StudentRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.service.AdminService;
import com.lms.www.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final ParentRepository parentRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginHistoryRepository loginHistoryRepository;
    private final ParentStudentRelationRepository parentStudentRelationRepository;
    private final EmailService emailService;
    private final SystemSettingsRepository systemSettingsRepository;

    public AdminServiceImpl(
            UserRepository userRepository,
            StudentRepository studentRepository,
            InstructorRepository instructorRepository,
            ParentRepository parentRepository,
            AuditLogRepository auditLogRepository,
            LoginHistoryRepository loginHistoryRepository,
            ParentStudentRelationRepository parentStudentRelationRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            SystemSettingsRepository systemSettingsRepository
    ) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.parentRepository = parentRepository;
        this.auditLogRepository = auditLogRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.parentStudentRelationRepository = parentStudentRelationRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.systemSettingsRepository = systemSettingsRepository;
    }

    // ===================== COMMON =====================
    private User createBaseUser(
            String firstName,
            String lastName,
            String email,
            String password,
            String phone,
            String roleName
    ) {

        // -----------------------------
        // EMAIL CHECK
        // -----------------------------
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        // -----------------------------
        // PASSWORD LENGTH CHECK (FIXED)
        // -----------------------------
        if (password.length() < 10) {
            throw new RuntimeException(
                    "Password must be at least 10 characters"
            );
        }

        // -----------------------------
        // CREATE & SAVE USER FIRST
        // -----------------------------
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setEnabled(true);
        user.setRoleName(roleName);

        user = userRepository.save(user); // ✅ REQUIRED BEFORE SYSTEM_SETTINGS

        // -----------------------------
        // CREATE SYSTEM SETTINGS (PER USER)
        // -----------------------------
        SystemSettings settings = new SystemSettings();
        settings.setUserId(user.getUserId());
        settings.setMaxLoginAttempts(5L);
        settings.setAccLockDuration(30L);
        settings.setPassExpiryDays(60L);
        settings.setPassLength(10L);
        settings.setJwtExpiryMins(60L);
        settings.setSessionTimeout(60L);
        settings.setMultiSession(false);
        settings.setEnableLoginAudit(null);
        settings.setEnableAuditLog(null);

        systemSettingsRepository.save(settings);

        return user;
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
        log.setUserId(admin.getUserId());
        log.setCreatedTime(LocalDateTime.now());
        log.setIpAddress(request.getRemoteAddr());
        auditLogRepository.save(log);
    }

    // ===================== CREATE =====================
    @Override
    public void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest) {
       try {
    	User user = createBaseUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getRoleName()
        );

        Student student = new Student();
        student.setUser(user);
        student.setDob(request.getDob());
        student.setGender(request.getGender());
        studentRepository.save(student);

        audit("CREATE", "STUDENT", user.getUserId(), admin, httpRequest);
        markAuditStatus(admin.getUserId(), true);
        emailService.sendRegistrationMail(user, user.getRoleName());
        
       } catch (RuntimeException ex) {

           // ❌ FAILURE
           markAuditStatus(admin.getUserId(), false);

           throw ex;
       }
    }

    @Override
    public void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest) {
        try {
    	User user = createBaseUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getRoleName()
        );

        Instructor instructor = new Instructor();
        instructor.setUser(user);
        instructorRepository.save(instructor);

        audit("CREATE", "INSTRUCTOR", user.getUserId(), admin, httpRequest);
        
        // ✅ SUCCESS
        markAuditStatus(admin.getUserId(), true);
        
        emailService.sendRegistrationMail(user, user.getRoleName());
        
        }catch (RuntimeException ex) {

            // ❌ FAILURE
            markAuditStatus(admin.getUserId(), false);

            throw ex;
        }
    }

    @Override
    public void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest) {
        try {
    	User user = createBaseUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getRoleName()
        );

        Parent parent = new Parent();
        parent.setUser(user);
        parentRepository.save(parent);

        audit("CREATE", "PARENT", user.getUserId(), admin, httpRequest);
        
        // ✅ SUCCESS
        markAuditStatus(admin.getUserId(), true);
        
        emailService.sendRegistrationMail(user, user.getRoleName());
        
        } catch (RuntimeException ex) {

            // ❌ FAILURE
            markAuditStatus(admin.getUserId(), false);

            throw ex;
        }
    }

    // ===================== READ =====================
    @Override public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override public List<Student> getAllStudents() { return studentRepository.findAll(); }
    @Override public List<Parent> getAllParents() { return parentRepository.findAll(); }
    @Override public List<Instructor> getAllInstructors() { return instructorRepository.findAll(); }

    @Override
    public Student getStudentByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
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

    // ===================== UPDATE / DELETE =====================
    @Override
    public void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request) {
        try {
    	User existing = getUserByUserId(userId);

        if (updatedUser.getFirstName() != null) existing.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null) existing.setLastName(updatedUser.getLastName());
        if (updatedUser.getPhone() != null) existing.setPhone(updatedUser.getPhone());

        userRepository.save(existing);
        audit("UPDATE", "USER", userId, admin, request);
        
        // ✅ SUCCESS
        markAuditStatus(admin.getUserId(), true);
        
        } catch (RuntimeException ex) {

            // ❌ FAILURE
            markAuditStatus(admin.getUserId(), false);

            throw ex;
        }
    }

    @Override
    public void deleteUser(Long userId, User admin, HttpServletRequest request) {
        try {
    	User user = getUserByUserId(userId);

        loginHistoryRepository.deleteByUser(user);
        systemSettingsRepository.findByUserId(userId)
                .ifPresent(systemSettingsRepository::delete);

        studentRepository.findByUser(user).forEach(studentRepository::delete);
        instructorRepository.findByUser(user).forEach(instructorRepository::delete);
        parentRepository.findAll()
                .stream()
                .filter(p -> p.getUser().getUserId().equals(userId))
                .forEach(parentRepository::delete);

        userRepository.delete(user);
        audit("DELETE", "USER", userId, admin, request);
        
     // ✅ SUCCESS
        markAuditStatus(admin.getUserId(), true);
       
        } catch (RuntimeException ex) {

            // ❌ FAILURE
            markAuditStatus(admin.getUserId(), false);

            throw ex;
        }
    }

    @Override
    public void setUserEnabled(Long userId, boolean enabled, User admin, HttpServletRequest request) {
        try{
        User user = getUserByUserId(userId);
        user.setEnabled(enabled);
        userRepository.save(user);
        audit(enabled ? "ENABLE" : "DISABLE", "USER", userId, admin, request);
        // ✅ SUCCESS
        markAuditStatus(admin.getUserId(), true);
        
        }catch (RuntimeException ex) {

            // ❌ FAILURE
            markAuditStatus(admin.getUserId(), false);

            throw ex;
        }
        
      
    }
    
    @Override
    public void mapParentToStudent(
            Long parentId,
            Long studentId,
            User admin,
            HttpServletRequest request
    ) {

       try {
       Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ParentStudentRelation relation = new ParentStudentRelation();
        relation.setParent(parent);
        relation.setStudent(student);

        parentStudentRelationRepository.save(relation);

        AuditLog log = new AuditLog();
        log.setAction("MAP");
        log.setEntityName("PARENT_STUDENT");
        log.setEntityId(relation.getRelId());
        log.setUserId(admin.getUserId());
        log.setCreatedTime(LocalDateTime.now());
        log.setIpAddress(request.getRemoteAddr());

        auditLogRepository.save(log);
        
        // ✅ SUCCESS
        markAuditStatus(admin.getUserId(), true);
        
       }catch (RuntimeException ex) {

           // ❌ FAILURE
           markAuditStatus(admin.getUserId(), false);

           throw ex;
       }
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAuditStatus(Long userId, Boolean status) {
        systemSettingsRepository.findByUserId(userId)
                .ifPresent(settings -> {
                    settings.setEnableAuditLog(status);
                    systemSettingsRepository.save(settings);
                });
    }

}
