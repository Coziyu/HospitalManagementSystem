package org.hms.services.staffmanagement;

public class Person {
    protected int staffId;

    public Person(int staffId) {
        this.staffId = staffId;
    }

    // Getter and setter for staffId
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
