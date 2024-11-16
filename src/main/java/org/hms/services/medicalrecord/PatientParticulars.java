package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTableEntry;
import org.hms.entities.BloodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PatientParticulars extends AbstractTableEntry {
    private String patientID;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private BloodType bloodType;

    public PatientParticulars() {
        super(-1);
    }

    public PatientParticulars(int entryID, String patientID, String name, Date dateOfBirth,
                              String gender, BloodType bloodType) {
        super(entryID);
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    public PatientParticulars(int entryID, String patientID, String name, String dateOfBirth, String gender, String bloodType) throws ParseException {
        super(entryID);
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
        this.gender = gender;
        this.bloodType = BloodType.valueOf(bloodType);
    }

//    // Static factory method to create from patient list CSV row
//    public static PatientParticulars fromPatientList(String[] csvRow) throws Exception {
//        try {
//            String patientID = csvRow[0].trim();
//            String name = csvRow[1].trim();
//            Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(csvRow[2].trim());
//            String gender = csvRow[3].trim();
//            BloodType bloodType = BloodType.valueOf(
//                    (csvRow[4].trim().endsWith("+") ? "POSITIVE_" : "NEGATIVE_") +
//                            csvRow[4].trim().substring(0, csvRow[4].trim().length() - 1)
//            );
//            return new PatientParticulars(patientID, name, dob, gender, bloodType);
//        } catch (Exception e) {
//            throw new Exception("Error creating PersonalParticulars from CSV: " + e.getMessage());
//        }
//    }

    // Getters
    public String getPatientID() { return patientID; }
    public String getName() { return name; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public BloodType getBloodType() { return bloodType; }



    @Override
    public String toCSVString() {
        // Entries have to be converted to Strings
        // Convert Date to String
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(dateOfBirth);
        return String.format("%s,%s,%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(patientID),
                preprocessCSVString(name),
                preprocessCSVString(dateString),
                preprocessCSVString(gender),
                bloodType
        );
    }

    /**
     * @param csvLine comma seperated entry values.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = csvLine.split(",");
        tableEntryID = Integer.parseInt(parts[0]);
        patientID = parts[1];
        name = parts[2];
        try {
            dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(parts[3]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        gender = parts[4];
        bloodType = BloodType.valueOf(parts[5]);
    }

    private static String bloodTypeToPrintString(BloodType bloodType) {
        switch (bloodType) {
            case POSITIVE_O -> {
                return "O+";
            }
            case POSITIVE_A -> {
                return "A+";
            }
            case POSITIVE_B -> {
                return "B+";
            }
            case POSITIVE_AB -> {
                return "AB+";
            }
            case NEGATIVE_O -> {
                return "O-";
            }
            case NEGATIVE_A -> {
                return "A-";
            }
            case NEGATIVE_B -> {
                return "B-";
            }
            case NEGATIVE_AB -> {
                return "AB-";
            }
            case NULL -> {
                return "ඞ";
            }
        }
        return "ඞ";
    }

    public String toPrintString(String formatString) {
        String printString = String.format(formatString,
                getTableEntryID(),
                patientID,
                name,
                new SimpleDateFormat("yyyy-MM-dd").format(dateOfBirth),
                gender,
                bloodTypeToPrintString(bloodType)
        );
        return printString;
    }
}