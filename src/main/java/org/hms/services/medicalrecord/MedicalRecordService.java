package org.hms.services.medicalrecord;

import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.services.AbstractService;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MedicalRecordService extends AbstractService<IMedicalDataInterface> {
    // CSV paths
    private static final String DATA_DIRECTORY = "data";
    private static final String PATIENT_LIST_CSV = DATA_DIRECTORY + "/PatientList.csv";
    private static final String CONTACT_INFO_CSV = DATA_DIRECTORY + "/contact_information.csv";
    private static final String MEDICAL_RECORDS_CSV = DATA_DIRECTORY + "/medical_records.csv";

    // In-memory storage
    private PatientTable patientTable;
    private ContactInformationTable contactInformationTable;
    private MedicalRecord medicalRecordsTable;

    public MedicalRecordService(IMedicalDataInterface storageService) {
        this.storageServiceInterface = storageService;
        this.patientTable = storageServiceInterface.getPatientTable();
        this.contactInformationTable = storageServiceInterface.getContactInformationTable();
        this.medicalRecordsTable = storageServiceInterface.getMedicalRecordTable();
    }


    // === PATIENT METHODS ===

    /**
     * This method returns a PrintString of a MedicalRecord, containing MedicalEntry Objects
     * for a specific patient.
     * @param patientID of the patient
     * @return A printString for the patient's medical record
     */
    public String getPatientMedicalRecord(String patientID) {
        MedicalRecord medicalRecord = (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
        return medicalRecord.toPrintString();
    }
    public String getPatientMedicalRecord(String doctorID, String patientID) {
        MedicalRecord medicalRecord =
                (MedicalRecord) medicalRecordsTable
                        .filterByAttribute(MedicalEntry::getPatientID, patientID)
                        .filterByAttribute(MedicalEntry::getDoctorID, doctorID);
        return medicalRecord.toPrintString();
    }

    /**
     * This method returns a PrintString of a MedicalEntry, containing a single MedicalEntry
     * @param entryID
     * @return A printString for the patient's medical record
     */
    public String getMedicalRecordEntry(int entryID) {
        MedicalRecord medicalRecord = (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getTableEntryID, entryID);
        return medicalRecord.toPrintString();
    }

    /**
     * This method returns a list of all valid entry IDs for a patient
     * @param patientID
     * @return A list of valid entry IDs
     */
    public List<Integer> getPatientMedicalRecordEntryIDs(String patientID) {
        MedicalRecord medicalRecord = (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
        return medicalRecord.getValidEntryNumbers();
    }

    public List<Integer> getPatientMedicalRecordEntryIDs(String doctorID, String patientID) {
        MedicalRecord medicalRecord =
                (MedicalRecord) medicalRecordsTable
                        .filterByAttribute(MedicalEntry::getDoctorID, doctorID)
                        .filterByAttribute(MedicalEntry::getPatientID, patientID);

        return medicalRecord.getValidEntryNumbers();
    }

    /**
     * Returns the personal particulars for a patient.
     * @param patientID of the patient.
     * @return the personal particulars of the patient.
     */
    public PatientParticulars getPersonalParticulars(String patientID) {
        return patientTable.searchByAttribute(PatientParticulars::getPatientID, patientID).getFirst();
    }

    /**
     * @param patientID
     * @return a PrintString of a PatientParticulars
     */
    public String getPatientPersonalParticulars(String patientID){
        return ((PatientTable) (patientTable.filterByAttribute(PatientParticulars::getPatientID, patientID))).toPrintString();
    }

    /**
     * This method updates the contact information for a patient
     * @param patientID
     * @param phone
     * @param email
     * @param address
     * @return
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

    public String getPatientContactInformation(String patientID){
        ContactInformationTable patientEntry = (ContactInformationTable) contactInformationTable.filterByAttribute(ContactInformation::getPatientID, patientID);
        return patientEntry.toPrintString();
    }

    // === CSV OPERATIONS ===


    private MedicalRecord getMedicalRecord(String patientID) {
        return (MedicalRecord) medicalRecordsTable.filterByAttribute(MedicalEntry::getPatientID, patientID);
    }

    private ContactInformation getPatientContactInformationEntry(String patientID){
        return contactInformationTable.searchByAttribute(ContactInformation::getPatientID,patientID).getFirst();
    }

    public void updatePatientPhoneNumber(String patientID, String newPhoneNumber) {
        ContactInformation contactInfo = getPatientContactInformationEntry(patientID);
        contactInfo.setPhoneNumber(newPhoneNumber);
        try {
            contactInformationTable.replaceEntry(contactInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePatientEmail(String patientID, String newEmail) {
        ContactInformation contactInfo = getPatientContactInformationEntry(patientID);
        contactInfo.setEmail(newEmail);
        try {
            contactInformationTable.replaceEntry(contactInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
     * Returns a list of unique patient IDs that have been treated by a specific doctor
     * @param doctorID
     * @return
     */
    public String[] getPatientIDsTreatedByDoctor(String doctorID) {
        return medicalRecordsTable.searchByAttribute(MedicalEntry::getDoctorID, doctorID)
                .stream()
                .map(MedicalEntry::getPatientID).
                distinct().
                toArray(String[]::new);
    }

    /**
     * Returns a list of unique patient particulars that have been treated by a specific doctor
     * @param doctorID
     * @return
     */
    public String getPatientParticularsTreatedByDoctor(String doctorID) {
        List<String> patientIDs = Arrays.asList(getPatientIDsTreatedByDoctor(doctorID));

        Predicate<String> isInPatientIDs = patientIDs::contains;

        return ( (PatientTable) patientTable.filterByCondition(PatientParticulars::getPatientID, isInPatientIDs)).toPrintString();

    }

    /**
     * Returns whether a patient has been treated by a specific doctor
     * @param patientID
     * @param doctorID
     * @return True if the patient has been treated by the doctor
     */
    public boolean isPatientTreatedByDoctor(String patientID, String doctorID) {
        return medicalRecordsTable.searchByAttribute(MedicalEntry::getPatientID, patientID)
                .stream()
                .anyMatch(medicalEntry -> medicalEntry.getDoctorID().equals(doctorID));
    }

    /**
     * Returns whether a patient exists
     * @param patientID
     * @return True if the patient exists
     */
    public boolean patientExists(String patientID) {
        return !patientTable.searchByAttribute(PatientParticulars::getPatientID, patientID).isEmpty();
    }

    /**
     * Updates the diagnosis of a medical entry
     * @param entryID
     * @param diagnosis
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

    public void updateTreatmentPlan(int entryID, String treatmentPlan) {
        MedicalEntry medicalEntry = medicalRecordsTable.filterByAttribute(MedicalEntry::getTableEntryID, entryID).getEntry(entryID);
        medicalEntry.setTreatmentPlan(treatmentPlan);
        try {
            medicalRecordsTable.replaceEntry(medicalEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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