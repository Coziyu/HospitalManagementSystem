package org.hms.entities;

public class User {
    private String id;
    private String password;
    private UserRole role;
    private boolean isFirstLogin;

    public User(String id, String password, UserRole role, boolean isFirstLogin) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.isFirstLogin = isFirstLogin;
    }

    public String getId() { return id; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public boolean isFirstLogin() { return isFirstLogin; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstLogin(boolean firstLogin) { isFirstLogin = firstLogin; }
}