package org.hms.services.appointment;

import org.hms.services.drugdispensary.DrugDispenseRequest;

import java.io.Serializable;
import java.util.ArrayList;

public class AppointmentOutcome implements Serializable {
    private String appointmentID;
    private String patientID;
    private String typeOfAppointment;
    private String consultationNotes;
    private ArrayList<DrugDispenseRequest> prescribedMedication;

    public AppointmentOutcome(String appointmentID, String patientID, String typeOfAppointment, String consultationNotes, ArrayList<DrugDispenseRequest> prescribedMedication) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.typeOfAppointment = typeOfAppointment;
        this.consultationNotes = consultationNotes;
        this.prescribedMedication = prescribedMedication;
    }
  
    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getTypeOfAppointment() {
        return typeOfAppointment;
    }

    public void setTypeOfAppointment(String typeOfAppointment) {
        this.typeOfAppointment = typeOfAppointment;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public ArrayList<DrugDispenseRequest> getPrescribedMedication() {
        return prescribedMedication;
    }

    public void setPrescribedMedication(ArrayList<DrugDispenseRequest> prescribedMedication) {
        this.prescribedMedication = prescribedMedication;
    }

    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(100);
        printStringBuilder.append("Appointment ID: ").append(appointmentID).append("\n");
        printStringBuilder.append("Type of Appointment: ").append(typeOfAppointment).append("\n");
        printStringBuilder.append("Consultation Notes: ").append(consultationNotes);

        return printStringBuilder.toString();
    }
}


