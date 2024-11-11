
package org.hms.services.medicalrecord;

import org.hms.entities.BloodType;
import org.hms.services.appointment.AppointmentOutcome;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord implements Serializable {
    private String patientID;
    private BloodType bloodType;
    private List<MedicalEntry> medicalHistory;

    public MedicalRecord(String patientID, BloodType bloodType) {
        this.patientID = patientID;
        this.bloodType = bloodType;
        this.medicalHistory = new ArrayList<>();
    }

    // Add entry created from scratch
    public void addMedicalEntry(String diagnosis, String treatmentPlan, String consultationNotes) {
        MedicalEntry entry = new MedicalEntry(diagnosis, treatmentPlan, consultationNotes);
        medicalHistory.add(entry);
    }

    // Add entry from appointment outcome
    public void addMedicalEntryFromAppointment(AppointmentOutcome outcome,
                                               String diagnosis,
                                               String treatmentPlan) {
        MedicalEntry entry = MedicalEntry.fromAppointmentOutcome(outcome, diagnosis, treatmentPlan);
        medicalHistory.add(entry);
    }

    // Getters
    public List<MedicalEntry> getMedicalHistory() {
        return medicalHistory;
    }

    public BloodType getBloodType() {
        return bloodType;
    }
}
