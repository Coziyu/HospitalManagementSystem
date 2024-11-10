package org.hms.views;

import org.hms.App;
import org.hms.entities.PatientContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PatientMenu extends AbstractMainMenu {
    private final PatientContext patientContext;
    private final Scanner scanner;

    public PatientMenu(App app, PatientContext patientContext) {
        this.app = app;
        this.patientContext = patientContext;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n=== Patient Menu ===");
            System.out.println("1. Schedule Appointment");
            System.out.println("2. View Medical Records");
            System.out.println("3. View Upcoming Appointments");
            System.out.println("4. Update Personal Information");
            System.out.println("5. View Prescriptions");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleScheduleAppointment();
                    case 2 -> handleViewMedicalRecord();
                    case 3 -> handleViewAppointments();
                    case 4 -> handleUpdateInformation();
                    case 5 -> handleViewPrescriptions();
                    case 6 -> {
                        app.getAuthenticationService().logout();
                        app.setCurrentMenu(new AuthenticationMenu(app));
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void handleScheduleAppointment() {
        System.out.println("\n=== Schedule Appointment ===");
        System.out.print("Enter date (YYYYMMDD): ");
        try {
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);

            if (date.isBefore(LocalDate.now())) {
                System.out.println("Cannot schedule appointments in the past.");
                return;
            }

            app.getAppointmentService().scheduleAppointment(Integer.parseInt(dateStr));
            System.out.println("Appointment scheduled for: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYYMMDD.");
        }
    }

    private void handleViewMedicalRecord() {
        System.out.println("\n=== Medical Records ===");
        Integer patientID = patientContext.getHospitalID();
        String medicalRecords = app.getMedicalRecordService().getMedicalRecords(patientID);
        System.out.println(medicalRecords);
    }

    private void handleViewAppointments() {
        System.out.println("\n=== Upcoming Appointments ===");
        // Implementation would use AppointmentService to get appointments
        System.out.println("Feature coming soon...");
    }

    private void handleUpdateInformation() {
        System.out.println("\n=== Update Personal Information ===");
        // Implementation would allow updating contact info, etc.
        System.out.println("Feature coming soon...");
    }

    private void handleViewPrescriptions() {
        System.out.println("\n=== Current Prescriptions ===");
        // Implementation would use a PrescriptionService to get prescriptions
        System.out.println("Feature coming soon...");
    }
}