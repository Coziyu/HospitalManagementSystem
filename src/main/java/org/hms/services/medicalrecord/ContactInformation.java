package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTableEntry;

public class ContactInformation extends AbstractTableEntry {
    private String patientID;  // Added to link to patient
    private String phoneNumber;
    private String email;
    private String address;

    public ContactInformation(){
        super(-1);
    }

    public ContactInformation(int entryID, String patientID, String phoneNumber, String email, String address) {
        super(entryID);
        this.patientID = patientID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    // Getters
    public String getPatientID() { return patientID; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    // Setters for updates
    public void setPatientID(String patientID) { this.patientID = patientID; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }

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

    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        patientID = parts[1];
        phoneNumber = parts[2];
        email = parts[3];
        address = parts[4];
    }

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