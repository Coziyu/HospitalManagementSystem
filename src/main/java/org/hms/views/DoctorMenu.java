package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.services.appointment.AppointmentOutcome;
import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.medicalrecord.MedicalRecord;
import org.hms.entities.Colour;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                    case 6 -> handleViewAppointments();
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
        }
    }

    private void handleRecordAppointmentOutcome() {
        // TODO: For Yingjie to implement

        ArrayList<DrugDispenseRequest> prescribedMedication = app.getAppointmentService().createNewArrayOfDrugDispenseRequest();

        System.out.print("Enter number of medications to prescribe: ");
        int medicationCount = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= medicationCount; i++) {
            System.out.print("Enter name of drug " + i + ": ");
            String drugName = scanner.nextLine();

            System.out.print("Enter quantity for " + drugName + ": ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Use the addDrugDispenseRequest method to add each DrugDispenseRequest to the list
            app.getAppointmentService().addDrugDispenseRequest(prescribedMedication, drugName, quantity);
        }

        // Step 3: Collect data for the AppointmentOutcome fields
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();

        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();

        System.out.print("Enter Type of Appointment: ");
        String typeOfAppointment = scanner.nextLine();

        System.out.print("Enter Consultation Notes (use ',' and '/' if needed): ");
        String consultationNotes = scanner.nextLine();

        // Use createNewAppointmentOutcome to create an AppointmentOutcome object
        AppointmentOutcome newOutcome = app.getAppointmentService().createNewAppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);

        System.out.println("AppointmentOutcome has been written to the CSV file.");
    }

    private void handleAppointmentRequests() {
        // TODO: For Yingjie to implement
        System.out.println("Feature coming soon");

        String doctorID = (app.getUserContext().getHospitalID());
        //doctorID = "D1001" ;  //remove this line after real doctor ID have appointments
        boolean request = app.getAppointmentService().viewRequest(doctorID);
        if(request == true){
        System.out.println("key in appointment ID");
        int appointmentID = Integer.parseInt(scanner.nextLine());

        app.getAppointmentService().manageAppointmentRequests(appointmentID, doctorID);
        }
    }

    private void handleSetAppointmentAvailability() {
        // TODO: For Yingjie to implement
        String doctorID = (app.getUserContext().getHospitalID());

        System.out.println("Do you want to set the schedule as:");
        System.out.println("1. Available");
        System.out.println("2. Unavailable");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());

            if (choice != 1 && choice != 2) {
                System.out.println("Invalid choice. Please select 1 or 2.");
                return;
            }

            System.out.print("Enter the date (YYYYMMDD): ");
            String date = scanner.nextLine();

            System.out.print("Enter the time slot (e.g., 12:00): ");
            String timeSlot = scanner.nextLine();

            if (choice == 1) {
                app.getAppointmentService().setDoctorSchedule(doctorID, date, timeSlot);
                System.out.println("Schedule set to 'Available' successfully.");
            } else if (choice == 2) {
                app.getAppointmentService().cancelDoctorSchedule(doctorID, date, timeSlot);
                System.out.println("Schedule set to 'Unavailable' successfully.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric value for the choice.");
        }


    }

    private void handleViewAppointments() {
        System.out.println("\n=== Today's Appointments ===");
        System.out.println("Doctor: Dr. " + userContext.getName());
        logDoctorAction("Viewed today's appointments");
        // Implementation would show today's appointments

        String doctorID = (app.getUserContext().getHospitalID());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);
        app.getAppointmentService().displayAppointmentsForDoctor(formattedDate, doctorID);

    }

    private void handleAccessPatientRecords() {
        System.out.println("\n" + Colour.BLUE + "=== Access Patient Records ===" + Colour.RESET);
        System.out.println("Accessing as: Dr. " + userContext.getName());
        System.out.print("Enter patient ID: ");

        try {
            int patientId = Integer.parseInt(scanner.nextLine());

            // Verify doctor's access rights for this patient
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

            // String records = app.getMedicalRecordService().getMedicalRecords(patientId);
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

            System.out.print("Enter medical notes: ");
            String notes = scanner.nextLine();

            logDoctorAction("Updated medical records for patient: " + patientId);
            System.out.println("Feature coming soon...");
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input format." + Colour.RESET);
        }
    }

    private void handleViewSchedule() {
        // TODO: Implement this.
        System.out.println("\n=== Daily Schedule ===");
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