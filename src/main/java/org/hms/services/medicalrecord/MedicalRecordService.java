package org.hms.services.medicalrecord;

import org.hms.entities.BloodType;
import org.hms.entities.UserContext;
import org.hms.entities.PatientContext;
import org.hms.entities.UserRole;
import org.hms.services.AbstractService;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MedicalRecordService extends AbstractService<IMedicalDataInterface> {
    // CSV paths
    private static final String DATA_DIRECTORY = "data";
    private static final String PATIENT_LIST_CSV = DATA_DIRECTORY + "/PatientList.csv";
    private static final String CONTACT_INFO_CSV = DATA_DIRECTORY + "/contact_information.csv";
    private static final String MEDICAL_RECORDS_CSV = DATA_DIRECTORY + "/medical_records.csv";

    // In-memory storage
    private Map<String, PersonalParticulars> personalParticularsMap;
    private Map<String, ContactInformation> contactInformationMap;
    private Map<String, MedicalRecord> medicalRecordsMap;

    public MedicalRecordService(IMedicalDataInterface storageService) {
        this.storageServiceInterface = storageService;
        this.personalParticularsMap = new HashMap<>();
        this.contactInformationMap = new HashMap<>();
        this.medicalRecordsMap = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get(DATA_DIRECTORY));

            // Initialize CSV files
            if (!Files.exists(Paths.get(CONTACT_INFO_CSV))) {
                createContactInfoCSV();
            }
            if (!Files.exists(Paths.get(MEDICAL_RECORDS_CSV))) {
                createMedicalRecordsCSV();
            }

            // Load data
            loadAllData();
        } catch (IOException e) {
            System.err.println("Error initializing data: " + e.getMessage());
        }
    }

    private void loadAllData() {
        loadPersonalParticulars();
        loadContactInformation();
        loadMedicalRecords();
    }

    private void loadPersonalParticulars() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENT_LIST_CSV))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    PersonalParticulars particulars = PersonalParticulars.fromPatientList(line.split(","));
                    personalParticularsMap.put(particulars.getPatientID(), particulars);
                } catch (Exception e) {
                    System.err.println("Error parsing patient: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading patient data: " + e.getMessage());
        }
    }

    private void loadContactInformation() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONTACT_INFO_CSV))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String patientId = parts[0].trim();
                ContactInformation contact = new ContactInformation(
                        patientId,
                        parts[1].trim(), // phone
                        parts[2].trim(), // email
                        parts[3].trim()  // address
                );
                contactInformationMap.put(patientId, contact);
            }
        } catch (IOException e) {
            System.err.println("Error loading contact data: " + e.getMessage());
        }
    }

    private void loadMedicalRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICAL_RECORDS_CSV))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String patientId = parts[0].trim();

                // Fetch the blood type from PersonalParticulars
                BloodType bloodType = personalParticularsMap.get(patientId).getBloodType();

                MedicalRecord record = medicalRecordsMap.computeIfAbsent(
                        patientId,
                        k -> new MedicalRecord(patientId)
                );

                record.addMedicalEntry(
                        parts[2].trim(), // diagnosis
                        parts[3].trim(), // treatment
                        parts[4].trim()  // notes
                );
            }
        } catch (IOException e) {
            System.err.println("Error loading medical records: " + e.getMessage());
        }
    }

    // === PATIENT METHODS ===
    public MedicalRecord viewOwnRecord(PatientContext patientContext) {
        String patientIDString = patientContext.getPatientID().toString();
        System.out.println("Looking up record with ID: " + patientIDString);
        // Use the medicalRecordsMap that's loaded from CSV instead of storageServiceInterface
        return medicalRecordsMap.get(patientIDString);
    }

    public PersonalParticulars getPersonalParticulars(String patientID) {
        return personalParticularsMap.get(patientID);
    }

    public boolean updateOwnContactInfo(PatientContext patientContext,
                                        String phone, String email, String address) {
        try {
            String patientID = String.valueOf(patientContext.getPatientID());
            ContactInformation newContact = new ContactInformation(patientID, phone, email, address);
            contactInformationMap.put(patientID, newContact);
            saveContactInfoToCSV();
            return true;
        } catch (Exception e) {
            System.err.println("Error updating contact info: " + e.getMessage());
            return false;
        }
    }

    // === DOCTOR METHODS ===
    public MedicalRecord viewPatientRecord(UserContext doctorContext, String patientID) {
        if (doctorContext.getUserType() != UserRole.DOCTOR) {
            System.err.println("Unauthorized: Only doctors can view patient records");
            return null;
        }
        return medicalRecordsMap.get(patientID);
    }

    public boolean addMedicalEntry(UserContext doctorContext, String patientID,
                                   String diagnosis, String treatment, String notes) {
        if (doctorContext.getUserType() != UserRole.DOCTOR) {
            System.err.println("Unauthorized: Only doctors can add medical entries");
            return false;
        }

        try {
            MedicalRecord record = medicalRecordsMap.get(patientID);
            record.addMedicalEntry(diagnosis, treatment, notes);
            saveMedicalRecordsToCSV();
            return true;
        } catch (Exception e) {
            System.err.println("Error adding medical entry: " + e.getMessage());
            return false;
        }
    }

    // === CSV OPERATIONS ===
    private void createContactInfoCSV() throws IOException {
        try (FileWriter writer = new FileWriter(CONTACT_INFO_CSV)) {
            writer.write("PatientID,PhoneNumber,Email,Address\n");
        }
    }

    private void createMedicalRecordsCSV() throws IOException {
        try (FileWriter writer = new FileWriter(MEDICAL_RECORDS_CSV)) {
            writer.write("PatientID,Date,Diagnosis,TreatmentPlan,ConsultationNotes,DoctorID\n");
        }
    }

    private void saveContactInfoToCSV() throws IOException {
        try (FileWriter writer = new FileWriter(CONTACT_INFO_CSV)) {
            writer.write("PatientID,PhoneNumber,Email,Address\n");
            for (ContactInformation contact : contactInformationMap.values()) {
                writer.write(String.format("%s,%s,%s,%s\n",
                        contact.getPatientID(),
                        contact.getPhoneNumber(),
                        contact.getEmail(),
                        contact.getAddress()
                ));
            }
        }
    }

    private void saveMedicalRecordsToCSV() throws IOException {
        try (FileWriter writer = new FileWriter(MEDICAL_RECORDS_CSV)) {
            writer.write("PatientID,Date,Diagnosis,TreatmentPlan,ConsultationNotes,DoctorID\n");
            for (MedicalRecord record : medicalRecordsMap.values()) {
                for (MedicalEntry entry : record.getMedicalHistory()) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                            entry.getDate(),
                            entry.getDiagnosis(),
                            entry.getTreatmentPlan(),
                            entry.getConsultationNotes(),
                            "D001" // Placeholder for doctor ID
                            // TODO: How should this be handled? Perhaps taking from AppointmentOutcome
                    ));
                }
            }
        }
    }

    // Required by AbstractService
    //TODO: For Elijah to reimplement this part.
    // For now, I have added patientID as an argument to your code.
    // make sure to ensure that your implementation is correct
    // Also note that patientID eventually would be stored as a String
//    public Optional<MedicalRecord> getMedicalRecord() {
//        return Optional.ofNullable(medicalRecordsMap.get(patientID));
//    }
    public Optional<MedicalRecord> getMedicalRecord(int patientID) {
        return Optional.ofNullable(medicalRecordsMap.get(patientID));
    }
}