package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTableEntry;

public class Staff extends AbstractTableEntry {
    private static int nextTableEntryID = 1; // Static variable to track the next ID
    private String staffId; // Alphanumerical ID
    private int age;
    private String name;
    private String role;
    private String status;
    private String gender;

    public Staff(int tableEntryID, String staffId, int age, String name, String role, String status, String gender) {
        super(tableEntryID); // Internal tableEntryID
        this.staffId = staffId;
        this.age = age;
        this.name = name;
        this.role = role;
        this.status = status;
        this.gender = gender;
    }

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

    // Getters and setters
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
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

    public void setTableEntryID(int tableEntryID) {
        this.tableEntryID = tableEntryID;
    }


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

    @Override
    public String toString() {
        return String.format("Staff ID: %s, Name: %s, Age: %d, Role: %s, Status: %s, Gender: %s",
                staffId, name, age, role, status, gender);
    }
}
