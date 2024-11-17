package org.hms.services.appointment;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents the details of a medical appointment, including the appointment ID,
 * patient ID, doctor ID, appointment time slot, and the status of the appointment.
 * The class provides methods to get and set these details.
 */
public class AppointmentInformation implements Serializable {
    /**
     * Represents the status of the medical appointment.
     * It can be one of the following values: CONFIRMED, CANCELLED, COMPLETED, PENDING.
     */
    AppointmentStatus appointmentStatus;
    /**
     * Unique identifier for a medical appointment.
     */
    private int appointmentID;
    /**
     * The unique identifier for the patient associated with the appointment.
     */
    private String patientID;
    /**
     * The unique identifier for the doctor associated with the medical appointment.
     */
    private String doctorID;
    /**
     * The time slot allocated for the medical appointment.
     * This Date object represents the specific date and time
     * when the appointment is scheduled to occur.
     */
    private Date appointmentTimeSlot;

    /**
     * Constructs an AppointmentInformation object with the specified details.
     *
     * @param appointmentID       The unique identifier for the appointment.
     * @param patientID           The unique identifier for the patient.
     * @param doctorID            The unique identifier for the doctor.
     * @param appointmentTimeSlot The date and time slot for the appointment.
     * @param appointmentStatus   The current status of the appointment.
     */
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

    /**
     * Retrieves the unique identifier for the appointment.
     *
     * @return the appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Retrieves the ID of the patient associated with this appointment.
     *
     * @return The patient ID as a String.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Retrieves the ID of the doctor associated with the appointment.
     *
     * @return the doctor's ID as a String
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Sets the ID of the doctor associated with this appointment.
     *
     * @param doctorID the new doctor ID to set for this appointment
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Retrieves the time slot for the appointment.
     *
     * @return the appointment time slot as a {@code Date}.
     */
    public Date getAppointmentTimeSlot() {
        return appointmentTimeSlot;
    }

    /**
     * Sets the time slot for the appointment.
     *
     * @param appointmentTimeSlot the date and time slot when the appointment is scheduled to take place
     */
    public void setAppointmentTimeSlot(Date appointmentTimeSlot) {
        this.appointmentTimeSlot = appointmentTimeSlot;
    }

    /**
     * Retrieves the current status of the appointment.
     *
     * @return the current status of the appointment as an {@link AppointmentStatus} enum.
     */
    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    /**
     * Updates the status of the appointment.
     *
     * @param appointmentStatus the new status of the appointment. It should be an instance of
     *                          {@link AppointmentStatus}, which can be any of the following:
     *                          CONFIRMED, CANCELLED, COMPLETED, or PENDING.
     */
    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
