package org.hms.entities;

public class UserContext {
    private String name;
    private UserRole userRole;
    private String hospitalID;

    public UserContext(String name, UserRole userRole, String hospitalID) {
        this.name = name;
        this.userRole = userRole;
        this.hospitalID = hospitalID;
    }

    public String getName() {
        return name;
    }

    public UserRole getUserType() {
        return userRole;
    }

    public String getHospitalID() {
        return hospitalID;
    }
}
