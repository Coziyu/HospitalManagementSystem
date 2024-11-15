package org.hms.services.medicalrecord;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordTest {
    private MedicalRecord medicalRecord;

    @Test
    void loadingFromCSV() {
        medicalRecord = new MedicalRecord("data/medical_records.csv");
        assertEquals(3, medicalRecord.getEntries().size());
    }
}