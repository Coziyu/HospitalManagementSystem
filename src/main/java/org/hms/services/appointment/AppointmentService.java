package org.hms.services.appointment;

import org.hms.services.AbstractService;

public class AppointmentService extends AbstractService<IAppointmentDataInterface> {

    public AppointmentService(IAppointmentDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
    }
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
