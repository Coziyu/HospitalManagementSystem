package org.hms;

import org.hms.services.AppointmentService;
import org.hms.services.PatientRecordService;
import org.hms.views.AuthenticationMenu;
import org.hms.views.AbstractMenu;

public class App {
    private final PatientRecordService patientRecordService;
    private final AppointmentService appointmentService;
    private AbstractMenu currentMenu;

    public App() {
        this.patientRecordService = new PatientRecordService();
        this.appointmentService = new AppointmentService();
    }

    public void setCurrentMenu(AbstractMenu menu) {
        this.currentMenu = menu;
    }

    public void run() {
        while (currentMenu != null) {
            currentMenu.displayAndExecute();
        }
    }

    public void initialise() {
        setCurrentMenu(new AuthenticationMenu(this));
    }

    public PatientRecordService getPatientRecordService() {
        return patientRecordService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }
}
