package com.lms.www.service;

import java.util.List;

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
import com.lms.www.model.Student;
import com.lms.www.model.TransportManager;
import com.lms.www.model.User;
import com.lms.www.model.Warden;

import jakarta.servlet.http.HttpServletRequest;

public interface AdminService {

	void createStudent(StudentRequest request, User admin, HttpServletRequest httpRequest);
    void createInstructor(InstructorRequest request, User admin, HttpServletRequest httpRequest);
    void createParent(ParentRequest request, User admin, HttpServletRequest httpRequest);
    void createDriver(DriverRequest request, User admin, HttpServletRequest httpRequest);
    void createConductor(ConductorRequest request, User admin, HttpServletRequest httpRequest);
    void createAccountant(AccountantRequest request, User admin, HttpServletRequest httpRequest);
    void createAffiliate(AffiliateRequest request, User admin, HttpServletRequest httpRequest);
    void createCommunityManager(CommunityManagerRequest request, User admin, HttpServletRequest httpRequest);
    void createDepartmentHead(DepartmentHeadRequest request, User admin, HttpServletRequest httpRequest);
    void createEvaluator(EvaluatorRequest request, User admin, HttpServletRequest httpRequest);
    void createInventoryManager(InventoryManagerRequest request, User admin, HttpServletRequest httpRequest);
    void createLibrarian(LibrarianRequest request, User admin, HttpServletRequest httpRequest);
    void createMarketingManager(MarketingManagerRequest request, User admin, HttpServletRequest httpRequest);
    void createMentor(MentorRequest request, User admin, HttpServletRequest httpRequest);
    void createTransportManager(TransportManagerRequest request, User admin, HttpServletRequest httpRequest);
    void createWarden(WardenRequest request, User admin, HttpServletRequest httpRequest);
    
    List<User> getAllUsers();
    User getUserByUserId(Long userId);
    User getUserByEmail(String email);

    List<Student> getAllStudents();
    List<Parent> getAllParents();
    List<Instructor> getAllInstructors();
    List<Driver> getAllDrivers();
    List<Conductor> getAllConductors();
    List<Accountant> getAllAccountants();
    List<Affiliate> getAllAffiliates();
    List<CommunityManager> getAllCommunityManagers();
    List<DepartmentHead> getAllDepartmentHeads();
    List<Evaluator> getAllEvaluators();
    List<InventoryManager> getAllInventoryManagers();
    List<Librarian> getAllLibrarians();
    List<MarketingManager> getAllMarketingManagers();
    List<Mentor> getAllMentors();
    List<TransportManager> getAllTransportManagers();
    List<Warden> getAllWardens();

    Student getStudentByStudentId(Long studentId);
    Parent getParentByParentId(Long parentId);
    Instructor getInstructorByInstructorId(Long instructorId);
    Driver getDriverByDriverId(Long driverId);
    Conductor getConductorByConductorId(Long conductorId);
    Accountant getAccountantByAccountantId(Long accountantId);
    Affiliate getAffiliateByAffiliateId(Long affiliateId);
    CommunityManager getCommunityManagerByCommunityManagerId(Long communityManagerId);
    DepartmentHead getDepartmentHeadByDepartmentHeadId(Long departmentHeadId);
    Evaluator getEvaluatorByEvaluatorId(Long evaluatorId);
    InventoryManager getInventoryManagerByInventoryManagerId(Long inventoryManagerId);
    Librarian getLibrarianByLibrarianId(Long librarianId);
    MarketingManager getMarketingManagerByMarketingManagerId(Long marketingManagerId);
    Mentor getMentorByMentorId(Long mentorId);
    TransportManager getTransportManagerByTransportManagerId(Long transportManagerId);
    Warden getWardenByWardenId(Long wardenId);
    
    void updateUser(Long userId, User updatedUser, User admin, HttpServletRequest request);
    void deleteUser(Long userId, User admin, HttpServletRequest request);

    void mapParentToStudent(
            Long parentId,
            Long studentId,
            User admin,
            HttpServletRequest request
    );

    void setUserEnabled(
            Long userId,
            boolean enabled,
            User admin,
            HttpServletRequest request
    );
    
    void updateMultiSessionAccess(
            Long userId,
            boolean allowMultiSession,
            User admin,
            HttpServletRequest request
    );
    
    void updateMultiSessionAccessByRole(
            String roleName,
            boolean allowMultiSession,
            User admin,
            HttpServletRequest request
    );

    void unlockUser(Long userId, User requester, HttpServletRequest request);
    
    void addPermissionsToUser(
            Long userId,
            List<String> permissions,
            User admin,
            HttpServletRequest request
    );

}
