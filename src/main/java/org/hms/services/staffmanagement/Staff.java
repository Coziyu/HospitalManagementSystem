// File: Staff.java
package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTableEntry;
import org.hms.entities.Person;

/**
 * Represents a staff member within the organization, with attributes for age,
 * name, role, status, and gender. This class is composed with a Person object
 * and extends AbstractTableEntry to provide basic table entry functionality.
 */
public class Staff extends AbstractTableEntry {
    private Person person;  // Composition: Staff "has-a" Person
    private int age;
    private String name;
    private String role;
    private String status;
    private String gender;

    /**
     * Constructs a new Staff object with the specified details.
     *
     * @param staffId the unique ID of the staff member
     * @param age the age of the staff member
     * @param name the name of the staff member
     * @param role the role assigned to the staff member
     * @param status the employment status of the staff member (e.g., active, inactive)
     * @param gender the gender of the staff member
     */
    public Staff(int staffId, int age, String name, String role, String status, String gender) {
        super(staffId);
        this.person = new Person(staffId);  // Initialize Person with staffId
        this.age = age;
        this.name = name;
        this.role = role;
        this.status = status;
        this.gender = gender;
    }

    /**
     * Retrieves the unique staff ID from the Person object.
     *
     * @return the staff ID
     */
    public int getStaffId() {
        return person.getStaffId();
    }

    /**
     * Sets the unique staff ID in the Person object.
     *
     * @param staffId the new staff ID
     */
    public void setStaffId(int staffId) {
        person.setStaffId(staffId);
    }

    // Getters and setters for other fields

    /**
     * Retrieves the age of the staff member.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the staff member.
     *
     * @param age the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Retrieves the name of the staff member.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the staff member.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the role assigned to the staff member.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the staff member.
     *
     * @param role the new role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Retrieves the employment status of the staff member.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the employment status of the staff member.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the gender of the staff member.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the staff member.
     *
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Converts the staff member's data to a CSV-formatted string.
     *
     * @return the CSV representation of the staff member's data
     */
    @Override
    public String toCSVString() {
        return String.format("%d,%d,%s,%s,%s,%s",
                getStaffId(),  // Access staffId from Person
                age,
                preprocessCSVString(name),
                preprocessCSVString(role),
                preprocessCSVString(status),
                preprocessCSVString(gender));
    }

    /**
     * Loads the staff member's data from a CSV-formatted string.
     *
     * @param csvLine the CSV line containing the staff member's data
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        this.setStaffId(Integer.parseInt(parts[0]));  // Set ID in Person
        this.age = Integer.parseInt(parts[1]);
        this.name = parts[2];
        this.role = parts[3];
        this.status = parts[4];
        this.gender = parts[5];
    }

    /**
     * Provides a string representation of the staff member, including their ID,
     * name, age, role, status, and gender.
     *
     * @return the string representation of the staff member
     */
    @Override
    public String toString() {
        return String.format("Staff ID: %d, Name: %s, Age: %d, Role: %s, Status: %s, Gender: %s",
                getStaffId(), name, age, role, status, gender);
    }
}
