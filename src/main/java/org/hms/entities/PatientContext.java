package org.hms.entities;

public class PatientContext extends UserContext {

    private Integer patientID;

    public PatientContext(UserContext userContext, Integer patientID) {
        super(userContext.getName(), userContext.getUserType(), userContext.getHospitalID());
        this.patientID = patientID;
    }
    public Integer getPatientID() {
        return patientID;
    }

}
