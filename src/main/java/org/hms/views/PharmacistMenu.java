package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.entities.Colour;
import org.hms.services.logging.AuditLogger;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Scanner;

public class PharmacistMenu extends AbstractMainMenu {
    private final Scanner scanner;
    private final UserContext userContext;

    public PharmacistMenu(App app) {
        this.app = app;
        this.userContext = app.getUserContext();
        this.scanner = new Scanner(System.in);
        validatePharmacistAccess();
    }

    private void validatePharmacistAccess() {
        if (userContext == null || userContext.getUserType() != UserRole.PHARMACIST) {
            System.out.println(Colour.RED + "Access Denied: Pharmacist privileges required." + Colour.RESET);
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

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

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    // TODO: For Nich & Yingjie to work on.
    private void handleViewAppointmentOutcomes() {
        System.out.println("\n" + Colour.BLUE + "=== View Appointment Outcome Records ===" + Colour.RESET);
        System.out.println("Recent Appointment Outcomes with Prescriptions:");

        // TODO: Implement fetching and displaying actual appointment outcomes
        System.out.println("\nAppointment ID: APT001");
        System.out.println("Date: 15/11/2024");
        System.out.println("Patient: P001");
        System.out.println("Doctor: Dr. Smith");
        System.out.println("Prescribed Medications:");
        System.out.println("1. Amoxicillin 500mg - Status: Pending");
        System.out.println("2. Paracetamol 500mg - Status: Dispensed");

        logPharmacistAction("Viewed appointment outcomes");
    }

    private void handleUpdatePrescriptionStatus() {
        System.out.println("\n" + Colour.BLUE + "=== Update Prescription Status ===" + Colour.RESET);

        // First show pending prescriptions
        System.out.println("Pending Prescriptions:");
        System.out.println("1. APT001-1: Amoxicillin 500mg (Patient: P001)");
        System.out.println("2. APT002-1: Ibuprofen 400mg (Patient: P002)");

        System.out.print("\nEnter prescription number to update (0 to return): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0) {
                System.out.println("\nUpdate Status to:");
                System.out.println("1. Dispensed");
                System.out.println("2. Out of Stock");
                System.out.println("3. Cancelled");

                System.out.print("Select new status: ");
                int statusChoice = Integer.parseInt(scanner.nextLine());

                // TODO: Implement actual status update
                System.out.println(Colour.GREEN + "Prescription status updated successfully!" + Colour.RESET);
                logPharmacistAction("Updated prescription status");
            }
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input. Please enter a number." + Colour.RESET);
        }
    }

    private void handleViewMedicationInventory() {
        System.out.println("\n" + Colour.BLUE + "=== Medication Inventory ===" + Colour.RESET);
        String drugInventoryString = app.getDrugDispensaryService().getDrugInventoryAsString();
        System.out.println(drugInventoryString);

        System.out.println("\nLow Stock Alerts:");
        // TODO: Implement low stock checking logic
        System.out.println("- Amoxicillin 500mg (5 units remaining)");
        System.out.println("- Paracetamol 500mg (10 units remaining)");

        logPharmacistAction("Viewed medication inventory");
    }

    // TODO: For Nich to TEST CORRECTNESS
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

    private void logPharmacistAction(String action) {
        AuditLogger.logAction(
                userContext.getName(),
                "PHARMACIST",
                String.valueOf(userContext.getHospitalID()),
                action
        );
    }
}