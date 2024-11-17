
package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTableEntry;
import org.hms.services.appointment.AppointmentOutcome;

import java.io.Serializable;

/**
 * The MedicalEntry class represents a record of a medical consultation which includes
 * details such as patient ID, doctor ID, date of consultation, diagnosis, treatment plan,
 * and consultation notes.
 */
public class MedicalEntry extends AbstractTableEntry {
    /**
     * Represents the unique identifier for a patient in a medical entry.
     */
    private String patientID;
    /**
     * Represents the unique identifier of a doctor associated with a medical entry.
     */
    private String doctorID;
    /**
     * Represents the date of the medical entry. This date is typically the date when
     * the consultation or medical event occurred.
     */
    private String date;
    /**
     * Represents the medical diagnosis associated with a medical entry.
     */
    private String diagnosis;
    /**
     * Represents the treatment plan prescribed by the doctor for the patient's condition.
     * This variable holds a detailed description of the steps or actions to be taken
     * for treating the patient's diagnosis.
     */
    private String treatmentPlan;
    /**
     * Stores the notes taken during a medical consultation.
     */
    private String consultationNotes;

    /**
     * Default constructor for the MedicalEntry class.
     * Initializes a new MedicalEntry instance with a default invalid entry ID.
     * Invokes the superclass constructor from AbstractTableEntry with an entry ID of -1.
     */
    public MedicalEntry() {
        super(-1);
    }

    /**
     * Constructs a MedicalEntry object with the specified details.
     *
     * @param entryID           the unique identifier for the medical entry
     * @param patientID         the unique identifier for the patient
     * @param doctorID          the unique identifier for the doctor
     * @param diagnosis         the diagnosis details of the patient
     * @param treatmentPlan     the treatment plan prescribed for the patient
     * @param consultationNotes additional consultation notes or observations
     */
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

    /**
     * Constructs a MedicalEntry object.
     *
     * @param entryID       The unique identifier for the medical entry.
     * @param patientID     The unique identifier for the patient.
     * @param doctorID      The unique identifier for the doctor.
     * @param outcome       The outcome of the appointment containing the consultation notes.
     * @param diagnosis     The diagnosis for the medical entry.
     * @param treatmentPlan The treatment plan detailed for the patient.
     */
    public MedicalEntry(int entryID, String patientID, String doctorID, AppointmentOutcome outcome, String diagnosis, String treatmentPlan) {
        super(entryID);
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = java.time.LocalDate.now().toString();
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.consultationNotes = outcome.getConsultationNotes();
    }


    /**
     * Retrieves the ID of the patient associated with this medical entry.
     *
     * @return the patient ID
     */
    // Getters
    public String getPatientID() {
        return patientID;
    }

    /**
     * Sets the patient ID for the medical entry.
     *
     * @param patientID the patient ID to set
     */
    // Setters
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Retrieves the identifier of the doctor associated with this medical entry.
     *
     * @return the doctor's ID as a String.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Sets the doctor ID for this medical entry.
     *
     * @param doctorID The ID of the doctor to be associated with this medical entry.
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Retrieves the date of the medical entry.
     *
     * @return the date associated with the medical entry
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date for the medical entry.
     *
     * @param date the date to set for the medical entry, formatted as a String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Retrieves the diagnosis information for the medical entry.
     *
     * @return the diagnosis information as a String
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Sets the diagnosis for the medical entry.
     *
     * @param diagnosis the diagnosis to be set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Retrieves the treatment plan for the medical entry.
     *
     * @return the treatment plan
     */
    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    /**
     * Sets the treatment plan for the medical entry.
     *
     * @param treatmentPlan the treatment plan to be assigned to the medical entry
     */
    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    /**
     * Retrieves the consultation notes associated with this medical entry.
     *
     * @return the consultation notes as a String
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Sets the consultation notes for the medical entry.
     *
     * @param consultationNotes the notes from the consultation to be added to the medical entry
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Converts the medical entry to a CSV string format.
     *
     * @return a CSV string representation of the medical entry
     */
    @Override
    public String toCSVString() {
        return String.format("%s,%S,%s,%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(getPatientID()),
                preprocessCSVString(getDoctorID()),
                preprocessCSVString(getDate()),
                preprocessCSVString(getDiagnosis()),
                preprocessCSVString(getTreatmentPlan()),
                preprocessCSVString(getConsultationNotes())
        );
    }

    /**
     * Loads the medical entry details from a CSV formatted string.
     *
     * @param csvLine a CSV formatted string containing the details of a medical entry
     */
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

    /**
     * Generates a formatted string representing the current state of the MedicalEntry object.
     *
     * @param formatString the format string specifying how the fields should be formatted
     * @return a formatted string containing the table entry ID, patient ID, doctor ID, date, diagnosis, treatment plan, and consultation notes
     */
    public String toPrintString(String formatString) {
        String printString = String.format(formatString,
                getTableEntryID(),
                getPatientID(),
                getDoctorID(),
                getDate(),
                getDiagnosis(),
                getTreatmentPlan(),
                getConsultationNotes()
        );
        return printString;
    }
}
