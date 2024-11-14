package org.hms.services.medicalrecord;

import org.hms.entities.UserContext;
import org.hms.entities.PatientContext;
import org.hms.entities.UserRole;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MedicalRecordCSVTest {

    @Test
    public void testViewOwnRecordWithCSV() {
        // Create test directory and files
        String testDirectory = "src/test/java/org/hms/services/medical_records";
        createTestFiles(testDirectory);

        // Create storage service and medical record service
        IMedicalDataInterface storageService = new MockMedicalDataInterface();  // Added this line
        MedicalRecordService service = new MedicalRecordService(storageService);

        // Create test patient
        UserContext userContext = new UserContext("Alice", UserRole.PATIENT, 1001);
        PatientContext patient = new PatientContext(userContext, 1001);

        System.out.println("Looking up medical record for patient ID: " + patient.getPatientID());


        // Test record retrieval
        MedicalRecord record = service.viewOwnRecord(patient);

        // Print results
        if (record == null) {
            System.out.println("Test failed: No record found in CSV");
        } else {
            System.out.println("Test passed: Found record for patient " + record.getPatientID());
            System.out.println("Blood Type: " + record.getBloodType());
            System.out.println("Number of medical entries: " + record.getMedicalHistory().size());
        }
    }

    private void createTestFiles(String directory) {
        try {
            // Create data directory
            Files.createDirectories(Paths.get(directory + "/data"));

            // Create PatientList.csv
            String patientData =
                    "PatientID,Name,Date of Birth,Gender,Blood Type,Contact Information\n" +
                            "1001,Alice Brown,1980-05-14,Female,A+,alice.brown@email.com";
            Files.write(Paths.get(directory + "/data/PatientList.csv"), patientData.getBytes());

            // Create medical_records.csv
            String medicalData =
                    "PatientID,Date,Diagnosis,TreatmentPlan,ConsultationNotes,DoctorID\n" +
                            "1001,2024-03-15,Flu,Rest and fluids,Patient shows typical symptoms,D001\n" +
                            "1001,2024-03-20,Follow-up,Continue rest,Symptoms improving,D001";
            Files.write(Paths.get(directory + "/data/medical_records.csv"), medicalData.getBytes());

            System.out.println("Test files created successfully in " + directory + "/data");

        } catch (IOException e) {
            System.err.println("Error creating test files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}