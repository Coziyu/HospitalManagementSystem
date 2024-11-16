package org.hms.entities;

public class PatientContext extends UserContext {

    private String patientID;

    public PatientContext(UserContext userContext, String patientID) {
        super(userContext.getName(), userContext.getUserType(), userContext.getHospitalID());
        this.patientID = patientID;
    }
    public String getPatientID() {
        return patientID;
    }

}
