package com.lms.www.service.Impl;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.lms.www.model.User;
import com.lms.www.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ================= ACCOUNT CREATION =================
    @Override
    public void sendAccountCredentialsMail(
            User user,
            String rawPassword,
            String loginUrl
    ) {

        String subject = "Your LMS Account Has Been Created";

        String body =
                "Hello " + user.getFirstName() + ",\n\n" +
                "Your LMS account has been successfully created.\n\n" +
                "You can access the application here:\n"+loginUrl+"\n\n"+
                "Login Details:\n" +
                "Email: " + user.getEmail() + "\n" +
                "Password: " + rawPassword + "\n\n" +
                "Please change your password after first login.\n\n" +
                "Regards,\n" +
                "LMS Team";

        send(user.getEmail(), subject, body);
    }


    @Override
    public void sendRegistrationMail(User user, String role) {
        send(
            user.getEmail(),
            "LMS Account Created",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your LMS account is active.\n" +
            "Role: " + role + "\n\n" +
            "- LMS Team"
        );
    }

    // ================= USER UPDATE =================
    @Override
    public void sendUserUpdatedMail(User user, String updatedFields, LocalDateTime time) {
        send(
            user.getEmail(),
            "Profile Updated",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your profile was updated.\n\n" +
            "Changes: " + updatedFields + "\n" +
            "Time: " + time + "\n\n" +
            "- LMS Security"
        );
    }


    @Override
    public void sendRelationMappingMail(User parent, User student, LocalDateTime time) {
        send(
            parent.getEmail(),
            "Student Mapped",
            "Hello " + parent.getFirstName() + ",\n\n" +
            "Student " + student.getFirstName() + " has been mapped to you.\n\n" +
            "Time: " + time + "\n\n" +
            "- LMS Admin"
        );
    }

    // ================= AUTH =================
    @Override
    public void sendLoginSuccessMail(User user, String ipAddress, LocalDateTime time) {
        send(
            user.getEmail(),
            "Login Successful",
            "Login detected.\n\nIP: " + ipAddress + "\nTime: " + time
        );
    }

    @Override
    public void sendLoginFailedMail(
            String email,
            String ipAddress,
            String userAgent,
            LocalDateTime time
    ) {

        String subject = "Failed Login Attempt Detected";

        String body =
                "Hello,\n\n" +
                "We detected a failed login attempt to your LMS account.\n\n" +
                "Details:\n" +
                "IP Address: " + ipAddress + "\n" +
                "Time: " + time + "\n" +
                "Device: "+userAgent+ "\n\n"+
                "If this was you, you can safely ignore this message.\n" +
                "If this was NOT you, we strongly recommend resetting your password immediately.\n\n" +
                "- LMS Security Team";

        send(email, subject, body);
    }


    @Override
    public void sendPasswordResetSuccessMail(
            String email,
            LocalDateTime resetTime
    ) {
        String subject = "Your LMS password has been changed";

        String body =
                "Hi,\n\n" +
                "Your LMS account password was successfully changed.\n\n" +
                "🕒 Time: " + resetTime + "\n\n" +
                "If you did NOT perform this action, please reset your password immediately or contact support.\n\n" +
                "Regards,\n" +
                "LMS Security Team";

        send(email, subject, body);
    }

    

    // ================= COMMON =================
    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR: " + e.getMessage());
        }

    }
    
    @Override
    public void sendAccountDeletionMail(User user) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(user.getEmail());
            msg.setSubject("LMS Account Deleted");
            msg.setText(
                    "Hello " + user.getFirstName() + ",\n\n" +
                    "Your LMS account has been deleted by an administrator.\n\n" +
                    "If this was not expected, please contact support.\n\n" +
                    "- LMS Team"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("MAIL ERROR (account delete): " + e.getMessage());
        }
    }

    @Override
    public void sendOtpMail(String email, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("OTP Verification");
        msg.setText(
            "Your OTP is: " + otp +
            "\nValid for 10 minutes."
        );
        mailSender.send(msg);
    }

    @Override
    public void sendSuperAdminCredentialsMail(
            String email,
            String password,
            String superAdminUrl
    ) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Super Admin Access");

        msg.setText(
            "Welcome!\n\n" +
            "Web Address: " + superAdminUrl + "\n" +
            "Username: " + email + "\n" +
            "Password: " + password + "\n\n" +
            "Please keep these credentials safe."
        );
        mailSender.send(msg);
    }

    @Override
    public void sendAdminCredentialsMail(
            String email,
            String password
    ) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Admin Access");

        msg.setText(
            "Welcome!\n\n" +
            "Username: " + email + "\n" +
            "Password: " + password + "\n\n" +
            "Please keep these credentials safe."
        );
        mailSender.send(msg);
    }
    
    @Override
    public void sendNewDeviceLoginAlert(
            User user,
            String ipAddress,
            String userAgent,
            LocalDateTime time
    ) {
        String subject = "New Login Detected on Your Account";

        String body = """
            Hi %s,

            We detected a login to your account from a new device.

            📅 Time: %s
            🌍 IP Address: %s
            💻 Device: %s

            If this was you, no action is needed.
            If not, please reset your password immediately.

            Regards,
            LMS Security Team
            """.formatted(
                user.getFirstName(),
                time,
                ipAddress,
                userAgent
            );

        send(user.getEmail(), subject, body);
    }
    
    @Override
    public void sendMultiSessionStatusMail(User user, boolean enabled) {

        String subject = "LMS Security Settings Updated";

        String body =
                "Hello " + user.getFirstName() + ",\n\n" +
                "Multi-session access has been " +
                (enabled ? "ENABLED" : "DISABLED") +
                " for your LMS account.\n\n" +
                "If you have any queries, please contact support immediately.\n\n" +
                "- LMS Team";

        send(user.getEmail(), subject, body);
    }
    
    
    @Override
    public void sendProfileUpdatedMail(User user) {

        String subject = "Profile Updated Successfully";

        String body =
                "Hello " + user.getFirstName() + ",\n\n" +
                "Your profile details have been updated successfully.\n\n" +
                "If you did not make this change, please contact support immediately.\n\n" +
                "- LMS Team";

        send(user.getEmail(), subject, body);
    }
    
    @Override
    public void sendAddressAddedMail(User user) {
        send(
            user.getEmail(),
            "Address Added",
            "Hello " + user.getFirstName() + ",\n\n" +
            "A new address has been added to your LMS account.\n\n" +
            "Verify your profile for more details."+
            "- LMS Team"
        );
    }
    
    @Override
    public void sendAddressUpdatedMail(User user) {
        send(
            user.getEmail(),
            "Address Updated",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your address details have been updated.\n\n" +
            "Verify your profile for more details."+
            "- LMS Team"
        );
    }
    
    @Override
    public void sendAddressDeletedMail(User user) {
        send(
            user.getEmail(),
            "Address Removed",
            "Hello " + user.getFirstName() + ",\n\n" +
            "Your address has been removed from your LMS account.\n\n" +
            "- LMS Team"
        );
    }
    
    @Override
    public void sendParentStudentMappingMailToParent(User parent, User student) {

        String subject = "Student Linked to Your Account";

        String body =
                "Hello " + parent.getFirstName() + ",\n\n" +
                "The following student has been successfully linked to your account:\n\n" +
                "Student Name: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                "Student Email: " + student.getEmail() + "\n\n" +
                "If you did not request this change, please contact support immediately.\n\n" +
                "- LMS Team";

        send(parent.getEmail(), subject, body);
    }

    @Override
    public void sendParentStudentMappingMailToStudent(User student, User parent) {

        String subject = "Parent Linked to Your Account";

        String body =
                "Hello " + student.getFirstName() + ",\n\n" +
                "A parent has been linked to your LMS account:\n\n" +
                "Parent Name: " + parent.getFirstName() + " " + parent.getLastName() + "\n" +
                "Parent Email: " + parent.getEmail() + "\n\n" +
                "If this is unexpected, please inform your administrator.\n\n" +
                "- LMS Team";

        send(student.getEmail(), subject, body);
    }
    
    @Override
    public void sendAccountStatusMail(User user, String status) {

        String subject = "LMS Account Status Update";

        String message = """
                Hello %s,

                Your LMS account status has been updated.

                Current Status: %s

                If you believe this was done by mistake, please contact support.

                Regards,
                LMS Team
                """.formatted(
                        user.getFirstName(),
                        status
                );

        send(
                user.getEmail(),
                subject,
                message
        );
    }

}
