package org.hms.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class AppointmentOutcome implements Serializable {
    String appointmentID;
    String typeOfAppointment;
    String consultationNotes;
    ArrayList<DrugRequest> prescribedMedication;
}
