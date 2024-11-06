package org.hms.services.medicalrecord;

import org.hms.entities.BloodType;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicalParticulars implements Serializable {
    String patientID;
    BloodType bloodType;
    ArrayList<String> medicalHistory;
}
