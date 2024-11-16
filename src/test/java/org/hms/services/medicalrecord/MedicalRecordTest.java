package org.hms.services.medicalrecord;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MedicalRecordTest {
    private MedicalRecord medicalRecord;

    @Test
    void loadingFromCSV() {
        // TODO: Replace this with real test data
        String filePath = System.getProperty("user.dir") + "/data/medical_records.csv";
        medicalRecord = new MedicalRecord(filePath);

        try {
            medicalRecord.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(medicalRecord.toPrintString());

        assertEquals(7, medicalRecord.getEntries().size());
    }
}