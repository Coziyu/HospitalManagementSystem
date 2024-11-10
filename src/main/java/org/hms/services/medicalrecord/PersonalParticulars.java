package org.hms.services.medicalrecord;

import org.hms.entities.BloodType;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalParticulars implements Serializable {
    private String patientID;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private BloodType bloodType;

    public PersonalParticulars(String patientID, String name, Date dateOfBirth,
                               String gender, BloodType bloodType) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    // Static factory method to create from patient list CSV row
    public static PersonalParticulars fromPatientList(String[] csvRow) throws Exception {
        try {
            String patientID = csvRow[0].trim();
            String name = csvRow[1].trim();
            Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(csvRow[2].trim());
            String gender = csvRow[3].trim();
            BloodType bloodType = BloodType.valueOf(
                    csvRow[4].trim().replace("+", "_POSITIVE").replace("-", "_NEGATIVE")
            );

            return new PersonalParticulars(patientID, name, dob, gender, bloodType);
        } catch (Exception e) {
            throw new Exception("Error creating PersonalParticulars from CSV: " + e.getMessage());
        }
    }

    // Getters
    public String getPatientID() { return patientID; }
    public String getName() { return name; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public BloodType getBloodType() { return bloodType; }
}