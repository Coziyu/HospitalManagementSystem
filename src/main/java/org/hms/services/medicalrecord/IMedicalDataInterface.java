package org.hms.services.medicalrecord;


import org.hms.services.storage.IDataInterface;

import java.util.Optional;

/**
 * This interface should be implemented by StorageService.
 * It should contain methods that requires getting data from
 * persistent storage.
 */
public interface IMedicalDataInterface extends IDataInterface {

    // Method to get appointment outcome (from CSV)
    /**
     * Retrieves a medical record for a given patient ID
     * @param patientID The ID of the patient
     * @return Optional containing the medical record if found
     */
    Optional<MedicalRecord> getMedicalRecord(String patientID);

    /**
     * Saves or updates a medical record
     * @param record The medical record to save
     * @return true if successful, false otherwise
     */
    boolean saveMedicalRecord(MedicalRecord record);

    /**
     * Updates contact information for a patient
     * @param patientID The ID of the patient
     * @param contact The new contact information
     * @return true if successful, false otherwise
     */
    boolean updateContactInformation(String patientID, ContactInformation contact);

    /**
     * Adds a medical entry to a patient's record
     * @param patientID The ID of the patient
     * @param entry The medical entry to add
     * @return true if successful, false otherwise
     */
    boolean addMedicalEntry(String patientID, MedicalEntry entry);

    MedicalRecord getMedicalRecordTable();
}
