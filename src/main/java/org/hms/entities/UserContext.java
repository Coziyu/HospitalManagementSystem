package org.hms.entities;

import org.hms.UserType;

public class UserContext {
    private String name;
    private UserType userType;
    private Integer hospitalID;

    public UserContext(String name, UserType userType, Integer hospitalID) {
        this.name = name;
        this.userType = userType;
        this.hospitalID = hospitalID;
    }

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    public Integer getHospitalID() {
        return hospitalID;
    }
}
