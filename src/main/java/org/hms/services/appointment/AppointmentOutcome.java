package org.hms.services.appointment;

import org.hms.services.drugdispensary.DrugDispenseRequest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The AppointmentOutcome class represents the outcome of an appointment. It includes details
 * such as appointment ID, patient ID, type of appointment, consultation notes, and prescribed medications.
 */
public class AppointmentOutcome implements Serializable {
    /**
     * Represents the unique identifier for a specific appointment.
     */
    private String appointmentID;
    /**
     * Represents the unique identifier for the patient associated with this appointment outcome.
     */
    private String patientID;
    /**
     * The type of appointment represented as a string. This can include various types
     * of appointments such as consultation, follow-up, or any other specific type associated
     * with the appointment.
     */
    private String typeOfAppointment;
    /**
     * Notes provided by the medical professional during the consultation of an appointment.
     */
    private String consultationNotes;
    /**
     * A list of drug dispense requests representing the medications prescribed during the appointment.
     */
    private ArrayList<DrugDispenseRequest> prescribedMedication;

    /**
     * Constructs an AppointmentOutcome with the specified details.
     *
     * @param appointmentID        the unique identifier for the appointment
     * @param patientID            the unique identifier for the patient
     * @param typeOfAppointment    the type of appointment conducted
     * @param consultationNotes    the notes taken during the consultation
     * @param prescribedMedication a list of prescribed medications resulting from the appointment
     */
    public AppointmentOutcome(String appointmentID, String patientID, String typeOfAppointment, String consultationNotes, ArrayList<DrugDispenseRequest> prescribedMedication) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.typeOfAppointment = typeOfAppointment;
        this.consultationNotes = consultationNotes;
        this.prescribedMedication = prescribedMedication;
    }

    /**
     * Retrieves the ID of the appointment.
     *
     * @return the appointment ID as a {@code String}.
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Sets the unique identifier for the appointment.
     *
     * @param appointmentID the unique identifier to set for the appointment
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * Retrieves the ID of the patient associated with the appointment outcome.
     *
     * @return the patient ID.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Sets the patient ID for the appointment outcome.
     *
     * @param patientID the unique identifier for the patient.
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Retrieves the type of appointment.
     *
     * @return the type of appointment as a String.
     */
    public String getTypeOfAppointment() {
        return typeOfAppointment;
    }

    /**
     * Sets the type of the appointment.
     *
     * @param typeOfAppointment the type of the appointment to be set
     */
    public void setTypeOfAppointment(String typeOfAppointment) {
        this.typeOfAppointment = typeOfAppointment;
    }

    /**
     * Retrieves the consultation notes from the appointment outcome.
     *
     * @return A string containing the consultation notes.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Sets the consultation notes for the appointment.
     *
     * @param consultationNotes the notes from the consultation to be set
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Retrieves the list of prescribed medications associated with the appointment outcome.
     *
     * @return an ArrayList of DrugDispenseRequest objects representing the prescribed medications.
     */
    public ArrayList<DrugDispenseRequest> getPrescribedMedication() {
        return prescribedMedication;
    }

    /**
     * Sets the list of prescribed medications for this AppointmentOutcome.
     *
     * @param prescribedMedication the list of DrugDispenseRequest objects representing the prescribed medications
     */
    public void setPrescribedMedication(ArrayList<DrugDispenseRequest> prescribedMedication) {
        this.prescribedMedication = prescribedMedication;
    }

    /**
     * Generates a string representation of the appointment outcome.
     *
     * @return A formatted string containing details of the appointment ID, type of appointment,
     * and consultation notes.
     */
    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(100);
        printStringBuilder.append("Appointment ID: ").append(appointmentID).append("\n");
        printStringBuilder.append("Type of Appointment: ").append(typeOfAppointment).append("\n");
        printStringBuilder.append("Consultation Notes: ").append(consultationNotes);

        return printStringBuilder.toString();
    }
}


