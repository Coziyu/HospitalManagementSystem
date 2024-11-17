package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.entities.Colour;
import org.hms.services.appointment.AppointmentOutcome;
import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.drugdispensary.DrugRequestStatus;
import org.hms.services.logging.AuditLogger;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * PharmacistMenu class is a view class that displays the main menu for the pharmacist user.
 * The pharmacist user can view appointment outcomes, update prescription status, view medication inventory,
 * and submit a drug replenishment request.
 * The class extends AbstractMainMenu and implements the displayAndExecute method to display the menu options.
 * The class also contains methods to handle the different menu options.
 *
 * @see AbstractMainMenu
 */
public class PharmacistMenu extends AbstractMainMenu {
    private final Scanner scanner;
    private final UserContext userContext;

    /**
     * Constructor for the PharmacistMenu class.
     * @param app
     */
    public PharmacistMenu(App app) {
        this.app = app;
        this.userContext = app.getUserContext();
        this.scanner = new Scanner(System.in);
        validatePharmacistAccess();
    }

    /**
     * Validates that the user has pharmacist access.
     */
    private void validatePharmacistAccess() {
        if (userContext == null || userContext.getUserType() != UserRole.PHARMACIST) {
            System.out.println(Colour.RED + "Access Denied: Pharmacist privileges required." + Colour.RESET);
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    /**
     * Displays the main menu for the pharmacist user.
     */
    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Pharmacist Menu ===" + Colour.RESET);
            System.out.println("Pharmacist: " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("Date: " + LocalDate.now());
            System.out.println("1. View Appointment Outcome Records");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleViewAppointmentOutcomes();
                    case 2 -> handleUpdatePrescriptionStatus();
                    case 3 -> handleViewMedicationInventory();
                    case 4 -> handleSubmitDrugReplenishRequest();
                    case 5 -> {
                        logPharmacistAction("Logged out");
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
     * Handles the viewing of appointment outcomes.
     * This method will display a list of patients with pending prescriptions.
     * The pharmacist can then select a patient to view the appointment outcomes and pending prescriptions.
     * The method will display the appointment outcomes and pending prescriptions for the selected patient.
     * The method will also log the action in the audit log.
     * @see AppointmentOutcome
     * @see DrugDispenseRequest
     * @see AuditLogger
     */
    private void handleViewAppointmentOutcomes() {
        System.out.println("\n" + Colour.BLUE + "=== View Appointment Outcome Records ===" + Colour.RESET);
        System.out.println("Recent patients with pending Prescriptions:");

        List<String> patientIDs = app.getAppointmentService().getPatientIDsWithPendingDrugRequest();
        // Select a patient
        if (patientIDs.isEmpty()) {
            System.out.println("No patients with pending prescriptions.");
            return;
        }

        String patientID = "";
        try {
            for (int i = 0; i < patientIDs.size(); i++) {
                System.out.println(i + ". " + patientIDs.get(i));
            }
            System.out.println("Select a patient (Enter the entry number):");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 0 || choice >= patientIDs.size()) {
                System.out.println(Colour.RED + "Invalid choice. Please try again." + Colour.RESET);
                return;
            }
            patientID = patientIDs.get(choice);

        } catch (InputMismatchException e) {
            System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            return;
        }

        List<AppointmentOutcome> appointmentOutcomes = app.getAppointmentService().getAppointmentOutcomesPendingPrescriptionByPatientID(patientID);

        if (appointmentOutcomes.isEmpty()) {
            System.out.println("No pending prescriptions for this patient.");
            return;
        }

        for (AppointmentOutcome appointmentOutcome : appointmentOutcomes) {
            // Print the AppointmentOutcome
            System.out.println("\n" + Colour.GREEN + " == Appointment Outcome == " + Colour.RESET);
            System.out.println(Color.YELLOW + appointmentOutcome.toPrintString() + Colour.RESET);
            // Display Pending Prescriptions
            System.out.println("\n" + Colour.GREEN + " == Pending Prescriptions == " + Colour.RESET);
            List<DrugDispenseRequest> pendingPrescriptions = appointmentOutcome.getPrescribedMedication();
            for (DrugDispenseRequest pendingPrescription : pendingPrescriptions) {
                System.out.println(pendingPrescription.toPrintString());
            }
        }

        logPharmacistAction("Viewed appointment outcomes");
    }

    /**
     * Handles the updating of prescription status.
     * This method will display a list of patients with pending prescriptions.
     * The pharmacist can then select a patient to view the appointment outcomes and pending prescriptions.
     * The method will display the appointment outcomes and pending prescriptions for the selected patient.
     * The pharmacist can then choose to dispense the prescription or not.
     * The method will update the prescription status and log the action in the audit log.
     * @see AppointmentOutcome
     */
    private void handleUpdatePrescriptionStatus() {

        System.out.println("\n" + Colour.BLUE + "=== Dispense Pending Prescriptions ===" + Colour.RESET);
        System.out.println("Recent patients with pending Prescriptions:");

        List<String> patientIDs = app.getAppointmentService().getPatientIDsWithPendingDrugRequest();
        // Select a patient
        if (patientIDs.isEmpty()) {
            System.out.println("No patients with pending prescriptions.");
            return;
        }

        String patientID = "";
        try {
            for (int i = 0; i < patientIDs.size(); i++) {
                System.out.println(i + ". " + patientIDs.get(i));
            }
            System.out.println("Select a patient (Enter the entry number):");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 0 || choice >= patientIDs.size()) {
                System.out.println(Colour.RED + "Invalid choice. Please try again." + Colour.RESET);
                return;
            }
            patientID = patientIDs.get(choice);

        } catch (InputMismatchException e) {
            System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            return;
        }

        List<AppointmentOutcome> appointmentOutcomes = app.getAppointmentService().getAppointmentOutcomesPendingPrescriptionByPatientID(patientID);

        if (appointmentOutcomes.isEmpty()) {
            System.out.println("No pending prescriptions for this patient.");
            return;
        }

        // Show patient's details first
        System.out.println(Colour.GREEN + " == Patient Particulars == " + Colour.RESET);
        String personalParticulars = app.getMedicalRecordService().getPatientPersonalParticulars(patientID);
        System.out.println(personalParticulars);
        System.out.println(Colour.GREEN + " == Medical History == " + Colour.RESET);
        String medicalRecordString = app.getMedicalRecordService().getPatientMedicalRecord(patientID);
        System.out.println(medicalRecordString);

        //Handle every AppointmentOutcome
        for (int j = 0; j < appointmentOutcomes.size(); j++) {

            // Print the AppointmentOutcome
            System.out.println(Colour.GREEN + " == Appointment Outcome == " + Colour.RESET);
            System.out.println(appointmentOutcomes.get(j).toPrintString());

            List<DrugDispenseRequest> dispenseRequests = appointmentOutcomes.get(j).getPrescribedMedication();

            for (int i = 0; i < dispenseRequests.size(); i++) {

                System.out.println(Colour.GREEN + " == Prescription Details == " + Colour.RESET);
                System.out.println(dispenseRequests.get(i).toPrintString());

                // Prompt confirmation for dispense
                System.out.print("Dispense this prescription? (Y/N): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    System.out.println("Prescription not dispensed.");
                    continue;
                } else if (choice.equalsIgnoreCase("Y")) {
                    boolean success = app.getDrugDispensaryService().dispenseDrug(dispenseRequests.get(i));
                    if (success) {

                        //TODO: Remove this assert
                        assert (dispenseRequests.get(i).getStatus() == DrugRequestStatus.DISPENSED);

                        System.out.println(Colour.GREEN + "Prescription dispensed successfully." + Colour.RESET);
                        app.getAppointmentService().updateAppointmentOutcometoCSV();
                    } else {
                        System.out.println(Colour.RED + "Failed to dispense prescription." + Colour.RESET);
                    }
                } else {
                    System.out.println(Colour.RED + "Invalid choice. Please try again." + Colour.RESET);
                    i--;
                }
            }
            System.out.println("Handled AppointmentOutcome " + (j + 1) + " of " + appointmentOutcomes.size());
        }

        logPharmacistAction("Dispensed pending prescriptions");
    }

    /**
     * Handles the viewing of the medication inventory.
     * This method will display the current medication inventory.
     */
    private void handleViewMedicationInventory() {
        System.out.println("\n" + Colour.BLUE + "=== Medication Inventory ===" + Colour.RESET);
        String drugInventoryString = app.getDrugDispensaryService().getDrugInventoryAsString();
        System.out.println(drugInventoryString);

        logPharmacistAction("Viewed medication inventory");
    }

    /**
     * Handles the submission of a drug replenishment request.
     * This method will display the current medication inventory, and then
     * prompt the pharmacist to select a drug to replenish.
     * The method will then add a new entry to the drug replenish request table
     * and update the inventory accordingly.
     */
    private void handleSubmitDrugReplenishRequest() {
        System.out.println("\n" + Colour.BLUE + "=== Submit Replenishment Request ===" + Colour.RESET);

        // Display current inventory
        handleViewMedicationInventory();

        // Select drug to replenish
        System.out.println("\nEnter the ID of the medication to replenish: ");
        int entryID = -1;
        while (entryID == -1) {
            try {
                entryID = Integer.parseInt(scanner.nextLine());
                if (!app.getDrugDispensaryService().isValidDrugEntryID(entryID)) {
                    System.out.println(Colour.RED + "Invalid medication ID. Please try again." + Colour.RESET);
                    entryID = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }

        // Enter quantity to add
        System.out.println("Enter the quantity to request: ");
        int quantity = -1;
        while (quantity == -1) {
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity <= 0) {
                    System.out.println(Colour.RED + "Quantity must be greater than 0." + Colour.RESET);
                    quantity = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }

        // Enter replenishment notes
        System.out.println("Enter replenishment notes (reason for request): ");
        String notes = scanner.nextLine();

        // Submit request
        app.getDrugDispensaryService().submitReplenishRequest(entryID, quantity, notes);

        System.out.println(Colour.GREEN + "Successfully submitted replenishment request." + Colour.RESET);
        logPharmacistAction("Submitted replenishment request for medication ID: " + entryID);
    }

    /**
     * Logs the pharmacist action in the audit log.
     * @param action The action to log
     */
    private void logPharmacistAction(String action) {
        AuditLogger.logAction(
                userContext.getName(),
                "PHARMACIST",
                String.valueOf(userContext.getHospitalID()),
                action
        );
    }
}