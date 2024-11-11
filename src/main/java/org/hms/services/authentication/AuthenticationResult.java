package org.hms.services.authentication;

import org.hms.entities.User;

public class AuthenticationResult {
    private boolean success;
    private User user;
    private String message;

    public AuthenticationResult(boolean success, User user, String message) {
        this.success = success;
        this.user = user;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public User getUser() { return user; }
    public String getMessage() { return message; }
}