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
}
