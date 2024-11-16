package org.hms.services.medicalrecord;

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
    private PatientTable patientTable;
    private Map<String, ContactInformation> contactInformationMap;
    private MedicalRecord medicalRecordsTable;

    public MedicalRecordService(IMedicalDataInterface storageService) {
        this.storageServiceInterface = storageService;
        this.patientTable = storageServiceInterface.getPatientTable();
        this.contactInformationMap = new HashMap<>();
        this.medicalRecordsTable = storageServiceInterface.getMedicalRecordTable();
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
        loadContactInformation();
    }



    // TODO: Refactor using AbstractTable
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

    // === PATIENT METHODS ===
    public MedicalRecord viewOwnRecord(PatientContext patientContext) {
        String patientIDString = patientContext.getPatientID().toString();
        System.out.println("Looking up record with ID: " + patientIDString);
        // Use the medicalRecordsMap that's loaded from CSV instead of storageServiceInterface
        return getMedicalRecord(patientIDString);
    }

    public PatientParticulars getPersonalParticulars(String patientID) {
        return patientTable.searchByAttribute(PatientParticulars::getPatientID, patientID).getFirst();
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

    /**
     * View a patient's medical record. If the doctor is not authorized, return null
     * @param doctorContext
     * @param patientID
     * @return a MedicalRecord if the doctor is authorized to access the patient's record
     *         null if the doctor is not authorized
     */
    public MedicalRecord viewPatientRecord(UserContext doctorContext, String patientID) {
        if (doctorContext.getUserType() != UserRole.DOCTOR) {
            System.err.println("Unauthorized: Only doctors can view patient records");
            return null;
        }
        return getMedicalRecord(patientID);
    }

    public boolean addMedicalEntry(UserContext doctorContext, String patientID,
                                   String diagnosis, String treatment, String notes) {
        if (doctorContext.getUserType() != UserRole.DOCTOR) {
            System.err.println("Unauthorized: Only doctors can add medical entries");
            return false;
        }

        try {
            MedicalEntry newEntry = medicalRecordsTable.createValidEntryTemplate();
            newEntry.setPatientID(patientID);
            newEntry.setDoctorID(String.valueOf(doctorContext.getHospitalID()));
            newEntry.setDiagnosis(diagnosis);
            newEntry.setTreatmentPlan(treatment);
            newEntry.setConsultationNotes(notes);

            medicalRecordsTable.addEntry(newEntry);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding medical entry: " + e.getMessage());
            return false;
        }
    }

    // === CSV OPERATIONS ===
    //TODO: Refactor
    private void createContactInfoCSV() throws IOException {
        try (FileWriter writer = new FileWriter(CONTACT_INFO_CSV)) {
            writer.write("PatientID,PhoneNumber,Email,Address\n");
        }
    }


    //TODO: Refactor
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


    public MedicalRecord getMedicalRecord(String patientID) {
        return (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
    }
}