package org.hms.services.medicalrecord;

import java.io.Serializable;

public class ContactInformation implements Serializable {
    private String patientID;  // Added to link to patient
    private String phoneNumber;
    private String email;
    private String address;

    public ContactInformation(String patientID, String phoneNumber, String email, String address) {
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
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
}