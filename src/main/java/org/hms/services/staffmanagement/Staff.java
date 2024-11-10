package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTableEntry;

public class Staff extends AbstractTableEntry {
    private int age; // Age of the staff member
    private String name; // Name of the staff member
    private String role; // Role of the staff member
    private String status; // Employment status (active/inactive)
    private String gender; // gender of the staff member

    // Constructor
    public Staff(int staffId, int age, String name, String role, String status, String gender) {
        super(staffId);
        this.name = name;
        this.role = role;
        this.status = status;
        this.gender = gender;
        this.age = age;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toCSVString() {
        return String.format("%d,%d,%s,%s,%s,%s",
                getStaffId(), // Assuming getID() returns the same value as staffId
                age,
                preprocessCSVString(name),
                preprocessCSVString(role),
                preprocessCSVString(status),
                preprocessCSVString(gender));
    }

    /**
     * @param csvLine comma-separated entry values.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        this.staffId = Integer.parseInt(parts[0]);
        this.age = Integer.parseInt(parts[1]);
        this.name = parts[2];
        this.role = parts[3];
        this.status = parts[4];
        this.gender = parts[5];
    }
}