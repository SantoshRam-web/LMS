package com.lms.www.service.Impl;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lms.www.config.JwtUtil;
import com.lms.www.config.UserAuthorizationUtil;
import com.lms.www.controller.request.AccountantRequest;
import com.lms.www.controller.request.AffiliateRequest;
import com.lms.www.controller.request.CommunityManagerRequest;
import com.lms.www.controller.request.ConductorRequest;
import com.lms.www.controller.request.DepartmentHeadRequest;
import com.lms.www.controller.request.DriverRequest;
import com.lms.www.controller.request.EvaluatorRequest;
import com.lms.www.controller.request.InstructorRequest;
import com.lms.www.controller.request.InventoryManagerRequest;
import com.lms.www.controller.request.LibrarianRequest;
import com.lms.www.controller.request.MarketingManagerRequest;
import com.lms.www.controller.request.MentorRequest;
import com.lms.www.controller.request.ParentRequest;
import com.lms.www.controller.request.StudentRequest;
import com.lms.www.controller.request.TransportManagerRequest;
import com.lms.www.controller.request.WardenRequest;
import com.lms.www.model.Accountant;
import com.lms.www.model.Affiliate;
import com.lms.www.model.AuditLog;
import com.lms.www.model.CommunityManager;
import com.lms.www.model.Conductor;
import com.lms.www.model.DepartmentHead;
import com.lms.www.model.Driver;
import com.lms.www.model.Evaluator;
import com.lms.www.model.Instructor;
import com.lms.www.model.InventoryManager;
import com.lms.www.model.Librarian;
import com.lms.www.model.MarketingManager;
import com.lms.www.model.Mentor;
import com.lms.www.model.Parent;
import com.lms.www.model.ParentStudentRelation;
import com.lms.www.model.Student;
import com.lms.www.model.SystemSettings;
import com.lms.www.model.TransportManager;
import com.lms.www.model.User;
import com.lms.www.model.UserPermission;
import com.lms.www.model.Warden;
import com.lms.www.repository.AccountantRepository;
import com.lms.www.repository.AddressRepository;
import com.lms.www.repository.AffiliateRepository;
import com.lms.www.repository.AuditLogRepository;
import com.lms.www.repository.CommunityManagerRepository;
import com.lms.www.repository.ConductorRepository;
import com.lms.www.repository.DepartmentHeadRepository;
import com.lms.www.repository.DriverRepository;
import com.lms.www.repository.EvaluatorRepository;
import com.lms.www.repository.InstructorRepository;
import com.lms.www.repository.InventoryManagerRepository;
import com.lms.www.repository.LibrarianRepository;
import com.lms.www.repository.LoginHistoryRepository;
import com.lms.www.repository.MarketingManagerRepository;
import com.lms.www.repository.MentorRepository;
import com.lms.www.repository.ParentRepository;
import com.lms.www.repository.ParentStudentRelationRepository;
import com.lms.www.repository.PasswordResetTokenRepository;
import com.lms.www.repository.StudentRepository;
import com.lms.www.repository.SystemSettingsRepository;
import com.lms.www.repository.TransportManagerRepository;
import com.lms.www.repository.UserPermissionRepository;
import com.lms.www.repository.UserRepository;
import com.lms.www.repository.UserSessionRepository;
import com.lms.www.repository.WardenRepository;
import com.lms.www.service.AdminService;
import com.lms.www.service.EmailService;
import com.lms.www.service.FailedLoginAttemptService;
import com.lms.www.tenant.TenantResolver;

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
    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;
    private final JwtUtil jwtUtil;
    private final TenantResolver tenantResolver;
    private final FailedLoginAttemptService failedLoginAttemptService;
    private final UserPermissionRepository userPermissionRepository; 
    private final AccountantRepository accountantRepository;
    private final AffiliateRepository affiliateRepository;
    private final CommunityManagerRepository communityManagerRepository;
    private final DepartmentHeadRepository departmentHeadRepository;
    private final EvaluatorRepository evaluatorRepository;
    private final InventoryManagerRepository inventoryManagerRepository;
    private final LibrarianRepository librarianRepository;
    private final MarketingManagerRepository marketingManagerRepository;
    private final MentorRepository mentorRepository;
    private final TransportManagerRepository transportManagerRepository;
    private final WardenRepository wardenRepository;

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
            UserSessionRepository userSessionRepository,
            DriverRepository driverRepository,
            ConductorRepository conductorRepository,
            JwtUtil jwtUtil,
            TenantResolver tenantResolver,
            FailedLoginAttemptService failedLoginAttemptService,
            UserPermissionRepository userPermissionRepository,
            AccountantRepository accountantRepository,
            AffiliateRepository affiliateRepository,
            CommunityManagerRepository communityManagerRepository,
            DepartmentHeadRepository departmentHeadRepository,
            EvaluatorRepository evaluatorRepository,
            InventoryManagerRepository inventoryManagerRepository,
            LibrarianRepository librarianRepository,
            MarketingManagerRepository marketingManagerRepository,
            MentorRepository mentorRepository,
            TransportManagerRepository transportManagerRepository,
            WardenRepository wardenRepository
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
        this.driverRepository = driverRepository;
        this.conductorRepository = conductorRepository;
        this.jwtUtil = jwtUtil;
        this.tenantResolver = tenantResolver;
        this.failedLoginAttemptService = failedLoginAttemptService;
        this.userPermissionRepository = userPermissionRepository;
        this.accountantRepository = accountantRepository;
        this.affiliateRepository = affiliateRepository;
        this.communityManagerRepository = communityManagerRepository;
        this.departmentHeadRepository = departmentHeadRepository;
        this.evaluatorRepository = evaluatorRepository;
        this.inventoryManagerRepository = inventoryManagerRepository;
        this.librarianRepository = librarianRepository;
        this.marketingManagerRepository = marketingManagerRepository;
        this.mentorRepository = mentorRepository;
        this.transportManagerRepository = transportManagerRepository;
        this.wardenRepository = wardenRepository;
    }

    // ===================== COMMON =====================
    private User createBaseUser(
            String firstName,
            String lastName,
            String email,
            String password,
            String phone,
            String roleName,
            User admin,
            HttpServletRequest request
    ) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Authorization header");
        }

        String token = authHeader.substring(7);

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

        // 🔥 FIXED PART
        String tenantDb = jwtUtil.extractTenantDb(token);
        String tenantDomain = tenantResolver.resolveTenantDomain(tenantDb);
        String loginUrl = tenantResolver.buildTenantLoginUrl(tenantDomain);

        emailService.sendAccountCredentialsMail(user, rawPassword, loginUrl);
        emailService.sendRegistrationMail(user, user.getRoleName());

        SystemSettings settings = new SystemSettings();
        settings.setUserId(user.getUserId());
        settings.setMaxLoginAttempts(3L);
        settings.setAccLockDuration(30L);
        settings.setPassExpiryDays(60L);
        settings.setPassLength(10L);
        settings.setJwtExpiryMins(60L);
        settings.setSessionTimeout(360L);
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
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Student student = new Student();
            student.setUser(user);
            student.setDob(request.getDob());
            student.setGender(request.getGender());
            studentRepository.save(student);
            
            saveUserPermissions(user, request.getPermissions());
            
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
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Instructor instructor = new Instructor();
            instructor.setUser(user);
            instructorRepository.save(instructor);
            
            saveUserPermissions(user, request.getPermissions());

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
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Parent parent = new Parent();
            parent.setUser(user);
            parentRepository.save(parent);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "PARENT", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createDriver(DriverRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Driver driver = new Driver();
            driver.setUser(user);
            driverRepository.save(driver);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "DRIVER", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createConductor(ConductorRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Conductor conductor = new Conductor();
            conductor.setUser(user);
            conductorRepository.save(conductor);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "CONDUCTOR", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createAccountant(AccountantRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Accountant accountant = new Accountant();
            accountant.setUser(user);
            accountantRepository.save(accountant);
            
            saveUserPermissions(user, request.getPermissions());
            
            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "ACCOUNTANT", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createAffiliate(AffiliateRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Affiliate affiliate = new Affiliate();
            affiliate.setUser(user);
            affiliateRepository.save(affiliate);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "AFFILIATE", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createCommunityManager(CommunityManagerRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            CommunityManager communityManager = new CommunityManager();
            communityManager.setUser(user);
            communityManagerRepository.save(communityManager);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "COMMUNITY MANAGER", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createDepartmentHead(DepartmentHeadRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            DepartmentHead departmentHead = new DepartmentHead();
            departmentHead.setUser(user);
            departmentHeadRepository.save(departmentHead);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "DEPARTMENT HEAD", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createEvaluator(EvaluatorRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Evaluator evaluator = new Evaluator();
            evaluator.setUser(user);
            evaluatorRepository.save(evaluator);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "EVALUATOR", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createInventoryManager(InventoryManagerRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            InventoryManager inventoryManager = new InventoryManager();
            inventoryManager.setUser(user);
            inventoryManagerRepository.save(inventoryManager);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "INVENTORY MANAGER", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createLibrarian(LibrarianRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Librarian librarian = new Librarian();
            librarian.setUser(user);
            librarianRepository.save(librarian);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "LIBRARIAN", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createMarketingManager(MarketingManagerRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            MarketingManager marketingManager = new MarketingManager();
            marketingManager.setUser(user);
            marketingManagerRepository.save(marketingManager);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "MARKETING MANAGER", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createMentor(MentorRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Mentor mentor = new Mentor();
            mentor.setUser(user);
            mentorRepository.save(mentor);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "MENTOR", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createTransportManager(TransportManagerRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            TransportManager transportManager = new TransportManager();
            transportManager.setUser(user);
            transportManagerRepository.save(transportManager);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "TRANSPORT MANAGER", user.getUserId(), admin, httpRequest);
            emailService.sendRegistrationMail(user, user.getRoleName());

        } catch (RuntimeException ex) {
            proxy().markAuditStatus(admin.getUserId(), false);
            throw ex;
        }
    }
    
    @Override
    public void createWarden(WardenRequest request, User admin, HttpServletRequest httpRequest) {
        try {
            User user = createBaseUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getRoleName(),
                    admin,
                    httpRequest
            );

            Warden warden = new Warden();
            warden.setUser(user);
            wardenRepository.save(warden);
            
            saveUserPermissions(user, request.getPermissions());

            proxy().markAuditStatus(admin.getUserId(), true);
            audit("CREATE", "WARDEN", user.getUserId(), admin, httpRequest);
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
    @Override public List<Driver> getAllDrivers() { return driverRepository.findAll(); }
    @Override public List<Conductor> getAllConductors() { return conductorRepository.findAll(); }
    @Override public List<Accountant> getAllAccountants() { return accountantRepository.findAll(); }
    @Override public List<Affiliate> getAllAffiliates() { return affiliateRepository.findAll(); }
    @Override public List<CommunityManager> getAllCommunityManagers() { return communityManagerRepository.findAll(); }
    @Override public List<DepartmentHead> getAllDepartmentHeads() { return departmentHeadRepository.findAll(); }
    @Override public List<Evaluator> getAllEvaluators() { return evaluatorRepository.findAll(); }
    @Override public List<InventoryManager> getAllInventoryManagers() { return inventoryManagerRepository.findAll(); }
    @Override public List<Librarian> getAllLibrarians() { return librarianRepository.findAll(); }
    @Override public List<MarketingManager> getAllMarketingManagers() { return marketingManagerRepository.findAll(); }
    @Override public List<Mentor> getAllMentors() { return mentorRepository.findAll(); }
    @Override public List<TransportManager> getAllTransportManagers() { return transportManagerRepository.findAll(); }
    @Override public List<Warden> getAllWardens() { return wardenRepository.findAll(); }
    
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
    
    @Override
    public Driver getDriverByDriverId(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }  
    
    @Override
    public Conductor getConductorByConductorId(Long conductorId) {
        return conductorRepository.findById(conductorId)
                .orElseThrow(() -> new RuntimeException("Conductor not found"));
    }  
    
    @Override
    public Accountant getAccountantByAccountantId(Long accountantId) {
        return accountantRepository.findById(accountantId)
                .orElseThrow(() -> new RuntimeException("Accountant not found"));
    } 
    
    @Override
    public Affiliate getAffiliateByAffiliateId(Long affiliateId) {
        return affiliateRepository.findById(affiliateId)
                .orElseThrow(() -> new RuntimeException("Affiliate not found"));
    } 
    
    @Override
    public CommunityManager getCommunityManagerByCommunityManagerId(Long communityManagerId) {
        return communityManagerRepository.findById(communityManagerId)
                .orElseThrow(() -> new RuntimeException("Community Manager not found"));
    } 
    
    @Override
    public DepartmentHead getDepartmentHeadByDepartmentHeadId(Long departmentHeadId) {
        return departmentHeadRepository.findById(departmentHeadId)
                .orElseThrow(() -> new RuntimeException("Department Head not found"));
    } 
    
    @Override
    public Evaluator getEvaluatorByEvaluatorId(Long evaluatorId) {
        return evaluatorRepository.findById(evaluatorId)
                .orElseThrow(() -> new RuntimeException("Evaluator not found"));
    } 
    
    @Override
    public InventoryManager getInventoryManagerByInventoryManagerId(Long inventoryManagerId) {
        return inventoryManagerRepository.findById(inventoryManagerId)
                .orElseThrow(() -> new RuntimeException("Inventory Manager not found"));
    } 
    
    @Override
    public Librarian getLibrarianByLibrarianId(Long librarianId) {
        return librarianRepository.findById(librarianId)
                .orElseThrow(() -> new RuntimeException("Librarian not found"));
    } 
    
    @Override
    public MarketingManager getMarketingManagerByMarketingManagerId(Long marketingManagerId) {
        return marketingManagerRepository.findById(marketingManagerId)
                .orElseThrow(() -> new RuntimeException("Marketing Manager not found"));
    } 
    
    @Override
    public Mentor getMentorByMentorId(Long mentorId) {
        return mentorRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
    } 
    
    @Override
    public TransportManager getTransportManagerByTransportManagerId(Long transportManagerId) {
        return transportManagerRepository.findById(transportManagerId)
                .orElseThrow(() -> new RuntimeException("Transport Manager not found"));
    } 
    
    @Override
    public Warden getWardenByWardenId(Long wardenId) {
        return wardenRepository.findById(wardenId)
                .orElseThrow(() -> new RuntimeException("Warden not found"));
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
            
            UserAuthorizationUtil.assertAdminCannotTouchAdmin(admin, existing);

            
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
            
            UserAuthorizationUtil.assertAdminCannotTouchAdmin(requester, targetUser);

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
    @Transactional
    public void updateMultiSessionAccessByRole(
            String roleName,
            boolean allowMultiSession,
            User admin,
            HttpServletRequest request
    ) {

        // 🔒 Only SUPER_ADMIN can change ADMIN role
        if ("ROLE_ADMIN".equals(roleName)
                && !"ROLE_SUPER_ADMIN".equals(admin.getRoleName())) {
            throw new RuntimeException("Only Super Admin can update Admin role");
        }

        List<User> users = userRepository.findByRoleName(roleName);

        for (User user : users) {

            // 🔒 Admin cannot touch Super Admin
            UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(admin, user);

            // 🔒 Admin cannot touch Admin
            UserAuthorizationUtil.assertAdminCannotTouchAdmin(admin, user);

            systemSettingsRepository.findByUserId(user.getUserId())
                    .ifPresent(settings -> {
                        settings.setMultiSession(allowMultiSession);
                        settings.setUpdatedTime(LocalDateTime.now());
                        systemSettingsRepository.save(settings);
                    });

            // 📧 EMAIL (reuse existing mail)
            emailService.sendMultiSessionStatusMail(user, allowMultiSession);

            // 📝 AUDIT
            proxy().markAuditStatus(admin.getUserId(), true);
            audit(
                    allowMultiSession
                            ? "MULTI_SESSION_ENABLE_ROLE"
                            : "MULTI_SESSION_DISABLE_ROLE",
                    "SYSTEM_SETTINGS",
                    user.getUserId(),
                    admin,
                    request
            );
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
            
            UserAuthorizationUtil.assertAdminCannotTouchAdmin(admin, user);

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

    @Override
    @Transactional
    public void unlockUser(Long userId, User requester, HttpServletRequest request) {

        User targetUser = getUserByUserId(userId);

        // 🔒 ONLY SUPER ADMIN
        if (!"ROLE_SUPER_ADMIN".equals(requester.getRoleName())) {
            throw new RuntimeException("Only Super Admin can unlock accounts");
        }

        // 🔥 CORE UNLOCK LOGIC
        failedLoginAttemptService.clearAttempts(userId);

        // 📧 Notify user
        emailService.sendAccountUnlockedMail(
                targetUser.getEmail(),
                LocalDateTime.now()
        );

        // ✅ AUDIT
        audit(
                "ACCOUNT_UNLOCK",
                "USER",
                userId,
                requester,
                request
        );
    }
    
    private void saveUserPermissions(
            User user,
            List<String> permissions
    ) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }

        // 🔒 Super Admin has implicit permissions
        if ("ROLE_SUPER_ADMIN".equals(user.getRoleName())) {
            return;
        }

        for (String permission : permissions) {
            if (permission == null || permission.isBlank()) {
                continue;
            }

            boolean exists = userPermissionRepository
                    .existsByUser_UserIdAndPermissionName(
                            user.getUserId(),
                            permission
                    );

            if (!exists) {
                UserPermission up = new UserPermission();
                up.setUser(user);
                up.setPermissionName(permission.trim());
                userPermissionRepository.save(up);
            }
        }
    }
    
    @Override
    public void addPermissionsToUser(
            Long userId,
            List<String> permissions,
            User admin,
            HttpServletRequest request
    ) {
        User user = getUserByUserId(userId);

        UserAuthorizationUtil.assertAdminCannotTouchAdmin(admin, user);
        UserAuthorizationUtil.assertAdminCannotTouchSuperAdmin(admin, user);

        saveUserPermissions(user, permissions);

        audit("ADD_PERMISSIONS", "USER", userId, admin, request);
    }

}
