package org.hms.views;

import org.hms.App;


import java.util.Scanner;

// Consider using reflection to autogenerate options
public class PatientMenu extends AbstractMainMenu {

    public PatientMenu(App app) {
        this.app = app;
    }

    @Override
    public void displayAndExecute() {
        // TOOD
    }

    // TODO: THIS IS AN EXAMPLE. CHANGE IT.
    private void handleScheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter date for appointment (e.g., 20231031): ");
        int date = scanner.nextInt();
        app.getAppointmentService().scheduleAppointment(date);
        System.out.println("Appointment scheduled on date: " + date);
    }

    private void handleViewMedicalRecord() {
        Integer patientID = app.getUserContext().getHospitalID();
        String medicalRecords = app.getMedicalRecordService().getMedicalRecords(patientID);
        System.out.println("Medical Records for " + patientID + ": " + medicalRecords);
    }
}
