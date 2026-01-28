package com.lms.www.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.UserAuthorizationUtil;
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
import com.lms.www.repository.AddressRepository;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.InstructorRepository;
import com.lms.www.repository.LoginHistoryRepository;
import com.lms.www.repository.ParentRepository;
import com.lms.www.repository.ParentStudentRelationRepository;
import com.lms.www.repository.PasswordResetTokenRepository;
import com.lms.www.repository.StudentRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserSessionRepository;
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
    private final ApplicationContext applicationContext;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AddressRepository addressRepository;
    private final UserSessionRepository userSessionRepository;



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
            SystemSettingsRepository systemSettingsRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            ApplicationContext applicationContext,
            AddressRepository addressRepository,
            UserSessionRepository userSessionRepository
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
        this.applicationContext = applicationContext;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.addressRepository = addressRepository;
        this.userSessionRepository = userSessionRepository;
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

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        if (password.length() < 10) {
            throw new RuntimeException("Password must be at least 10 characters");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        String rawPassword = password; 
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setEnabled(true);
        user.setRoleName(roleName);
        
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("Phone number already in use");
        }

        user = userRepository.save(user);
        
        emailService.sendAccountCredentialsMail(user, rawPassword);
        emailService.sendRegistrationMail(user, user.getRoleName());


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
        settings.setPasswordLastUpdatedAt(LocalDateTime.now());
        settings.setUpdatedTime(LocalDateTime.now());

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

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "STUDENT", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
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

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "INSTRUCTOR", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
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

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "PARENT", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }

    // ===================== READ =====================
    @Override public List<User> getAllUsers() { return userRepository.findAll(); }
    @Override public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
            
         // 🔒 BLOCK ADMIN → SUPER ADMIN
            UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(
                    admin,
                    existing
            );
            
            if (updatedUser.getFirstName() != null) existing.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getLastName() != null) existing.setLastName(updatedUser.getLastName());
            if (updatedUser.getPhone() != null) existing.setPhone(updatedUser.getPhone());

            userRepository.save(existing);
            emailService.sendProfileUpdatedMail(existing);
            proxy().markAuditStatus(admin.getUserId(), true);
            audit("UPDATE", "USER", userId, admin, request);

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }

    @Override
    public void deleteUser(Long userId, User requester, HttpServletRequest request) {
        throw new RuntimeException(
                "User deletion is not possible. Use account enable/disable instead."
        );
    }

    @Override
    public void mapParentToStudent(Long parentId, Long studentId, User admin, HttpServletRequest request) {
        try {
            Parent parent = parentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            ParentStudentRelation relation = new ParentStudentRelation();
            relation.setParent(parent);
            relation.setStudent(student);
            parentStudentRelationRepository.save(relation);
            
            emailService.sendParentStudentMappingMailToParent(
                    parent.getUser(),
                    student.getUser()
            );

            emailService.sendParentStudentMappingMailToStudent(
                    student.getUser(),
                    parent.getUser()
            );


            proxy().markAuditStatus(admin.getUserId(), true);
            audit("MAP", "PARENT_STUDENT", relation.getRelId(), admin, request);

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }

    // ===================== AUDIT FLAG =====================
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAuditStatus(Long userId, Boolean status) {
        systemSettingsRepository.findByUserId(userId)
                .ifPresent(settings -> {
                    settings.setEnableAuditLog(status);
                    systemSettingsRepository.save(settings);
                });
    }

    private AdminServiceImpl proxy() {
        return applicationContext.getBean(AdminServiceImpl.class);
    }

    @Override
    public void setUserEnabled(
            Long userId,
            boolean enabled,
            User requester,
            HttpServletRequest request
    ) {
        try {
            User targetUser = getUserByUserId(userId);

            // 🔒 RULE 1: ADMIN cannot touch SUPER_ADMIN
            if ("ROLE_ADMIN".equals(requester.getRoleName())
                    && "ROLE_SUPER_ADMIN".equals(targetUser.getRoleName())) {
                throw new RuntimeException("Admin cannot modify Super Admin");
            }

            // 🔒 RULE 2: NO ONE can disable SUPER_ADMIN
            if (!enabled && "ROLE_SUPER_ADMIN".equals(targetUser.getRoleName())) {
                throw new RuntimeException("Super Admin cannot be disabled");
            }

            // ✅ UPDATE STATUS
            targetUser.setEnabled(enabled);
            userRepository.save(targetUser);

            // 📧 EMAIL
            emailService.sendAccountStatusMail(
                    targetUser,
                    enabled ? "ACCOUNT ENABLED" : "ACCOUNT DISABLED"
            );

            // ✅ AUDIT
            proxy().markAuditStatus(requester.getUserId(), true);
            audit(
                    enabled ? "ENABLE" : "DISABLE",
                    "USER",
                    userId,
                    requester,
                    request
            );

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(requester.getUserId(), false);
            throw ex;
        }
    }

    
    @Override
    public void updateMultiSessionAccess(
            Long userId,
            boolean allowMultiSession,
            User admin,
            HttpServletRequest request
    ) {
        try {

            User user = getUserByUserId(userId);

            // 🔒 BLOCK NON-SUPER-ADMIN → SUPER-ADMIN
            UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(
                    admin,
                    user
            );

            SystemSettings settings = systemSettingsRepository
                    .findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("System settings not found"));

            settings.setMultiSession(allowMultiSession);
            settings.setUpdatedTime(LocalDateTime.now());
            systemSettingsRepository.save(settings);

            emailService.sendMultiSessionStatusMail(user, allowMultiSession);

            proxy().markAuditStatus(admin.getUserId(), true);
            audit(
                    allowMultiSession ? "MULTI_SESSION_ENABLE" : "MULTI_SESSION_DISABLE",
                    "SYSTEM_SETTINGS",
                    userId,
                    admin,
                    request
            );

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }


}
