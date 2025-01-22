package com.inventory.management.model;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String username;
    private String hashedPassword;
    private Role role;

    public enum Role {
        ADMIN, STAFF
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.role = role;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getHashedPassword() { return hashedPassword; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
