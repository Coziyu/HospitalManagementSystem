
package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTableEntry;
import org.hms.services.appointment.AppointmentOutcome;
import java.io.Serializable;

public class MedicalEntry extends AbstractTableEntry {
    private String patientID;
    private String doctorID;
    private String date;
    private String diagnosis;
    private String treatmentPlan;
    private String consultationNotes;

    public MedicalEntry() {
        super(-1);
    }

    // Constructor for creating from scratch
    public MedicalEntry(int entryID, String patientID, String doctorID, String diagnosis, String treatmentPlan, String consultationNotes) {
        super(entryID);
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = java.time.LocalDate.now().toString();
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.consultationNotes = consultationNotes;
    }

    public MedicalEntry(int entryID, String patientID, String doctorID, AppointmentOutcome outcome, String diagnosis, String treatmentPlan) {
        super(entryID);
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = java.time.LocalDate.now().toString();
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.consultationNotes = outcome.getConsultationNotes();
    }



    // Getters
    public String getPatientID() { return patientID; }
    public String getDoctorID() { return doctorID; }
    public String getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatmentPlan() { return treatmentPlan; }
    public String getConsultationNotes() { return consultationNotes; }

    // Setters
    public void setPatientID(String patientID) { this.patientID = patientID; }
    public void setDoctorID(String doctorID) { this.doctorID = doctorID; }
    public void setDate(String date) { this.date = date; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan; }
    public void setConsultationNotes(String consultationNotes) { this.consultationNotes = consultationNotes; }

    @Override
    public String toCSVString() {
        return String.format("%s,%S,%s,%s,%s,%s,%s\n",
                getTableEntryID(),
                preprocessCSVString(getPatientID()),
                preprocessCSVString(getDoctorID()),
                preprocessCSVString(getDate()),
                preprocessCSVString(getDiagnosis()),
                preprocessCSVString(getTreatmentPlan()),
                preprocessCSVString(getConsultationNotes())
        );
    }

    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        patientID = parts[1];
        doctorID = parts[2];
        date = parts[3];
        diagnosis = parts[4];
        treatmentPlan = parts[5];
        consultationNotes = parts[6];
    }
}
