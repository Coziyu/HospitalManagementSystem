package org.hms.entities;

/**
 * The UserContext class provides a context containing essential user information.
 * This class stores the user's name, role, and associated hospital ID.
 */
public class UserContext {
    /**
     * The name of the user.
     */
    private String name;
    /**
     * Represents the role of the user within the context.
     * The role can be one of the predefined values in the UserRole enum,
     * indicating whether the user is a patient, doctor, pharmacist, or administrator.
     */
    private UserRole userRole;
    /**
     * The unique identifier associated with the hospital to which the user is linked.
     */
    private String hospitalID;

    /**
     * Constructs a new UserContext with the given user details.
     *
     * @param name       the name of the user
     * @param userRole   the role of the user in the system
     * @param hospitalID the hospital ID associated with the user
     */
    public UserContext(String name, UserRole userRole, String hospitalID) {
        this.name = name;
        this.userRole = userRole;
        this.hospitalID = hospitalID;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the role of the user.
     *
     * @return the userRole, indicating the user's role within the system
     */
    public UserRole getUserType() {
        return userRole;
    }

    /**
     * Retrieves the hospital ID associated with the current user context.
     *
     * @return the hospitalID of the current user
     */
    public String getHospitalID() {
        return hospitalID;
    }
}
