package org.hms;

import org.hms.entities.UserContext;
import org.hms.services.appointment.AppointmentService;
import org.hms.services.drugdispensary.DrugDispensaryService;
import org.hms.services.medicalrecord.MedicalRecordService;
import org.hms.services.staffmanagement.Staff;
import org.hms.services.staffmanagement.StaffManagementService;
import org.hms.services.storage.StorageService;
import org.hms.services.authentication.AuthenticationService;
import org.hms.views.AuthenticationMenu;
import org.hms.views.AbstractMenu;

/**
 * Main application class for the Hospital Management System (HMS).
 * This class initializes and runs the application, managing various services and user interactions.
 */
public class App {
    private final StorageService storageService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final DrugDispensaryService drugDispensaryService;
    private final AuthenticationService authenticationService;
    private final StaffManagementService staffManagementService;

    private UserContext userContext;
    private AbstractMenu currentMenu;

    /**
     * Constructor for App
     */
    public App() {
        this.storageService = new StorageService();
        this.medicalRecordService = new MedicalRecordService(storageService);
        this.appointmentService = new AppointmentService(storageService);
        this.drugDispensaryService = new DrugDispensaryService(storageService);
        this.authenticationService = new AuthenticationService();
        this.staffManagementService = new StaffManagementService(storageService);
        this.userContext = null;
    }

    /**
     * Get the current menu
     *
     * @return the current menu
     */
    public void setCurrentMenu(AbstractMenu menu) {
        this.currentMenu = menu;
    }

    /**
     * Run the application
     */
    public void run() {
        while (currentMenu != null) {
            currentMenu.displayAndExecute();
        }
    }

    /**
     * Initialise the application
     */
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

    public DrugDispensaryService getDrugDispensaryService() {
        return drugDispensaryService;
    }

    public StaffManagementService getStaffManagementService() {
        return staffManagementService;
    }

    public UserContext getUserContext() {
        return userContext;
    }

    public void setUserContext(UserContext userContext) {
        this.userContext = userContext;
    }
}