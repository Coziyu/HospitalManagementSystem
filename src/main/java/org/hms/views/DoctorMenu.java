package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.services.medicalrecord.MedicalRecord;
import org.hms.entities.Colour;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;

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
            System.out.println(Colour.RED + "Access Denied: Doctor privileges required." + Colour.RESET);
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Doctor Menu ===" + Colour.RESET);
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
                    case 6 -> handleViewUpcomingAppointments();
                    case 7 -> handleRecordAppointmentOutcome();
                    case 8 -> {
                        logDoctorAction("Logged out");
                        app.getAuthenticationService().logout();
                        app.setCurrentMenu(new AuthenticationMenu(app));
                        return;
                    }
                    default -> System.out.println(Colour.RED + "Invalid option. Please try again." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private void handleAccessPatientRecords() {
        System.out.println("\n" + Colour.BLUE + "=== Access Patient Records ===" + Colour.RESET);
        System.out.println("Accessing as: Dr. " + userContext.getName());
        System.out.print("Enter patient ID: ");

        try {
            int patientId = Integer.parseInt(scanner.nextLine());

            if (!canAccessPatientRecords(patientId)) {
                System.out.println(Colour.RED + "Access denied: Patient not assigned to you." + Colour.RESET);
                logDoctorAction("Attempted unauthorized access to patient records: " + patientId);
                return;
            }

            //TODO: For Elijah to refactor this part. Since his method was refactored to use Optional<>
            // Also, is this meant to be `records` or `record` singular? If it's singular, consider
            // somemthing like this line below:
            // Optional<MedicalRecord> record = app.getMedicalRecordService().getMedicalRecord();
            // If it's all the records, then you have to implement the method / declare the method in
            // the DataInterface

            // Display medical record information
            System.out.println("\nPatient Medical Record:");
            System.out.println("- Personal Information:");
            System.out.println("  • Name: [Patient Name]");
            System.out.println("  • Date of Birth: [DOB]");
            System.out.println("  • Gender: [Gender]");
            System.out.println("- Medical History:");
            System.out.println("  • Past Diagnoses");
            System.out.println("  • Treatment Plans");
            System.out.println("  • Prescribed Medications");

            String records = "placeholder";
            System.out.println(records);

            logDoctorAction("Accessed medical records for patient: " + patientId);
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid patient ID format." + Colour.RESET);
        }
    }

    private void handleUpdateMedicalRecords() {
        System.out.println("\n" + Colour.BLUE + "=== Update Medical Records ===" + Colour.RESET);
        System.out.println("Updating as: Dr. " + userContext.getName());
        System.out.print("Enter patient ID: ");

        try {
            int patientId = Integer.parseInt(scanner.nextLine());

            if (!canAccessPatientRecords(patientId)) {
                System.out.println(Colour.RED + "Access denied: Patient not assigned to you." + Colour.RESET);
                logDoctorAction("Attempted unauthorized update to patient records: " + patientId);
                return;
            }

            System.out.println("\nUpdate Options:");
            System.out.println("1. Add New Diagnosis");
            System.out.println("2. Add Prescription");
            System.out.println("3. Add Treatment Plan");
            System.out.println("4. Return to Main Menu");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter diagnosis: ");
                    String diagnosis = scanner.nextLine();
                    // TODO: Save diagnosis
                }
                case 2 -> {
                    System.out.print("Enter medication name: ");
                    String medication = scanner.nextLine();
                    System.out.print("Enter dosage: ");
                    String dosage = scanner.nextLine();
                    // TODO: Save prescription
                }
                case 3 -> {
                    System.out.print("Enter treatment plan: ");
                    String plan = scanner.nextLine();
                    // TODO: Save treatment plan
                }
                case 4 -> { return; }
                default -> System.out.println(Colour.RED + "Invalid option." + Colour.RESET);
            }

            logDoctorAction("Updated medical records for patient: " + patientId);
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input format." + Colour.RESET);
        }
    }

    private void handleViewSchedule() {
        System.out.println("\n" + Colour.BLUE + "=== Weekly Schedule ===" + Colour.RESET);
        System.out.println("Schedule for: Dr. " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        // TODO: Implement this to show actual schedule
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            System.out.println("\n" + date.getDayOfWeek() + " - " + date);
            System.out.println("Morning: [Appointments/Available]");
            System.out.println("Afternoon: [Appointments/Available]");
        }

        logDoctorAction("Viewed weekly schedule");
    }

    private void handleSetAppointmentAvailability() {
        System.out.println("\n" + Colour.BLUE + "=== Set Appointment Availability ===" + Colour.RESET);
        System.out.println("1. Set availability for specific date");
        System.out.println("2. Set recurring availability");
        System.out.println("3. Return to main menu");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter date (DD/MM/YYYY): ");
                    String date = scanner.nextLine();
                    // TODO: Implement date availability setting
                }
                case 2 -> {
                    System.out.println("Select day of week:");
                    // TODO: Implement recurring availability setting
                }
                case 3 -> { return; }
                default -> System.out.println(Colour.RED + "Invalid option." + Colour.RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input format." + Colour.RESET);
        }
    }

    private void handleAppointmentRequests() {
        System.out.println("\n" + Colour.BLUE + "=== Handle Appointment Requests ===" + Colour.RESET);
        // TODO: Show pending appointment requests
        System.out.println("Pending Requests:");
        System.out.println("1. Patient P001 - 15/11/2024 09:00");
        System.out.println("2. Patient P002 - 15/11/2024 10:00");

        System.out.print("Enter request number to handle (0 to return): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0) {
                System.out.println("1. Accept");
                System.out.println("2. Decline");
                System.out.print("Choice: ");
                int action = Integer.parseInt(scanner.nextLine());
                // TODO: Implement accept/decline logic
            }
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input format." + Colour.RESET);
        }
    }

    private void handleViewUpcomingAppointments() {
        System.out.println("\n" + Colour.BLUE + "=== Upcoming Appointments ===" + Colour.RESET);
        System.out.println("Doctor: Dr. " + userContext.getName());

        // TODO: Implement actual appointment retrieval
        System.out.println("\nToday's Appointments:");
        System.out.println("09:00 - Patient P001 (Consultation)");
        System.out.println("10:00 - Patient P002 (Follow-up)");

        System.out.println("\nUpcoming Appointments:");
        System.out.println("Tomorrow:");
        System.out.println("09:30 - Patient P003 (X-ray)");
        System.out.println("11:00 - Patient P004 (Consultation)");

        logDoctorAction("Viewed upcoming appointments");
    }

    private void handleRecordAppointmentOutcome() {
        System.out.println("\n" + Colour.BLUE + "=== Record Appointment Outcome ===" + Colour.RESET);
        System.out.print("Enter Patient ID: ");

        try {
            String patientId = scanner.nextLine();

            System.out.println("\nAppointment Details:");
            System.out.println("Date: " + LocalDate.now());

            System.out.println("\nSelect Service Type:");
            System.out.println("1. Consultation");
            System.out.println("2. X-ray");
            System.out.println("3. Blood Test");
            System.out.println("4. Other");
            System.out.print("Choice: ");
            int serviceType = Integer.parseInt(scanner.nextLine());

            System.out.println("\nPrescribed Medications:");
            List<String> medications = List.of(); // Placeholder for medication list
            while (true) {
                System.out.print("Add medication (or press Enter to finish): ");
                String med = scanner.nextLine();
                if (med.isEmpty()) break;
                // TODO: Add medication to list with pending status
            }

            System.out.println("\nConsultation Notes:");
            String notes = scanner.nextLine();

            // TODO: Save appointment outcome
            logDoctorAction("Recorded outcome for patient: " + patientId);
            System.out.println(Colour.GREEN + "Appointment outcome recorded successfully!" + Colour.RESET);

        } catch (Exception e) {
            System.out.println(Colour.RED + "Error recording appointment outcome: " + e.getMessage() + Colour.RESET);
        }
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