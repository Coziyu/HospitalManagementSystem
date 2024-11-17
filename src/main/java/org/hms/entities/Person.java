package org.hms.entities;

/**
 * The Person class represents an individual with a staff ID.
 * This class allows you to get and set the staff ID of a person.
 */
public class Person {
    /**
     * The unique identifier assigned to a staff member.
     */
    private String staffId;

    /**
     * Initializes a new instance of the Person class with the specified staff ID.
     *
     * @param staffId the unique identifier for the staff member
     */
    public Person(String staffId) {
        this.staffId = staffId;
    }

    /**
     * Retrieves the staff ID of the person.
     *
     * @return the staff ID
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * Sets the staff ID for the person.
     *
     * @param staffId the staff ID to set
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
