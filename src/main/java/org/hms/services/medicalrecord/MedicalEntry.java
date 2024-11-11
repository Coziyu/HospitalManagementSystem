
package org.hms.services.medicalrecord;

import org.hms.services.appointment.AppointmentOutcome;
import java.io.Serializable;

public class MedicalEntry implements Serializable {
    private String date;
    private String diagnosis;
    private String treatmentPlan;
    private String consultationNotes;

    // Constructor for creating from scratch
    public MedicalEntry(String diagnosis, String treatmentPlan, String consultationNotes) {
        this.date = java.time.LocalDate.now().toString();
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.consultationNotes = consultationNotes;
    }

    // Static factory method to create from AppointmentOutcome
    public static MedicalEntry fromAppointmentOutcome(AppointmentOutcome outcome,
                                                      String diagnosis,
                                                      String treatmentPlan) {
        MedicalEntry entry = new MedicalEntry(
                diagnosis,
                treatmentPlan,
                outcome.getConsultationNotes()
        );
        return entry;
    }

    // Getters
    public String getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatmentPlan() { return treatmentPlan; }
    public String getConsultationNotes() { return consultationNotes; }
}
