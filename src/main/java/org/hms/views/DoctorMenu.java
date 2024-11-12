package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.services.medicalrecord.MedicalRecord;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class DoctorMenu extends AbstractMainMenu {
    private final Scanner scanner;
    private final UserContext userContext;

    public DoctorMenu(App app) {
        this.app = app;
        this.userContext = app.getUserContext();
        this.scanner = new Scanner(System.in);
        validateDoctorAccess();
    }

    private void validateDoctorAccess() {
        if (userContext == null || userContext.getUserType() != UserRole.DOCTOR) {
            System.out.println("Access Denied: Doctor privileges required.");
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n=== Doctor Menu ===");
            System.out.println("Doctor: Dr. " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("Date: " + LocalDate.now());
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Appointment Slot Availability");
            System.out.println("5. Handle Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleAccessPatientRecords();
                    case 2 -> handleUpdateMedicalRecords();
                    case 3 -> handleViewSchedule();
                    case 4 -> handleSetAppointmentAvailability();
                    case 5 -> handleAppointmentRequests();
                    case 6 -> handleViewAppointments();
                    case 7 -> handleRecordAppointmentOutcome();
                    case 8 -> {
                        logDoctorAction("Logged out");
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

    private void handleRecordAppointmentOutcome() {
        // TODO: For Yingjie to implement
        System.out.println("Feature coming soon");
    }

    private void handleAppointmentRequests() {
        // TODO: For Yingjie to implement
        System.out.println("Feature coming soon");
    }

    private void handleSetAppointmentAvailability() {
        // TODO: For Yingjie to implement
        System.out.println("Feature coming soon");
    }

    private void handleViewAppointments() {
        System.out.println("\n=== Today's Appointments ===");
        System.out.println("Doctor: Dr. " + userContext.getName());
        logDoctorAction("Viewed today's appointments");
        // Implementation would show today's appointments
        System.out.println("Feature coming soon...");
    }

    private void handleAccessPatientRecords() {
        System.out.println("\n=== Access Patient Records ===");
        System.out.println("Accessing as: Dr. " + userContext.getName());
        System.out.print("Enter patient ID: ");

        try {
            int patientId = Integer.parseInt(scanner.nextLine());

            // Verify doctor's access rights for this patient
            if (!canAccessPatientRecords(patientId)) {
                System.out.println("Access denied: Patient not assigned to you.");
                logDoctorAction("Attempted unauthorized access to patient records: " + patientId);
                return;
            }

            //TODO: For Elijah to refactor this part. Since his method was refactored to use Optional<>
            // Also, is this meant to be `records` or `record` singular? If it's singular, consider
            // somemthing like this line below:
            // Optional<MedicalRecord> record = app.getMedicalRecordService().getMedicalRecord();
            // If it's all the records, then you have to implement the method / declare the method in
            // the DataInterface

            // String records = app.getMedicalRecordService().getMedicalRecords(patientId);
            String records = "placeholder";

            System.out.println(records);
            logDoctorAction("Accessed medical records for patient: " + patientId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid patient ID format.");
        }
    }

    private void handleUpdateMedicalRecords() {
        // TODO: Implement this.
        System.out.println("\n=== Update Medical Records ===");
        System.out.println("Updating as: Dr. " + userContext.getName());
        System.out.print("Enter patient ID: ");

        try {
            int patientId = Integer.parseInt(scanner.nextLine());

            if (!canAccessPatientRecords(patientId)) {
                System.out.println("Access denied: Patient not assigned to you.");
                logDoctorAction("Attempted unauthorized update to patient records: " + patientId);
                return;
            }

            System.out.print("Enter medical notes: ");
            String notes = scanner.nextLine();

            logDoctorAction("Updated medical records for patient: " + patientId);
            System.out.println("Feature coming soon...");
        } catch (NumberFormatException e) {
            System.out.println("Invalid patient ID format.");
        }
    }

    private void handleViewSchedule() {
        // TODO: Implement this.
        System.out.println("\n=== Weekly Schedule ===");
        System.out.println("Schedule for: Dr. " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());
        logDoctorAction("Viewed weekly schedule");
        // Implementation would show doctor's schedule
        System.out.println("Feature coming soon...");
    }

    private boolean canAccessPatientRecords(int patientId) {
        // TODO: For implementation
        // In a real implementation, this would check if the patient is assigned to this doctor
        // For now, returning true for demonstration
        return true;
    }

    private void logDoctorAction(String action) {
        String logMessage = String.format("Doctor Action - Dr. %s (Hospital: %d) - %s",
                userContext.getName(),
                userContext.getHospitalID(),
                action
        );
        System.out.println("LOG: " + LocalDateTime.now() + " - " + logMessage);
    }
}