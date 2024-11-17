package org.hms.services.medicalrecord;

import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.services.AbstractService;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * MedicalRecordService is responsible for managing medical records, personal
 * particulars, and contact information related to patients in the system.
 * It extends the AbstractService class and implements methods for handling
 * various operations such as retrieving medical records, updating patient information,
 * and managing medical entries by doctors.
 */
public class MedicalRecordService extends AbstractService<IMedicalDataInterface> {
    /**
     * The directory path where all CSV data files are stored.
     * This constants is used for accessing various medical records
     * and patient-related data stored in CSV format.
     */
    // CSV paths
    private static final String DATA_DIRECTORY = "data";
    /**
     * Represents the file path to the CSV containing the patient list.
     * This constant is used to locate the 'PatientList.csv' file within the data directory.
     */
    private static final String PATIENT_LIST_CSV = DATA_DIRECTORY + "/PatientList.csv";
    /**
     * Constant representing the file path to the CSV file containing patient contact information.
     * This file is located in the data directory, which is defined by the `DATA_DIRECTORY` constant.
     */
    private static final String CONTACT_INFO_CSV = DATA_DIRECTORY + "/contact_information.csv";
    /**
     * The file path to the medical records CSV file.
     * This path is constructed by combining the base data directory and
     * the specific file name "medical_records.csv". It is used for accessing
     * and managing medical records for patients within the system.
     */
    private static final String MEDICAL_RECORDS_CSV = DATA_DIRECTORY + "/medical_records.csv";

    /**
     * An in-memory table storing patient particulars including their ID, name, birth date, gender, and blood type.
     * This table is utilized to manage patient-related data and supports operations for querying and updating patient information.
     */
    // In-memory storage
    private PatientTable patientTable;
    /**
     * A table of contact information entries specific to the medical records service.
     * Utilized primarily for managing and accessing patients' contact information,
     * such as phone numbers, email addresses, and physical addresses.
     */
    private ContactInformationTable contactInformationTable;
    /**
     * A reference to the table containing medical records.
     * This table manages entries related to patients' medical history,
     * treatments, diagnoses, and consultation notes.
     */
    private MedicalRecord medicalRecordsTable;

    /**
     * Constructs an instance of MedicalRecordService which initializes necessary tables
     * for managing medical records, patient information, and contact details.
     *
     * @param storageService An implementation of IMedicalDataInterface that provides access
     *                       to the patient table, contact information table, and medical record table.
     */
    public MedicalRecordService(IMedicalDataInterface storageService) {
        this.storageServiceInterface = storageService;
        this.patientTable = storageServiceInterface.getPatientTable();
        this.contactInformationTable = storageServiceInterface.getContactInformationTable();
        this.medicalRecordsTable = storageServiceInterface.getMedicalRecordTable();
    }


    // === PATIENT METHODS ===

