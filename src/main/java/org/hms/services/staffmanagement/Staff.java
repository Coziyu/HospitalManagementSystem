// File: Staff.java
package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTableEntry;
import org.hms.entities.Person;

public class Staff extends AbstractTableEntry {
    private Person person;  // Composition: Staff "has-a" Person
    private int age;
    private String name;
    private String role;
    private String status;
    private String gender;

    // Constructor
    public Staff(int staffId, int age, String name, String role, String status, String gender) {
        super(staffId);
        this.person = new Person(staffId);  // Initialize Person with staffId
        this.age = age;
        this.name = name;
        this.role = role;
        this.status = status;
        this.gender = gender;
    }

    // Delegate methods to person
    public int getStaffId() {
        return person.getStaffId();
    }

    public void setStaffId(int staffId) {
        person.setStaffId(staffId);
    }

    // Getters and setters for other fields
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
                getStaffId(),  // Access staffId from Person
                age,
                preprocessCSVString(name),
                preprocessCSVString(role),
                preprocessCSVString(status),
                preprocessCSVString(gender));
    }

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

    @Override
    public String toString() {
        return String.format("Staff ID: %d, Name: %s, Age: %d, Role: %s, Status: %s, Gender: %s",
                getStaffId(), name, age, role, status, gender);
    }
}
