package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.services.appointment.AppointmentOutcome;
import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.logging.AuditLogger;
import org.hms.services.medicalrecord.MedicalRecord;

import org.hms.entities.Colour;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * The DoctorMenu class provides a menu interface for doctors to perform various actions
 * related to appointment management, medical record handling, and schedule viewing.
 * It extends the AbstractMainMenu class to leverage common menu functionalities.
 */
public class DoctorMenu extends AbstractMainMenu {
    /**
     * Scanner instance for capturing user input within the DoctorMenu.
     * <p>
     * This Scanner object is utilized to read various inputs from the console,
     * allowing the doctor to interact with different menu options and execute commands accordingly.
     */
    private final Scanner scanner;
    /**
     * The userContext field holds the context data of the currently logged-in user,
     * including their name, role, and associated hospital ID.
     * It is used for validating and authorizing user actions within the DoctorMenu.
     */
    private final UserContext userContext;

    /**
     * Constructs a DoctorMenu object with the specified App instance.
     * This constructor initializes the DoctorMenu by setting the application context,
     * user context, and input scanner. Additionally, it validates that the user has
     * the appropriate doctor access
     */
    public DoctorMenu(App app) {
        this.app = app;
        this.userContext = app.getUserContext();
        this.scanner = new Scanner(System.in);
        validateDoctorAccess();
    }

