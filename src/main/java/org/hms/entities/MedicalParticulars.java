package org.hms.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicalParticulars implements Serializable {
    String patientID;
    BloodType bloodType;
    ArrayList<String> medicalHistory;
}
