package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTableEntry;
import org.hms.entities.BloodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The PatientParticulars class represents the details of a patient, including
 * their ID, name, date of birth, gender, and blood type. This class extends the
 * AbstractTableEntry, allowing it to be used as an entry in a table.
 */
public class PatientParticulars extends AbstractTableEntry {
    /**
     * Represents the unique identifier assigned to a patient.
     * This identifier is used to distinguish each patient in the system.
     */
    private String patientID;
    /**
     * Represents the name of a patient.
     */
    private String name;
    /**
     * Stores the date of birth of the patient.
     */
    private Date dateOfBirth;
    /**
     * Represents the gender of the patient.
     */
    private String gender;
    /**
     * Represents the blood type of the patient.
     * Using the BloodType enum, it can indicate various blood types such as POSITIVE_O, NEGATIVE_A, etc.
     */
    private BloodType bloodType;

    /**
     * Default constructor for the PatientParticulars class.
     * Initializes a new instance of PatientParticulars with a default identifier.
     */
    public PatientParticulars() {
        super(-1);
    }

    /**
     * Constructs a new instance of PatientParticulars with the specified details.
     *
     * @param entryID     the unique identifier for the table entry
     * @param patientID   the unique identifier for the patient
     * @param name        the name of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param gender      the gender of the patient
     * @param bloodType   the blood type of the patient
     */
    public PatientParticulars(int entryID, String patientID, String name, Date dateOfBirth,
                              String gender, BloodType bloodType) {
        super(entryID);
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    /**
     * Constructs a new PatientParticulars object with the specified details.
     *
     * @param entryID     the unique identifier for the table entry
     * @param patientID   the unique identifier for the patient
     * @param name        the name of the patient
     * @param dateOfBirth the date of birth of the patient in "yyyy-MM-dd" format
     * @param gender      the gender of the patient
     * @param bloodType   the blood type of the patient as a string
     * @throws ParseException if the dateOfBirth is not in the expected "yyyy-MM-dd" format
     */
    public PatientParticulars(int entryID, String patientID, String name, String dateOfBirth, String gender, String bloodType) throws ParseException {
        super(entryID);
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
        this.gender = gender;
        this.bloodType = BloodType.valueOf(bloodType);
    }

    /**
     * Converts a BloodType enum to its string representation.
     *
     * @param bloodType the BloodType enum to be converted
     * @return the string representation of the specified BloodType
     */
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

    /**
     * Retrieves the ID of the patient.
     *
     * @return the patientID of the patient
     */
    // Getters
    public String getPatientID() {
        return patientID;
    }

    /**
     * Retrieves the name of the patient.
     *
     * @return the name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the date of birth of the patient.
     *
     * @return the date of birth of the patient
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Retrieves the gender of the patient.
     *
     * @return the gender of the patient
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the blood type of the patient.
     *
     * @return the blood type of the patient
     */
    public BloodType getBloodType() {
        return bloodType;
    }

    /**
     * Converts the patient's particulars to a CSV string format. Each field is
     * converted to a string and concatenated with commas. Fields containing commas
     * are enclosed in double quotes, and any existing double quotes within fields
     * are doubled.
     *
     * @return a CSV formatted string representing the patient's particulars
     */
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
     * Loads the patient particulars from a given CSV string.
     *
     * @param csvLine the CSV string containing patient particulars data.
     *                Expected format: "entryID,patientID,name,dateOfBirth,gender,bloodType".
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

    /**
     * Formats the patient's particulars as a string according to the given format pattern.
     *
     * @param formatString the format pattern to use for formatting. This pattern must contain placeholders
     *                     for the patient's particulars such as table entry ID, patient ID, name, date of birth, gender, and blood type.
     * @return a formatted string representing the patient's particulars
     */
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