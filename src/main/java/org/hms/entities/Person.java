// File: Person.java for staff superclass
package org.hms.entities;

public class Person {
    private String staffId;

    public Person(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
