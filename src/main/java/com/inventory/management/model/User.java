package com.inventory.management.model;

public class User {
    private String username;
    private String password;
    private String role; // "Admin" or "Staff"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password; // In real app: hash the password
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }

    public boolean checkPassword(String pwd) {
        return this.password.equals(pwd);
    }
}
