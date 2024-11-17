package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTableEntry;

/**
 * The Staff class represents a staff member with relevant details, such as staff ID, age, name, role, status, and gender.
 * It is an extension of the AbstractTableEntry and includes additional properties specific to the staff.
 */
public class Staff extends AbstractTableEntry {
    /**
     * A static variable to track the next available ID for table entries.
     * Initialized to 1 and incremented each time a new entry is created.
     */
    private static int nextTableEntryID = 1; // Static variable to track the next ID
    /**
     * Represents a unique alphanumerical identifier for a staff member.
     */
    private String staffId; // Alphanumerical ID
    /**
     * Represents the age of the staff member.
     * This variable is private and is accessed through getters and setters.
     */
    private int age;
    /**
     * Represents the name of the staff member.
     */
    private String name;
    /**
     * Represents the role of the staff member.
     * This variable holds the job title or function that the staff member performs in the organization.
     */
    private String role;
    /**
     * Represents the employment status of a staff member.
     */
    private String status;
    /**
     * Represents the gender of a staff member.
     */
    private String gender;

    /**
     * Constructs a new Staff object with the specified parameters.
     *
     * @param tableEntryID the unique identifier for the table entry
     * @param staffId      the alphanumeric identifier for the staff
     * @param age          the age of the staff member
     * @param name         the name of the staff member
     * @param role         the role of the staff member
     * @param status       the current status of the staff member
     * @param gender       the gender of the staff member
     */
    public Staff(int tableEntryID, String staffId, int age, String name, String role, String status, String gender) {
        super(tableEntryID); // Internal tableEntryID
        this.staffId = staffId;
        this.age = age;
        this.name = name;
        this.role = role;
        this.status = status;
        this.gender = gender;
    }

    /**
     * Constructor for creating new staff with automatic ID assignment.
     *
     * @param staffId the unique identifier for the staff member
     * @param age     the age of the staff member
     * @param name    the full name of the staff member
     * @param role    the role of the staff member (e.g., Doctor, Pharmacist)
     * @param status  the current status of the staff member (e.g., active, inactive)
     * @param gender  the gender of the staff member (e.g., M, F)
     */
    // Constructor for creating new staff with automatic ID assignment
    public Staff(String staffId, int age, String name, String role, String status, String gender) {
        super(nextTableEntryID++); // Assign and increment the next ID
        this.staffId = staffId;
        this.age = age;
        this.name = name;
        this.role = role;
        this.status = status;
        this.gender = gender;
    }

    /**
     * Retrieves the staff ID.
     *
     * @return the staff ID as a string
     */
    // Getters and setters
    public String getStaffId() {
        return staffId;
    }

    /**
     * Sets the unique identifier for the staff.
     *
     * @param staffId the unique identifier for the staff
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * Retrieves the age of the staff member.
     *
     * @return the age of the staff member as an int.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the staff member.
     *
     * @param age The age to be assigned to the staff member.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Retrieves the name of the staff member.
     *
     * @return the name of the staff member
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the staff member.
     *
     * @param name the new name of the staff member
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the role of the staff member.
     *
     * @return the role of the staff member.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the staff member.
     *
     * @param role the role to be assigned to the staff member
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Retrieves the current status of the staff.
     *
     * @return the status of the staff as a String
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the staff member.
     *
     * @param status the new status to be assigned to the staff member
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the gender of the staff member.
     *
     * @return the gender of the staff member as a String
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the staff.
     *
     * @param gender the gender to set for the staff
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the table entry ID for the staff.
     *
     * @param tableEntryID the unique table entry ID to be set for this staff member
     */
    public void setTableEntryID(int tableEntryID) {
        this.tableEntryID = tableEntryID;
    }


    /**
     * Converts the staff member's attributes to a CSV-formatted string, excluding the table entry ID.
     *
     * @return a CSV-formatted string representing the staff member's details
     */
    @Override
    public String toCSVString() {
        // Exclude tableEntryID from the CSV file
        return String.format("%s,%d,%s,%s,%s,%s",
                staffId,
                age,
                preprocessCSVString(name),
                preprocessCSVString(role),
                preprocessCSVString(status),
                preprocessCSVString(gender));
    }

    /**
     * Loads the staff member details from a CSV-formatted string and assigns them to the corresponding fields.
     *
     * @param csvLine a single line in CSV format containing staff member details
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);

        // Assign a new tableEntryID for each loaded staff member
        this.tableEntryID = nextTableEntryID++;

        this.staffId = parts[0];
        this.age = Integer.parseInt(parts[1]);
        this.name = parts[2];
        this.role = parts[3];
        this.status = parts[4];
        this.gender = parts[5];
    }

    /**
     * Returns a string representation of the Staff object, including all the key properties.
     *
     * @return a string representation of the Staff object
     */
    @Override
    public String toString() {
        return String.format("Staff ID: %s, Name: %s, Age: %d, Role: %s, Status: %s, Gender: %s",
                staffId, name, age, role, status, gender);
    }
}
