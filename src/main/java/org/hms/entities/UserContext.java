package org.hms.entities;

import org.hms.UserRole;

public class UserContext {
    private String name;
    private UserRole userRole;
    private Integer hospitalID;

    public UserContext(String name, UserRole userRole, Integer hospitalID) {
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

    public Integer getHospitalID() {
        return hospitalID;
    }
}
