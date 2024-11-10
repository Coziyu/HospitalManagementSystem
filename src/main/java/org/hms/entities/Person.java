// File: Person.java for staff superclass
package org.hms.entities;

public class Person {
    private int staffId;

    public Person(int staffId) {
        this.staffId = staffId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
