package org.hms.services;

public class MedicalRecordService extends AbstractService {
    public String getMedicalRecords(Integer patientID) {
        return patientID + "'s Medical record information!";
    }
}
