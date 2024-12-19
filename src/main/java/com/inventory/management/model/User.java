package com.inventory.management.model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Represents a user in the inventory management system.
 */
public class User {
    private String username;
    private String hashedPassword;
    private Role role;

    /**
     * Defines user roles.
     */
    public enum Role {
        ADMIN, STAFF
    }

    /**
     * Constructs a new User with the specified username, password, and role.
     *
     * @param username the username
     * @param password the plaintext password
     * @param role     the user role
     */
    public User(String username, String password, Role role) {
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.role = role;
    }

    /**
     * Hashes the plaintext password using BCrypt.
     *
     * @param password the plaintext password
     * @return the hashed password
     */
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifies a plaintext password against the hashed password.
     *
     * @param password the plaintext password to verify
     * @return true if the password matches; false otherwise
     */
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    // No setter for hashedPassword to prevent direct modification

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
