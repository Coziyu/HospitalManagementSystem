package org.hms.services.medicalrecord;

import org.hms.services.AbstractService;

public class MedicalRecordService
        extends AbstractService<IMedicalDataAccess>
        implements IMedicalDataAccess {
    public String getMedicalRecords(Integer patientID) {
        return patientID + "'s Medical record information!";
    }
}
