package org.hms.services.authentication;

import org.hms.entities.User;

/**
 * Represents the result of an authentication attempt.
 */
public class AuthenticationResult {
    /**
     * Indicates whether the authentication attempt was successful.
     */
    private boolean success;
    /**
     * The user object associated with the authentication result.
     */
    private User user;
    /**
     * Provides additional details regarding the authentication attempt.
     * This message may include error descriptions, success confirmations, or
     * other relevant information pertaining to the outcome of the authentication process.
     */
    private String message;

    /**
     * Constructs an AuthenticationResult object that encapsulates the result of an authentication attempt.
     *
     * @param success A boolean indicating whether the authentication was successful.
     * @param user    The User object representing the authenticated user, or null if authentication failed.
     * @param message A message providing additional details about the authentication result.
     */
    public AuthenticationResult(boolean success, User user, String message) {
        this.success = success;
        this.user = user;
        this.message = message;
    }

    /**
     * Checks if the authentication attempt was successful.
     *
     * @return true if the authentication was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Retrieves the user associated with this authentication result.
     *
     * @return the User object that represents the authenticated user, or null if authentication failed
     */
    public User getUser() {
        return user;
    }

    /**
     * Retrieves the message associated with the authentication result.
     *
     * @return the message describing the outcome of the authentication attempt
     */
    public String getMessage() {
        return message;
    }
}