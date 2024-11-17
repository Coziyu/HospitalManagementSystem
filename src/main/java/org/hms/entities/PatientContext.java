package org.hms.entities;

/**
 * The PatientContext class extends the UserContext to provide additional context specific
 * to a patient by incorporating a patientID.
 */
public class PatientContext extends UserContext {

    /**
     * Unique identifier assigned to a patient.
     */
    private String patientID;

    /**
     * Constructs a new PatientContext object by extending the given UserContext
     * and adding patient-specific information.
     *
     * @param userContext The UserContext object containing common user information
     *                    such as name, role, and hospital ID.
     * @param patientID   The unique identifier assigned to a patient.
     */
    public PatientContext(UserContext userContext, String patientID) {
        super(userContext.getName(), userContext.getUserType(), userContext.getHospitalID());
        this.patientID = patientID;
    }

    /**
     * Retrieves the unique identifier assigned to the patient.
     *
     * @return the patientID of the patient
     */
    public String getPatientID() {
        return patientID;
    }

}
