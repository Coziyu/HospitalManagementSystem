package org.hms.services.medicalrecord;

import org.hms.entities.BloodType;

import java.util.*;

class MockMedicalDataInterface implements IMedicalDataInterface {
    private Map<String, MedicalRecord> records = new HashMap<>();

    public MockMedicalDataInterface() {
        // Create and store a test record for ID 1001
        MedicalRecord testRecord = new MedicalRecord("1001");
        records.put("1001", testRecord);
        System.out.println("Mock: Created test record for ID 1001");
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecord(String patientID) {
        System.out.println("Mock: Looking for record with ID " + patientID);
        System.out.println("Mock: Available records: " + records.keySet());
        return Optional.ofNullable(records.get(patientID));
    }

    // Other required interface methods with minimal implementations
    @Override
    public boolean saveMedicalRecord(MedicalRecord record) { return true; }

    @Override
    public boolean updateContactInformation(String patientID, ContactInformation contact) { return true; }

    @Override
    public boolean addMedicalEntry(String patientID, MedicalEntry entry) { return true; }
}