    /**
     * Validates if the current user has the necessary doctor privileges to access specific functionalities
     * within the application. If the user context is null or the user role is not a doctor, access is denied,
     * a message is printed, and the application redirects to the authentication menu.
     */
    private void validateDoctorAccess() {
        if (userContext == null || userContext.getUserType() != UserRole.DOCTOR) {
            System.out.println(Colour.RED + "Access Denied: Doctor privileges required." + Colour.RESET);
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    /**
     * Displays the doctor menu and processes user selections.
     * <p>
     * This method continuously displays the doctor menu with various options including:
     * viewing and updating patient medical records, managing appointment slots and requests,
     * viewing personal schedules and upcoming appointments, recording appointment outcomes, and logging out.
     * <p>
     * The method reads user input and executes the corresponding actions based on the selected option.
     * If the user chooses to log out, the method logs the action, calls the logout service, and
     * navigates back to the authentication
     */
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

    /**
     * Handles the recording of the outcome for a specific appointment. This method:
     * - Displays all confirmed appointments for the currently logged-in doctor.
     * - Prompts the doctor to enter the Appointment ID.
     * - Allows the doctor to prescribe medications and checks the current drug inventory.
     * - Prompts the doctor to enter various details including diagnosis, treatment plan, and consultation notes.
     * - Adds a new medical entry into the patient's record.
     * - Comple
     */
    private void handleRecordAppointmentOutcome() {
        // TODO: For nicholas to check

        if (!app.getAppointmentService().displayAllAppointmentsForDoctor(userContext.getHospitalID())) {
            return;
        }
        ;

        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();


        ArrayList<DrugDispenseRequest> prescribedMedication = app.getAppointmentService().createNewArrayOfDrugDispenseRequest();

        int medicationCount;
        while (true) {
            System.out.print("Enter number of medications to prescribe: ");
            medicationCount = Integer.parseInt(scanner.nextLine());
            if (medicationCount > 0) {
                break; // Valid medication count
            } else {
                System.out.println(Colour.RED + "Number of medications must be greater than 0. Please re-enter." + Colour.RESET);
            }
        }

        //System.out.print("Enter number of medications to prescribe: ");
        //int medicationCount = Integer.parseInt(scanner.nextLine());

        // Show list of drugs in the pharmacy
        System.out.println(Colour.GREEN + " == Available Drugs == " + Colour.RESET);
        System.out.println(app.getDrugDispensaryService().getDrugInventoryAsString());

        for (int i = 1; i <= medicationCount; i++) {
            System.out.print("Enter name of drug " + i + ": ");
            String drugName = scanner.nextLine();

            // Check if the drug is available in the pharmacy
            if (!app.getDrugDispensaryService().doesDrugExist(drugName)) {
                System.out.println(Colour.RED + "Drug not available in pharmacy." + Colour.RESET);
                System.out.println("Do you want to add a record for this drug? Note that drug will only be in stock once the administrator approves it. (y/n)");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("y")) {
                    app.getDrugDispensaryService().addNewDrug(drugName, 0, 0);
                } else {
                    // Return to the start of the loop
                    i--;
                    continue;
                }
            }

            int quantity;
            while (true) {
                System.out.print("Enter quantity for " + drugName + ": ");
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity > 0) {
                    break; // Valid quantity
                } else {
                    System.out.println(Colour.RED + "Quantity must be greater than 0. Please re-enter." + Colour.RESET);
                }
            }

            //System.out.print("Enter quantity for " + drugName + ": ");
            //int quantity = Integer.parseInt(scanner.nextLine());

            // Use the addDrugDispenseRequest method to add each DrugDispenseRequest to the list
            app.getAppointmentService().addDrugDispenseRequest(prescribedMedication, drugName, quantity);
        }


        //System.out.print("Enter Patient ID: ");
        String patientID = app.getAppointmentService().getPatienIDbyAppointmentID(appointmentID);

        System.out.print("Enter Type of Appointment: ");
        String typeOfAppointment = scanner.nextLine();

        System.out.println("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();

        System.out.println("Enter Treatment Plan: ");
        String treatmentPlan = scanner.nextLine();

        System.out.print("Enter Consultation Notes (use ',' and '/' if needed): ");
        String consultationNotes = scanner.nextLine();

        if (!app.getMedicalRecordService().addMedicalEntry(app.getUserContext(), patientID, diagnosis, treatmentPlan, consultationNotes)) {
            System.out.println(Colour.RED + "Failed to add medical entry" + Colour.RESET);
        }

        // Use createNewAppointmentOutcome to create an AppointmentOutcome object
        AppointmentOutcome newOutcome = app.getAppointmentService().createNewAppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
        app.getAppointmentService().completeAnAppointment(appointmentID, app.getUserContext().getHospitalID());

        //System.out.println("AppointmentOutcome has been written to the CSV file.");
        System.out.println("Appointment is completed and outcome has been recorded");
    }

    /**
     * Handles appointment requests by retrieving the doctor's ID from the user context
     * and checking for pending requests. If there are any pending requests, it prompts
     * the doctor to input the appointment ID to manage the request.
     */
    private void handleAppointmentRequests() {

        String doctorID = (app.getUserContext().getHospitalID());
        boolean request = app.getAppointmentService().viewRequest(doctorID);
        if (request == true) {
            System.out.println("key in appointment ID");
            int appointmentID = Integer.parseInt(scanner.nextLine());

            app.getAppointmentService().manageAppointmentRequests(appointmentID, doctorID);
        }
    }

    /**
     * Handles setting the appointment availability for a doctor.
     * The method allows the doctor to set their schedule as either available or unavailable for a specific date and time slot.
     * The doctor can input their desired availability and the system will check if a schedule exists for that date.
     * If the schedule does not exist, it will create a new one and then update the schedule with
     */
    private void handleSetAppointmentAvailability() {
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

            /******this part for check does schedule alr exists in the folder, if no, initialize a new date******/
            if (!app.getAppointmentService().checkExistingSchedule(date)) {
                app.getAppointmentService().createNewSchedule(date);
            }

            /******************/

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

    /**
     * Displays the upcoming appointments for the doctor currently logged into the system.
     * <p>
     * This method prints the doctor's name and logs the action of viewing upcoming appointments.
     * It then retrieves the doctor's ID from the user context and uses the appointment service to
     * display all confirmed appointments for the doctor.
     */
    private void handleViewAppointments() {
        System.out.println("\n=== Upcoming Appointments ===");
        System.out.println("Doctor: Dr. " + userContext.getName());
        logDoctorAction("Viewed upcoming appointments");
        // Implementation would show today's appointments

        String doctorID = (app.getUserContext().getHospitalID());
        //LocalDate currentDate = LocalDate.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //String formattedDate = currentDate.format(formatter);
        //app.getAppointmentService().displayAppointmentsForDoctor(formattedDate, doctorID);
        app.getAppointmentService().displayAllAppointmentsForDoctor(doctorID);

    }

    /**
     * Handles the process of accessing patient records for a doctor.
     * <p>
     * This method performs the following actions:
     * 1. Displays the header indicating that the access patient records section is being accessed.
     * 2. Retrieves and displays the doctor's name.
     * 3. Retrieves and displays the list of patients under the doctor's care.
     * 4. Prompts the doctor to enter
     */
    private void handleAccessPatientRecords() {
        System.out.println("\n" + Colour.BLUE + "=== Access Patient Records ===" + Colour.RESET);
        System.out.println("Accessing as: Dr. " + userContext.getName());

        String doctorID = app.getUserContext().getHospitalID();

        // Display a list of patients under the doctor's care
        System.out.println("Patients under your care:");
        System.out.println(app.getMedicalRecordService().getPatientParticularsTreatedByDoctor(doctorID));

        try {
            //TODO: Nich: consider change to ID selection
            System.out.print("Enter PatientID to view: ");
            String patientID = scanner.nextLine();

            // Ensure that the patient exists
            if (!app.getMedicalRecordService().patientExists(patientID)) {
                System.out.println(Colour.RED + "Patient not found." + Colour.RESET);
                logDoctorAction("Attempted access to non-existent patient: " + patientID);
                return;
            }

            // Verify doctor's access rights for this patient
            if (!app.getMedicalRecordService().isPatientTreatedByDoctor(patientID, doctorID)) {
                System.out.println(Colour.RED + "Access denied: Patient not assigned to you." + Colour.RESET);
                logDoctorAction("Unauthorised access to patient records: " + patientID);
                return;
            }

            // Display patient's personal particulars
            System.out.println("\n" + Colour.GREEN + " == Patient Personal Particulars == " + Colour.RESET);
            System.out.println(app.getMedicalRecordService().getPatientPersonalParticulars(patientID));

            // Display patient's Medical Records
            System.out.println("\n" + Colour.GREEN + " == Patient Medical Records == " + Colour.RESET);
            System.out.println(app.getMedicalRecordService().getPatientMedicalRecord(patientID));

            logDoctorAction("Accessed medical records for patient: " + patientID);

        } catch (Exception e) {
            System.out.println(Colour.RED + "An error occurred." + Colour.RESET);
        }
    }

    /**
     * Handles the process of updating medical records for patients under the doctor's care.
     * This method performs the following steps:
     * - Displays a list of patients under the doctor's care.
     * - Prompts the doctor to enter the PatientID of the patient whose records need updating.
     * - Validates whether the patient exists and whether they are assigned to the current doctor
     */
    private void handleUpdateMedicalRecords() {
        System.out.println("\n" + Colour.BLUE + "=== Update Medical Records ===" + Colour.RESET);
        System.out.println("Updating as: Dr. " + userContext.getName());

        String doctorID = app.getUserContext().getHospitalID();

        // Display a list of patients under the doctor's care
        System.out.println("Patients under your care:");
        System.out.println(app.getMedicalRecordService().getPatientParticularsTreatedByDoctor(doctorID));

        try {
            System.out.print("Enter PatientID to update: ");

            String patientID = scanner.nextLine();

            // Ensure that the patient exists
            if (!app.getMedicalRecordService().patientExists(patientID)) {
                System.out.println(Colour.RED + "Patient not found." + Colour.RESET);
                logDoctorAction("Attempted access to non-existent patient: " + patientID);
                return;
            }

            if (!app.getMedicalRecordService().isPatientTreatedByDoctor(patientID, userContext.getHospitalID())) {
                System.out.println(Colour.RED + "Access denied: Patient not assigned to you." + Colour.RESET);
                logDoctorAction("Attempted unauthorised update to patient records: " + patientID);
                return;
            }

            // Display Patient's Medical Records
            System.out.println("\n" + Colour.GREEN + " == Patient Medical Records == " + Colour.RESET);
            System.out.println(app.getMedicalRecordService().getPatientMedicalRecord(doctorID, patientID));

            List<Integer> validEntryNumbers = app.getMedicalRecordService().getPatientMedicalRecordEntryIDs(doctorID, patientID);


            // Prompt select MedicalEntry to update from the MedicalRecord
            int entryID = -1;
            while (!validEntryNumbers.contains(entryID)) {
                if (entryID != -1) {
                    System.out.println(Colour.RED + "Invalid entryID." + Colour.RESET);
                    logDoctorAction("Attempted access to non-existent medical record entry: " + entryID);
                }
                System.out.print("Enter entryID to update: (-1 to cancel): ");
                entryID = scanner.nextInt();
                scanner.nextLine();

                if (entryID == -1) {
                    return;
                }
            }

            // Display the MedicalEntry to be updated
            System.out.println("\n" + Colour.GREEN + " == Medical Entry to Update == " + Colour.RESET);
            System.out.println(app.getMedicalRecordService().getMedicalRecordEntry(entryID));

            // Update the MedicalEntry
            // Select to change Diagnosis, Treatment Plan, or Consultation Notes
            System.out.println("\n" + Colour.GREEN + " == Update Medical Entry == " + Colour.RESET);
            System.out.println("Select an option:");
            System.out.println("1. Update Diagnosis");
            System.out.println("2. Update Treatment Plan");
            System.out.println("3. Update Consultation Notes");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Diagnosis: ");
                    String diagnosis = scanner.nextLine();
                    app.getMedicalRecordService().updateDiagnosis(entryID, diagnosis);
                    break;
                case 2:
                    System.out.print("Enter Treatment Plan: ");
                    String treatmentPlan = scanner.nextLine();
                    app.getMedicalRecordService().updateTreatmentPlan(entryID, treatmentPlan);
                    break;
                case 3:
                    System.out.print("Enter Consultation Notes: ");
                    String consultationNotes = scanner.nextLine();
                    app.getMedicalRecordService().updateConsultationNotes(entryID, consultationNotes);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            // Display the updated MedicalEntry
            System.out.println("\n" + Colour.GREEN + " == Updated Medical Entry == " + Colour.RESET);
            System.out.println(app.getMedicalRecordService().getMedicalRecordEntry(entryID));

            logDoctorAction("Updated medical records for patient: " + patientID);
        } catch (Exception e) {
            System.out.println(Colour.RED + "An error occurred." + Colour.RESET);
        }
    }

    /**
     * Handles the action for viewing a doctor's daily schedule.
     * <p>
     * This method prompts the user to input a date in "YYYYMMDD" format. It then checks
     * if a schedule for that date already exists. If not, it initializes a new schedule.
     * The method proceeds to display the doctor's schedule for the specified date
     * and logs the action of viewing the schedule. The schedule includes time slots
     */
    private void handleViewSchedule() {
        // TODO: Implement this.
        String doctorID = app.getUserContext().getHospitalID();
        System.out.print("Enter date (YYYYMMDD): ");

        String dateStr = scanner.nextLine();
        /******this part for check does schedule alr exists in the folder, if no, initialize a new date******/
        if (!app.getAppointmentService().checkExistingSchedule(dateStr)) {
            app.getAppointmentService().createNewSchedule(dateStr);
        }

        /******************/

        System.out.println("\n=== Daily Schedule ===");
        System.out.println("Schedule for: Dr. " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());
        logDoctorAction("Viewed daily schedule");
        // Implementation would show doctor's schedule
        app.getAppointmentService().viewDoctorDailySchedule(doctorID, dateStr);
    }

    /**
     * Logs an action performed by a doctor in the hospital management system.
     * The action details are recorded including the doctor's name, role, hospital ID, and a description of the action.
     *
     * @param action a description of the action performed by the doctor
     */
    private void logDoctorAction(String action) {
        AuditLogger.logAction(
                userContext.getName(),
                "DOCTOR",
                String.valueOf(userContext.getHospitalID()),
                action
        );
    }
}