package org.hms.services.medicalrecord;


import org.hms.services.storage.IDataInterface;

/**
 * This interface should be implemented by StorageService.
 * It should contain methods that requires getting data from
 * persistent storage.
 */
public interface IMedicalDataInterface extends IDataInterface {

    /**
     * Returns the PatientTable containing patient particulars.
     *
     * @return An instance of PatientTable containing patient-related data.
     */
    PatientTable getPatientTable();

    /**
     * Retrieves the table containing medical records.
     *
     * @return A MedicalRecord table.
     */
    MedicalRecord getMedicalRecordTable();

    /**
     * Retrieves the ContactInformationTable, which contains and manages contact information entries
     * such as phone numbers, emails, and addresses associated with patients.
     *
     * @return an instance of ContactInformationTable providing access to contact information entries.
     */
    ContactInformationTable getContactInformationTable();
}
