package org.hms.services.medicalrecord;

import org.hms.entities.UserContext;
import org.hms.entities.PatientContext;
import org.hms.entities.UserRole;
import org.hms.entities.BloodType;
import org.hms.services.medicalrecord.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordServicePatientTest {
    @Test
    public void testViewOwnRecord() {
        MockMedicalDataInterface mockStorage = new MockMedicalDataInterface();
        MedicalRecordService service = new MedicalRecordService(mockStorage);

        UserContext userContext = new UserContext("Alice", UserRole.PATIENT, 1001);
        PatientContext patient = new PatientContext(userContext, 1001);

        System.out.println("Patient ID type: " + patient.getPatientID().getClass());
        System.out.println("Patient ID value: " + patient.getPatientID());

        MedicalRecord record = service.viewOwnRecord(patient);
    }
}