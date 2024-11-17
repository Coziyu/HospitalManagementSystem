package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTableEntry;

/**
 * The ContactInformation class represents the contact details of a patient,
 * including their phone number, email, and address. This class extends
 * AbstractTableEntry and provides functionality to store and retrieve these
 * details, as well as methods to serialize and deserialize instances from CSV format.
 */
public class ContactInformation extends AbstractTableEntry {
    /**
     * Represents the unique identifier for the patient associated with this contact information.
     * This ID serves as a link to the corresponding patient record in the system.
     */
    private String patientID;  // Added to link to patient
    /**
     * Stores the phone number of the patient in the contact information.
     */
    private String phoneNumber;
    /**
     * Stores the email address associated with the patient's contact information.
     * This value is utilized for communication purposes, allowing the system to
     * send notifications, updates, or any relevant information to the patient's
     * registered email address.
     */
    private String email;
    /**
     * Stores the address of the patient.
     */
    private String address;

    /**
     * Default constructor for the ContactInformation class.
     * Initializes the instance with a default table entry ID of -1.
     */
    public ContactInformation() {
        super(-1);
    }

    /**
     * Constructs a new ContactInformation object with the specified details.
     *
     * @param entryID     the unique identifier for the table entry
     * @param patientID   the unique identifier for the patient
     * @param phoneNumber the phone number of the patient
     * @param email       the email address of the patient
     * @param address     the physical address of the patient
     */
    public ContactInformation(int entryID, String patientID, String phoneNumber, String email, String address) {
        super(entryID);
        this.patientID = patientID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    /**
     * Retrieves the unique identifier (ID) of the patient associated with
     * this contact information entry.
     *
     * @return the patient ID as a String.
     */
    // Getters
    public String getPatientID() {
        return patientID;
    }

    /**
     * Sets the unique identifier for the patient.
     *
     * @param patientID the unique identifier to set for the patient
     */
    // Setters for updates
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * Retrieves the phone number associated with the contact information.
     *
     * @return the phone number as a {@code String}
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number for this contact information.
     *
     * @param phoneNumber the new phone number to be set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the email address of the contact.
     *
     * @return the email address of the contact
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for the contact information.
     *
     * @param email the email address to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the address associated with the contact information.
     *
     * @return the address as a String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address for the contact information.
     *
     * @param address The new address to be set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Converts the contact information to a CSV string representation.
     * This includes the table entry ID, patient ID, phone number, email, and address,
     * all separated by commas.
     *
     * @return a string containing the CSV representation of the contact information
     */
    @Override
    public String toCSVString() {
        return String.format("%s,%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(patientID),
                preprocessCSVString(phoneNumber),
                preprocessCSVString(email),
                preprocessCSVString(address)
        );
    }

    /**
     * Loads the contact information of a patient from a CSV string.
     * The CSV string is assumed to have the following format:
     * tableEntryID,patientID,phoneNumber,email,address
     *
     * @param csvLine a CSV formatted string containing contact information.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        patientID = parts[1];
        phoneNumber = parts[2];
        email = parts[3];
        address = parts[4];
    }

    /**
     * Converts the contact information into a formatted string based on the given format string.
     *
     * @param formatString the format string that specifies the desired output format
     * @return a formatted string containing the contact information
     */
    public String toPrintString(String formatString) {
        String printString = String.format(formatString,
                getTableEntryID(),
                patientID,
                phoneNumber,
                email,
                address
        );
        return printString;
    }
}