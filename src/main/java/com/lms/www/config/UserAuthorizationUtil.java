package com.lms.www.config;

import com.lms.www.model.User;

public final class UserAuthorizationUtil {

    private UserAuthorizationUtil() {}

    public static void assertAdminCannotTouchSuperAdmin(
            User requester,
            User targetUser
    ) {
        if ("ROLE_ADMIN".equals(requester.getRoleName())
                && "ROLE_SUPER_ADMIN".equals(targetUser.getRoleName())) {
            throw new RuntimeException(
                "Admin is not allowed to perform operations on Super Admin"
            );
        }
    }
    public static void assertAdminCannotTouchAdmin(
            User requester,
            User targetUser
    ) {
    	if ("ROLE_ADMIN".equals(requester.getRoleName())
                && "ROLE_ADMIN".equals(targetUser.getRoleName())) {
            throw new RuntimeException(
                    "Admin cannot perform operations on Admin"
            );
        }
    	
    }
    
}
