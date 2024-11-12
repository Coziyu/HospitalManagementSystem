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
            System.out.println("1. View Medical Records");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. View Upcoming Appointments");
            System.out.println("8. View Past Appointment Outcomes");
            System.out.println("9. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleViewMedicalRecord();
                    case 2 -> handleUpdateInformation();
                    case 3 -> handleViewAvailableAppointmentSlot();
                    case 4 -> handleScheduleAppointment();
                    case 5 -> handleRescheduleAppointment();
                    case 6 -> handleCancelAppointment();
                    case 7 -> handleViewUpcomingAppointments();
                    case 8 -> handleViewPastAppointmentOutcome();
                    case 9 -> {
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

    // TODO: For implementation
    private void handleViewPastAppointmentOutcome() {
        System.out.println("Feature coming soon...");
    }

    private void handleCancelAppointment() {
        System.out.println("Feature coming soon...");
    }

    private void handleRescheduleAppointment() {
        System.out.println("Feature coming soon...");
    }

    private void handleViewAvailableAppointmentSlot() {
        System.out.println("Feature coming soon...");
    }

    // TODO: For implementation
    private void handleScheduleAppointment() {
        System.out.println("Feature coming soon...");
        System.out.println("\n=== Schedule Appointment ===");
        System.out.print("Enter date (YYYYMMDD): ");
        try {
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);

            if (date.isBefore(LocalDate.now())) {
                System.out.println("Cannot schedule appointments in the past.");
                return;
            }
            // TODO: scheduleAppointment requires doctorID, patientID, and timeSlot too.
            // app.getAppointmentService().scheduleAppointment(Integer.parseInt(dateStr));
            System.out.println("Appointment scheduled for: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYYMMDD.");
        }
    }

    private void handleViewMedicalRecord() {
        System.out.println("\n=== Medical Records ===");
        Integer patientID = patientContext.getHospitalID();

        //TODO: For Elijah to refactor this part. Since his method was refactored to use Optional<>
        // Also, is this meant to be `records` or `record` singular? If it's singular, consider
        // somemthing like this line below:
        // Optional<MedicalRecord> record = app.getMedicalRecordService().getMedicalRecord();
        // If it's all the records, then you have to implement the method / declare the method in
        // the DataInterface
//        String medicalRecords = app.getMedicalRecordService().getMedicalRecords(patientID);
        String medicalRecords = "placeholder";

        System.out.println(medicalRecords);
    }

    // TODO: For Yingjie to implement
    private void handleViewUpcomingAppointments() {
        System.out.println("\n=== Upcoming Appointments ===");
        // Implementation would use AppointmentService to get appointments
        System.out.println("Feature coming soon...");
    }

    // TODO: For Amos to implement
    private void handleUpdateInformation() {
        System.out.println("\n=== Update Personal Information ===");
        // Implementation would allow updating contact info, etc.
        System.out.println("Feature coming soon...");
    }
    // TODO: Decide if deprecated
    private void handleViewPrescriptions() {
        System.out.println("\n=== Current Prescriptions ===");
        // Implementation would use a PrescriptionService to get prescriptions
        System.out.println("Feature coming soon...");
    }
}