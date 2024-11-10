package org.hms.services.staffmanagement;

public class Staff {
    private int staffId; // Unique identifier for the staff member
    private int age; // Age of the staff member
    private String name; // Name of the staff member
    private String role; // Role of the staff member
    private String status; // Employment status (active/inactive)
    private String gender; // Contact details of the staff member

    // Constructor
    public Staff(int staffId, int age, String name, String role, String status, String gender) {
        this.staffId = staffId;
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
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staff)) return false;
        Staff staff = (Staff) o;
        return staffId == staff.staffId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(staffId);
    }
}