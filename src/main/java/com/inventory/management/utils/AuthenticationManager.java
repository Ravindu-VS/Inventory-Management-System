package com.inventory.management.utils;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationManager {
    private static final Map<String, String> userCredentials = new HashMap<>();
    private static final Map<String, String> userRoles = new HashMap<>();

    static {
        userCredentials.put("admin", "admin123");
        userRoles.put("admin", "Admin");

        userCredentials.put("staff", "staff123");
        userRoles.put("staff", "Staff");
    }

    public static String authenticate(String username, String password) {
        if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            return userRoles.get(username);
        }
        return null;
    }

    public static boolean isAuthorized(String role, String requiredRole) {
        return role != null && role.equalsIgnoreCase(requiredRole);
    }
}
