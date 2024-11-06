package org.hms.services.medicalrecord;

import org.hms.services.AbstractService;

public class MedicalRecordService
        extends AbstractService<IMedicalDataInterface> {
    public MedicalRecordService(IMedicalDataInterface storageService) {
        this.storageServiceInterface = storageService;
    }
    public String getMedicalRecords(Integer patientID) {
        return patientID + "'s Medical record information!";
    }
}
