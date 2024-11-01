package org.hms.services;

public class AppointmentService extends AbstractService {
    public boolean scheduleAppointment(int date) {
        return true;
    }

    public String viewDoctorSchedule(String doctorID) {
        return doctorID + ": " + "Doctor's schedule information!";
    }

    public String manageAppointmentRequests(String doctorID) {
        return doctorID + ": " + "Appointment requests information!";
    }

    public boolean updatePrescriptionStatus(String appointmentID) {
        return true;
    }
}
