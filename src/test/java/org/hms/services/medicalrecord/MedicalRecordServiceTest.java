package org.hms.services.medicalrecord;

import org.hms.services.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordServiceTest {

    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        medicalRecordService = new MedicalRecordService(new StorageService());
    }
    @Test
    void getPatientMedicalRecord() {
        System.out.println(medicalRecordService.getPatientMedicalRecord("PAT001"));
    }
    @Test
    void viewContactInfo(){
        System.out.println(medicalRecordService.getPatientContactInformation("PAT001"));
    }
}