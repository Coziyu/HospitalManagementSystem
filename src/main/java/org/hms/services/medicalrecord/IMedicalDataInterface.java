package org.hms.services.medicalrecord;


import org.hms.services.storage.IDataInterface;

import java.util.Optional;

/**
 * This interface should be implemented by StorageService.
 * It should contain methods that requires getting data from
 * persistent storage.
 */
public interface IMedicalDataInterface extends IDataInterface {

    PatientTable getPatientTable();

    MedicalRecord getMedicalRecordTable();
}
