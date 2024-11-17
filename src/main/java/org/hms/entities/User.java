package org.hms.entities;

/**
 * Represents a user within the system, encapsulating user-related information
 * such as the user's ID, password, role, and login status.
 */
public class User {
    /**
     * Unique identifier for the user.
     */
    private String id;
    /**
     * The password associated with the user.
     * This field stores the user's encrypted password for authentication purposes.
     */
    private String password;
    /**
     * Indicates the role of the user in the system.
     * The possible roles include PATIENT, DOCTOR, PHARMACIST, and ADMINISTRATOR.
     */
    private UserRole role;
    /**
     * Indicates whether this is the user's first login.
     * <p>
     * This flag is used to determine if the user is logging in for the first time,
     * potentially triggering additional onboarding steps or actions within the system.
     */
    private boolean isFirstLogin;

    /**
     * Constructs a new User with specified id, password, role, and first login status.
     *
     * @param id           the unique identifier for the user
     * @param password     the password for the user
     * @param role         the role assigned to the user
     * @param isFirstLogin indicates if this is the user's first login
     */
    public User(String id, String password, UserRole role, boolean isFirstLogin) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * Retrieves the ID of the user.
     *
     * @return the user's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password to set for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the role of the user.
     *
     * @return the UserRole of the user
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Checks if the user is logging in for the first time.
     *
     * @return true if it is the user's first login, false otherwise
     */
    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    /**
     * Sets the first login status for the user.
     *
     * @param firstLogin A boolean indicating whether this is the user's first login.
     */
    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }
}