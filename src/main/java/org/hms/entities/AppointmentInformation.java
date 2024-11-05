package org.hms.entities;

import java.io.Serializable;
import java.util.Date;

public class AppointmentInformation implements Serializable {
    int appointmentID;
    String patientID;
    String doctorID;
    Date appointmentTimeSlot;


    AppointmentStatus appointmentStatus;

    public AppointmentInformation(int appointmentID,
                                  String patientID,
                                  String doctorID,
                                  Date appointmentTimeSlot,
                                  AppointmentStatus appointmentStatus) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentTimeSlot = appointmentTimeSlot;
        this.appointmentStatus = appointmentStatus;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public Date getAppointmentTimeSlot() {
        return appointmentTimeSlot;
    }

    public void setAppointmentTimeSlot(Date appointmentTimeSlot) {
        this.appointmentTimeSlot = appointmentTimeSlot;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
