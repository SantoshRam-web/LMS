package com.lms.www.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.lms.www.controller.request.UserPermissionRequest;
import com.lms.www.controller.request.WardenRequest;
import com.lms.www.model.Address;
import com.lms.www.model.User;
import com.lms.www.service.AccountUnlockService;
import com.lms.www.service.AddressService;
import com.lms.www.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AddressService addressService;
    private final AccountUnlockService accountUnlockService;

    public AdminController(AdminService adminService, AddressService addressService,
    		AccountUnlockService accountUnlockService) {
        this.adminService = adminService;
        this.addressService = addressService;
        this.accountUnlockService = accountUnlockService; 
    }


    // ---------- CREATE ----------
    @PostMapping("/students")
    public ResponseEntity<String> createStudent(
            @RequestBody StudentRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createStudent(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Student created successfully");
    }

    @PostMapping("/instructors")
    public ResponseEntity<String> createInstructor(
            @RequestBody InstructorRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createInstructor(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Instructor created successfully");
    }

    @PostMapping("/parents")
    public ResponseEntity<String> createParent(
            @RequestBody ParentRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createParent(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Parent created successfully");
    }
    
    @PostMapping("/drivers")
    public ResponseEntity<String> createDriver(
            @RequestBody DriverRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createDriver(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Driver created successfully");
    }
    
    @PostMapping("/conductors")
    public ResponseEntity<String> createConductor(
            @RequestBody ConductorRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createConductor(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Conductor created successfully");
    }
    
    @PostMapping("/accountants")
    public ResponseEntity<String> createAccountant(
            @RequestBody AccountantRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createAccountant(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Accountant created successfully");
    }
    
    @PostMapping("/affiliates")
    public ResponseEntity<String> createAffiliate(
            @RequestBody AffiliateRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createAffiliate(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Affiliate created successfully");
    }
    
    @PostMapping("/community-manager")
    public ResponseEntity<String> createCommunityManager(
            @RequestBody CommunityManagerRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createCommunityManager(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Community Manager created successfully");
    }
    
    @PostMapping("/department-head")
    public ResponseEntity<String> createDepartmentHead(
            @RequestBody DepartmentHeadRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createDepartmentHead(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Department Head created successfully");
    }
    
    @PostMapping("/evaluator")
    public ResponseEntity<String> createEvaluator(
            @RequestBody EvaluatorRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createEvaluator(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Evaluator created successfully");
    }
    
    @PostMapping("/inventory-manager")
    public ResponseEntity<String> createInventoryManager(
            @RequestBody InventoryManagerRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createInventoryManager(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Inventory Manager created successfully");
    }
    
    @PostMapping("/librarian")
    public ResponseEntity<String> createLibrarian(
            @RequestBody LibrarianRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createLibrarian(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Librarian created successfully");
    }
    
    @PostMapping("/marketing-manager")
    public ResponseEntity<String> createMarketingManager(
            @RequestBody MarketingManagerRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createMarketingManager(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Marketing Manager created successfully");
    }
    
    @PostMapping("/mentor")
    public ResponseEntity<String> createMentor(
            @RequestBody MentorRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createMentor(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Mentor created successfully");
    }
    
    @PostMapping("/transport-manager")
    public ResponseEntity<String> createTransportManager(
            @RequestBody TransportManagerRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createTransportManager(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Transport Manager created successfully");
    }
    
    @PostMapping("/warden")
    public ResponseEntity<String> createWarden(
            @RequestBody WardenRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.createWarden(request, getLoggedInUser(), httpRequest);
        return ResponseEntity.ok("Warden created successfully");
    }

    // ---------- READ ----------
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserByUserId(userId));
    }


    // ---------- HELPER ----------
    private User getLoggedInUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return adminService.getUserByEmail(email);
    }
    
    @PatchMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody User updatedUser,
            HttpServletRequest request
    ) {
        adminService.updateUser(userId, updatedUser, getLoggedInUser(), request);
        return ResponseEntity.ok("User updated");
    }
    
 // ---------- STUDENTS ----------
    @GetMapping("/getstudents")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @GetMapping("/getstudents/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(adminService.getStudentByStudentId(studentId));
    }

    // ---------- PARENTS ----------
    @GetMapping("/getparents")
    public ResponseEntity<?> getAllParents() {
        return ResponseEntity.ok(adminService.getAllParents());
    }

    @GetMapping("/getparents/{parentId}")
    public ResponseEntity<?> getParentById(@PathVariable Long parentId) {
        return ResponseEntity.ok(adminService.getParentByParentId(parentId));
    }

    // ---------- INSTRUCTORS ----------
    @GetMapping("/getinstructors")
    public ResponseEntity<?> getAllInstructors() {
        return ResponseEntity.ok(adminService.getAllInstructors());
    }

    @GetMapping("/getinstructors/{instructorId}")
    public ResponseEntity<?> getInstructorById(@PathVariable Long instructorId) {
        return ResponseEntity.ok(adminService.getInstructorByInstructorId(instructorId));
    }
    
    // ---------- DRIVERS ----------
    @GetMapping("/getdrivers")
    public ResponseEntity<?> getAllDrivers() {
        return ResponseEntity.ok(adminService.getAllDrivers());
    }

    @GetMapping("/getdrivers/{driverId}")
    public ResponseEntity<?> getDriverById(@PathVariable Long driverId) {
        return ResponseEntity.ok(adminService.getDriverByDriverId(driverId));
    }
    
    // ---------- CONDUCTORS ----------
    @GetMapping("/getconductors")
    public ResponseEntity<?> getAllConductors() {
        return ResponseEntity.ok(adminService.getAllConductors());
    }

    @GetMapping("/getconductors/{conductorId}")
    public ResponseEntity<?> getConductorById(@PathVariable Long conductorId) {
        return ResponseEntity.ok(adminService.getConductorByConductorId(conductorId));
    }
    
 // ---------- ACCOUNTANTS ----------
    @GetMapping("/getaccountants")
    public ResponseEntity<?> getAllAccountants() {
        return ResponseEntity.ok(adminService.getAllAccountants());
    }

    @GetMapping("/getaccountants/{accountantId}")
    public ResponseEntity<?> getAccountantById(@PathVariable Long accountantId) {
        return ResponseEntity.ok(adminService.getAccountantByAccountantId(accountantId));
    }
    
    // ---------- AFFILIATE ----------
    @GetMapping("/getaffiliates")
    public ResponseEntity<?> getAllAffiliates() {
        return ResponseEntity.ok(adminService.getAllAffiliates());
    }

    @GetMapping("/getaffiliates/{affiliateId}")
    public ResponseEntity<?> getAffiliateById(@PathVariable Long affiliateId) {
        return ResponseEntity.ok(adminService.getAffiliateByAffiliateId(affiliateId));
    }
    
    // ---------- COMMUNITY-MANAGER ----------
    @GetMapping("/getcommunitymanagers")
    public ResponseEntity<?> getAllCommunityManagers() {
        return ResponseEntity.ok(adminService.getAllCommunityManagers());
    }

    @GetMapping("/getcommunitymanagers/{communityManagerId}")
    public ResponseEntity<?> getCommunityManagerById(@PathVariable Long communityManagerId) {
        return ResponseEntity.ok(adminService.getCommunityManagerByCommunityManagerId(communityManagerId));
    }
    
    // ---------- DEPARTMENT-HEAD ----------
    @GetMapping("/getdepartmentheads")
    public ResponseEntity<?> getAllDepartmentHeads() {
        return ResponseEntity.ok(adminService.getAllDepartmentHeads());
    }

    @GetMapping("/getdepartmentheads/{departmentHeadId}")
    public ResponseEntity<?> getDepartmentHeadsById(@PathVariable Long departmentHeadId) {
        return ResponseEntity.ok(adminService.getDepartmentHeadByDepartmentHeadId(departmentHeadId));
    }
    
    // ---------- EVALUATOR ----------
    @GetMapping("/getevaluators")
    public ResponseEntity<?> getAllEvaluators() {
        return ResponseEntity.ok(adminService.getAllEvaluators());
    }

    @GetMapping("/getevaluators/{evaluatorId}")
    public ResponseEntity<?> getEvaluatorById(@PathVariable Long evaluatorId) {
        return ResponseEntity.ok(adminService.getEvaluatorByEvaluatorId(evaluatorId));
    }
    
    // ---------- INVENTORY-MANAGER ----------
    @GetMapping("/getinventorymanagers")
    public ResponseEntity<?> getAllInventoryManagers() {
        return ResponseEntity.ok(adminService.getAllInventoryManagers());
    }

    @GetMapping("/getinventorymanagers/{inventoryManagerId}")
    public ResponseEntity<?> getInventoryManagerById(@PathVariable Long inventoryManagerId) {
        return ResponseEntity.ok(adminService.getInventoryManagerByInventoryManagerId(inventoryManagerId));
    }
    
    // ---------- LIBRARIAN ----------
    @GetMapping("/getlibrarians")
    public ResponseEntity<?> getAllLibrarians() {
        return ResponseEntity.ok(adminService.getAllLibrarians());
    }

    @GetMapping("/getlibrarians/{librarianId}")
    public ResponseEntity<?> getLibrarianById(@PathVariable Long librarianId) {
        return ResponseEntity.ok(adminService.getLibrarianByLibrarianId(librarianId));
    }
    
    // ---------- MARKETING-MANAGER ----------
    @GetMapping("/getmarketingmanagers")
    public ResponseEntity<?> getAllMarketingManagers() {
        return ResponseEntity.ok(adminService.getAllMarketingManagers());
    }

    @GetMapping("/getmarketingmanagers/{marketingManagerId}")
    public ResponseEntity<?> getMarketingManagerById(@PathVariable Long marketingManagerId) {
        return ResponseEntity.ok(adminService.getMarketingManagerByMarketingManagerId(marketingManagerId));
    }
    
    // ---------- MENTOR ----------
    @GetMapping("/getmentors")
    public ResponseEntity<?> getAllMentors() {
        return ResponseEntity.ok(adminService.getAllMentors());
    }

    @GetMapping("/getmentors/{mentorId}")
    public ResponseEntity<?> getMentorById(@PathVariable Long mentorId) {
        return ResponseEntity.ok(adminService.getMentorByMentorId(mentorId));
    }
    
    // ---------- TRANSPORT-MANAGER ----------
    @GetMapping("/gettransportmanagers")
    public ResponseEntity<?> getAllTransportManagers() {
        return ResponseEntity.ok(adminService.getAllTransportManagers());
    }

    @GetMapping("/gettransportmanagers/{transportManagerId}")
    public ResponseEntity<?> getTransportManagerById(@PathVariable Long transportManagerId) {
        return ResponseEntity.ok(adminService.getTransportManagerByTransportManagerId(transportManagerId));
    }
    
    // ---------- WARDEN ----------
    @GetMapping("/getwardens")
    public ResponseEntity<?> getAllWardens() {
        return ResponseEntity.ok(adminService.getAllWardens());
    }

    @GetMapping("/getwardens/{wardenId}")
    public ResponseEntity<?> getWardenById(@PathVariable Long wardenId) {
        return ResponseEntity.ok(adminService.getWardenByWardenId(wardenId));
    }

    // ---------- MAP ----------
    @PostMapping("/parent-student-map")
    public ResponseEntity<?> mapParentStudent(
            @RequestBody Map<String, Long> body,
            HttpServletRequest request
    ) {
        adminService.mapParentToStudent(
                body.get("parentId"),
                body.get("studentId"),
                getLoggedInUser(),
                request
        );
        return ResponseEntity.ok("Parent mapped to student");
    }
    
 // ---------- ENABLE / DISABLE USER ----------

    @PatchMapping("/users/{userId}/disable")
    public ResponseEntity<?> disableUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        adminService.setUserEnabled(userId, false, getLoggedInUser(), request);
        return ResponseEntity.ok("User disabled");
    }

    @PatchMapping("/users/{userId}/enable")
    public ResponseEntity<?> enableUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        adminService.setUserEnabled(userId, true, getLoggedInUser(), request);
        return ResponseEntity.ok("User enabled");
    }
    
 

 // ---------- ADDRESS (ADMIN) ----------

    @PostMapping("/users/{userId}/addAddress")
    public ResponseEntity<?> addAddress(
            @PathVariable Long userId,
            @RequestBody Address address,
            HttpServletRequest request
    ) {
        addressService.addAddress(userId, address, getLoggedInUser(), request);
        return ResponseEntity.ok("Address added successfully");
    }

    @GetMapping("/users/{userId}/getAddress")
    public ResponseEntity<?> getAddress(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddress(userId));
    }

    @PatchMapping("/users/{userId}/updateAddress")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long userId,
            @RequestBody Address address,
            HttpServletRequest request
    ) {
        addressService.updateAddress(userId, address, getLoggedInUser(), request);
        return ResponseEntity.ok("Address updated successfully");
    }

    @DeleteMapping("/users/{userId}/deleteAddress")
    public ResponseEntity<?> deleteAddress(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        addressService.deleteAddress(userId, getLoggedInUser(), request);
        return ResponseEntity.ok("Address deleted successfully");
    }

    
    @PutMapping("/users/{userId}/multi-session")
    public ResponseEntity<String> updateMultiSessionAccess(
            @PathVariable Long userId,
            @RequestBody MultiSessionRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.updateMultiSessionAccess(
                userId,
                request.isMultiSession(),
                getLoggedInUser(),
                httpRequest
        );
        return ResponseEntity.ok("Multi-session access updated");
    }

    static class MultiSessionRequest {
        private boolean multiSession;

        public boolean isMultiSession() {
            return multiSession;
        }
    }
    
    @PutMapping("/roles/{roleName}/multi-session")
    public ResponseEntity<String> updateMultiSessionByRole(
            @PathVariable String roleName,
            @RequestBody MultiSessionRequest request,
            HttpServletRequest httpRequest
    ) {
        adminService.updateMultiSessionAccessByRole(
                roleName,
                request.isMultiSession(),
                getLoggedInUser(),
                httpRequest
        );
        return ResponseEntity.ok("Multi-session access updated for role " + roleName);
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUserBlocked() {
        throw new RuntimeException(
                "User deletion is not allowed. Use enable/disable instead."
        );
    }

    @PatchMapping("/users/{userId}/unlock")
    public ResponseEntity<String> unlockUser(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        adminService.unlockUser(
                userId,
                getLoggedInUser(),
                request
        );
        return ResponseEntity.ok("User account unlocked");
    }
    
    @PostMapping("/users/{userId}/permissions")
    public ResponseEntity<String> addPermissions(
            @PathVariable Long userId,
            @RequestBody UserPermissionRequest requestBody,
            HttpServletRequest request
    ) {
        adminService.addPermissionsToUser(
                userId,
                requestBody.getPermissions(),
                getLoggedInUser(),
                request
        );
        return ResponseEntity.ok("Permissions added successfully");
    }
}
