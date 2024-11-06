package org.hms.services.appointment;

import org.hms.services.drugdispensary.DrugRequest;

import java.io.Serializable;
import java.util.ArrayList;

public class AppointmentOutcome implements Serializable {
    private String appointmentID;
    private String typeOfAppointment;
    private String consultationNotes;
    private ArrayList<DrugRequest> prescribedMedication;
}