    /**
     * Retrieves the medical record for the specified patient ID.
     *
     * @param patientID The unique identifier of the patient whose medical record is to be retrieved.
     * @return A string representation of the patient's medical record.
     */
    public String getPatientMedicalRecord(String patientID) {
        MedicalRecord medicalRecord = (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
        return medicalRecord.toPrintString();
    }

    /**
     * Retrieves the medical record of a patient treated by a specific doctor.
     *
     * @param doctorID  the unique identifier of the doctor
     * @param patientID the unique identifier of the patient
     * @return a string representation of the patient's medical record
     */
    public String getPatientMedicalRecord(String doctorID, String patientID) {
        MedicalRecord medicalRecord =
                (MedicalRecord) medicalRecordsTable
                        .filterByAttribute(MedicalEntry::getPatientID, patientID)
                        .filterByAttribute(MedicalEntry::getDoctorID, doctorID);
        return medicalRecord.toPrintString();
    }

    /**
     * Retrieves the medical record entry with the specified entry ID.
     *
     * @param entryID the ID of the medical record entry to retrieve.
     * @return a formatted string representation of the medical record entry.
     */
    public String getMedicalRecordEntry(int entryID) {
        MedicalRecord medicalRecord = (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getTableEntryID, entryID);
        return medicalRecord.toPrintString();
    }

    /**
     * Retrieves a list of entry IDs for a given patient's medical records.
     *
     * @param patientID the unique identifier of the patient whose medical record entries are to be retrieved
     * @return a list of integers representing the entry IDs of the patient's medical records
     */
    public List<Integer> getPatientMedicalRecordEntryIDs(String patientID) {
        MedicalRecord medicalRecord = (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
        return medicalRecord.getValidEntryNumbers();
    }

    /**
     * Retrieves a list of valid medical record entry IDs for a specific patient and doctor.
     *
     * @param doctorID  the unique identifier for the doctor
     * @param patientID the unique identifier for the patient
     * @return a list of integers representing the valid entry IDs in the patient's medical record
     */
    public List<Integer> getPatientMedicalRecordEntryIDs(String doctorID, String patientID) {
        MedicalRecord medicalRecord =
                (MedicalRecord) medicalRecordsTable
                        .filterByAttribute(MedicalEntry::getDoctorID, doctorID)
                        .filterByAttribute(MedicalEntry::getPatientID, patientID);

        return medicalRecord.getValidEntryNumbers();
    }

    /**
     * Retrieves the personal particulars of a patient given their patient ID.
     *
     * @param patientID the ID of the patient whose personal particulars are to be retrieved
     * @return the PatientParticulars object containing the patient's personal details
     */
    public PatientParticulars getPersonalParticulars(String patientID) {
        return patientTable.searchByAttribute(PatientParticulars::getPatientID, patientID).getFirst();
    }

    /**
     * Retrieves the personal particulars of a patient by their patient ID.
     *
     * @param patientID the unique identifier of the patient whose personal particulars are to be retrieved
     * @return a string representation of the patient's personal particulars in an ASCII table format
     */
    public String getPatientPersonalParticulars(String patientID) {
        return ((PatientTable) (patientTable.filterByAttribute(PatientParticulars::getPatientID, patientID))).toPrintString();
    }

    /**
     * Updates the contact information of the patient identified by the given patient ID.
     * The method updates the phone number, email, and address of the patient.
     *
     * @param patientID the unique identifier for the patient whose contact information is to be updated
     * @param phone     the new phone number to be set for the patient
     * @param email     the new email address to be set for the patient
     * @param address   the new address to be set for the patient
     * @return true if the update was successful, false otherwise
     */
    public boolean updateOwnContactInfo(String patientID, String phone, String email, String address) {
        try {
            ContactInformation newContact = contactInformationTable.searchByAttribute(ContactInformation::getPatientID, patientID).getFirst();
            newContact.setPhoneNumber(phone);
            newContact.setEmail(email);
            newContact.setAddress(address);

            boolean success = contactInformationTable.replaceEntry(newContact);

            return success;

        } catch (Exception e) {
            System.err.println("Error updating contact info: " + e.getMessage());
            return false;
        }
    }

    // === DOCTOR METHODS ===

    /**
     * Views the medical record of a patient.
     * The method allows only doctors to view patient records.
     *
     * @param doctorContext the context of the doctor requesting the patient record
     * @param patientID     the ID of the patient whose medical record is being requested
     * @return the medical record of the patient if the doctor is authorized, null otherwise
     */
    public MedicalRecord viewPatientRecord(UserContext doctorContext, String patientID) {
        if (doctorContext.getUserType() != UserRole.DOCTOR) {
            System.err.println("Unauthorized: Only doctors can view patient records");
            return null;
        }
        return getMedicalRecord(patientID);
    }

    /**
     * Adds a new medical entry to the patient's record.
     *
     * @param doctorContext The context of the doctor adding the medical entry, which includes user role and hospital ID.
     * @param patientID     The unique identifier of the patient for whom the medical entry is being added.
     * @param diagnosis     The diagnosis associated with the medical entry.
     * @param treatment     The treatment plan associated with the medical entry.
     * @param notes         Additional consultation notes to be included with the medical entry.
     * @return true if the medical entry was successfully added, false otherwise.
     */
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

    /**
     * Retrieves the contact information of a patient by their patient ID.
     *
     * @param patientID the ID of the patient whose contact information is to be retrieved
     * @return a formatted string representation of the patient's contact information
     */
    public String getPatientContactInformation(String patientID) {
        ContactInformationTable patientEntry = (ContactInformationTable) contactInformationTable.filterByAttribute(ContactInformation::getPatientID, patientID);
        return patientEntry.toPrintString();
    }

    // === CSV OPERATIONS ===


    /**
     * Retrieves the medical record associated with a given patient ID.
     *
     * @param patientID the unique identifier of the patient whose medical record is to be retrieved
     * @return the MedicalRecord of the patient matching the provided patientID
     */
    private MedicalRecord getMedicalRecord(String patientID) {
        return (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
    }

    /**
     * Retrieves the contact information entry of a specific patient from the contact information table.
     *
     * @param patientID the unique identifier of the patient whose contact information is being retrieved
     * @return the ContactInformation instance associated with the given patient ID
     */
    private ContactInformation getPatientContactInformationEntry(String patientID) {
        return contactInformationTable.searchByAttribute(ContactInformation::getPatientID, patientID).getFirst();
    }

    /**
     * Updates the phone number associated with a patient's contact information.
     *
     * @param patientID      the unique identifier of the patient
     * @param newPhoneNumber the new phone number to be set for the patient
     */
    public void updatePatientPhoneNumber(String patientID, String newPhoneNumber) {
        ContactInformation contactInfo = getPatientContactInformationEntry(patientID);
        contactInfo.setPhoneNumber(newPhoneNumber);
        try {
            contactInformationTable.replaceEntry(contactInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the email address of a patient in the system.
     *
     * @param patientID The unique identifier of the patient whose email is to be updated.
     * @param newEmail  The new email address to update for the patient.
     */
    public void updatePatientEmail(String patientID, String newEmail) {
        ContactInformation contactInfo = getPatientContactInformationEntry(patientID);
        contactInfo.setEmail(newEmail);
        try {
            contactInformationTable.replaceEntry(contactInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the address of the specified patient.
     *
     * @param patientID  the unique identifier of the patient whose address is to be updated
     * @param newAddress the new address to be set for the patient
     */
    public void updatePatientAddress(String patientID, String newAddress) {
        ContactInformation contactInfo = getPatientContactInformationEntry(patientID);
        contactInfo.setAddress(newAddress);
        try {
            contactInformationTable.replaceEntry(contactInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of unique patient IDs treated by a specific doctor.
     *
     * @param doctorID The unique identifier of the doctor whose treated patient IDs are to be retrieved.
     * @return An array of unique patient IDs that have been treated by the specified doctor.
     */
    public String[] getPatientIDsTreatedByDoctor(String doctorID) {
        return medicalRecordsTable.searchByAttribute(MedicalEntry::getDoctorID, doctorID)
                .stream()
                .map(MedicalEntry::getPatientID).
                distinct().
                toArray(String[]::new);
    }

    /**
     * Retrieves the personal particulars of all patients treated by a specific doctor.
     *
     * @param doctorID The ID of the doctor whose patients' particulars are to be retrieved.
     * @return A string representation of the personal particulars of the patients treated by the specified doctor, formatted as an ASCII table.
     */
    public String getPatientParticularsTreatedByDoctor(String doctorID) {
        List<String> patientIDs = Arrays.asList(getPatientIDsTreatedByDoctor(doctorID));

        Predicate<String> isInPatientIDs = patientIDs::contains;

        return ((PatientTable) patientTable.filterByCondition(PatientParticulars::getPatientID, isInPatientIDs)).toPrintString();

    }

    /**
     * Checks whether a patient has been treated by a specific doctor.
     *
     * @param patientID the unique identifier of the patient
     * @param doctorID  the unique identifier of the doctor
     * @return true if the patient has been treated by the doctor, otherwise false
     */
    public boolean isPatientTreatedByDoctor(String patientID, String doctorID) {
        return medicalRecordsTable.searchByAttribute(MedicalEntry::getPatientID, patientID)
                .stream()
                .anyMatch(medicalEntry -> medicalEntry.getDoctorID().equals(doctorID));
    }

    /**
     * Checks if a patient with the given patient ID exists in the patient table.
     *
     * @param patientID the ID of the patient to check for existence
     * @return true if a patient with the given patient ID exists, false otherwise
     */
    public boolean patientExists(String patientID) {
        return !patientTable.searchByAttribute(PatientParticulars::getPatientID, patientID).isEmpty();
    }

    /**
     * Updates the diagnosis for a given medical record entry.
     *
     * @param entryID   The unique identifier of the medical record entry to be updated.
     * @param diagnosis The new diagnosis to be set for the specified medical record entry.
     */
    public void updateDiagnosis(int entryID, String diagnosis) {
        MedicalEntry medicalEntry = medicalRecordsTable.filterByAttribute(MedicalEntry::getTableEntryID, entryID).getEntry(entryID);
        medicalEntry.setDiagnosis(diagnosis);
        try {
            medicalRecordsTable.replaceEntry(medicalEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the treatment plan for a specific medical record entry.
     *
     * @param entryID       the unique identifier of the medical record entry
     * @param treatmentPlan the new treatment plan to be set for the medical record entry
     */
    public void updateTreatmentPlan(int entryID, String treatmentPlan) {
        MedicalEntry medicalEntry = medicalRecordsTable.filterByAttribute(MedicalEntry::getTableEntryID, entryID).getEntry(entryID);
        medicalEntry.setTreatmentPlan(treatmentPlan);
        try {
            medicalRecordsTable.replaceEntry(medicalEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the consultation notes for a specific medical record entry.
     *
     * @param entryID           the unique identifier of the medical record entry to update
     * @param consultationNotes the new consultation notes to be recorded
     */
    public void updateConsultationNotes(int entryID, String consultationNotes) {
        MedicalEntry medicalEntry = medicalRecordsTable.filterByAttribute(MedicalEntry::getTableEntryID, entryID).getEntry(entryID);
        medicalEntry.setConsultationNotes(consultationNotes);
        try {
            medicalRecordsTable.replaceEntry(medicalEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}