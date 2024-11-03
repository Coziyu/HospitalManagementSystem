package org.hms.entities;

import org.hms.UserType;

public class PatientContext extends UserContext {

    private Integer patientID;

    public PatientContext(String name, UserType userType, Integer hospitalID, Integer patientID) {
        super(name, userType, hospitalID);
        this.patientID = patientID;
    }

    public Integer getPatientID() {
        return patientID;
    }

}
