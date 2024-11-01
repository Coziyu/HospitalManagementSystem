package org.hms.services;

public class PatientRecordService extends AbstractService {
    public String getMedicalRecords(String patientID) {
        return patientID + ": " + "Medical record information!";
    }
}
