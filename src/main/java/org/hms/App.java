package org.hms;

import org.hms.entities.UserContext;
import org.hms.services.appointment.AppointmentService;
import org.hms.services.drugdispensary.DrugDispensaryService;
import org.hms.services.medicalrecord.MedicalRecordService;
import org.hms.services.storage.StorageService;
import org.hms.services.authentication.AuthenticationService;
import org.hms.views.AuthenticationMenu;
import org.hms.views.AbstractMenu;

public class App {
    private final StorageService storageService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final DrugDispensaryService drugDispensaryService;
    private final AuthenticationService authenticationService;

    private UserContext userContext;
    private AbstractMenu currentMenu;

    public App() {
        this.storageService = new StorageService();
        this.medicalRecordService = new MedicalRecordService(storageService);
        this.appointmentService = new AppointmentService(storageService);
        this.drugDispensaryService = new DrugDispensaryService(storageService);
        this.authenticationService = new AuthenticationService();
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

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public DrugDispensaryService getDrugDispensaryService(){
        return drugDispensaryService;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}