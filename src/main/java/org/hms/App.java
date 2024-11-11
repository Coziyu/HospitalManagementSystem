package org.hms;

import org.hms.entities.UserContext;
import org.hms.services.appointment.AppointmentService;
import org.hms.services.medicalrecord.MedicalRecordService;
import org.hms.services.storage.StorageService;
import org.hms.services.authentication.AuthenticationService;
import org.hms.views.AuthenticationMenu;
import org.hms.views.AbstractMenu;
import org.hms.entities.UserRole;

public class App {
    private final StorageService storageService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final AuthenticationService authenticationService;
    private UserContext userContext;
    private AbstractMenu currentMenu;

    public App() {
        this.storageService = new StorageService();
        this.medicalRecordService = new MedicalRecordService(storageService);
        this.appointmentService = new AppointmentService(storageService);
        this.authenticationService = new AuthenticationService();
        this.userContext = null;

        // Initialize test users
        initializeTestUsers();
    }

    private void initializeTestUsers() {
        authenticationService.addUser("DOC001", UserRole.DOCTOR);
        authenticationService.addUser("PAT001", UserRole.PATIENT);
        authenticationService.addUser("PHARM001", UserRole.PHARMACIST);
        authenticationService.addUser("ADMIN001", UserRole.ADMINISTRATOR);
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

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}