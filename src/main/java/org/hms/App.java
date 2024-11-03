package org.hms;

import org.hms.entities.UserContext;
import org.hms.services.AppointmentService;
import org.hms.services.MedicalRecordService;
import org.hms.views.AuthenticationMenu;
import org.hms.views.AbstractMenu;

public class App {
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final UserContext userContext;
    private AbstractMenu currentMenu;

    public App() {
        this.medicalRecordService = new MedicalRecordService();
        this.appointmentService = new AppointmentService();
        this.userContext = null;
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

    public MedicalRecordService getMedicalRecordService() {
        return medicalRecordService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

//    public UserContext getUserContext() {
//        return userContext;
//    }

}
