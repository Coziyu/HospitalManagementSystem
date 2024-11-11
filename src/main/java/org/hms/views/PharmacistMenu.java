package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import java.time.LocalDateTime;
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
            System.out.println("Access Denied: Pharmacist privileges required.");
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n=== Pharmacist Menu ===");
            System.out.println("Pharmacist: " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("1. View Prescriptions");
            System.out.println("2. Dispense Medication");
            System.out.println("3. Check Medication Stock");
            System.out.println("4. Update Inventory");
            System.out.println("5. View Patient History");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleViewPrescriptions();
                    case 2 -> handleDispenseMedication();
                    case 3 -> handleCheckStock();
                    case 4 -> handleUpdateInventory();
                    case 5 -> handleViewPatientHistory();
                    case 6 -> {
                        logPharmacistAction("Logged out");
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

    private void handleViewPrescriptions() {
        System.out.println("\n=== View Prescriptions ===");
        System.out.println("Pharmacist: " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        System.out.print("Enter patient ID: ");
        try {
            int patientId = Integer.parseInt(scanner.nextLine());
            logPharmacistAction("Viewed prescriptions for patient ID: " + patientId);
            // Implementation would show patient's prescriptions
            System.out.println("Feature coming soon...");
        } catch (NumberFormatException e) {
            System.out.println("Invalid patient ID format.");
        }
    }

    private void handleDispenseMedication() {
        System.out.println("\n=== Dispense Medication ===");
        System.out.println("Pharmacist: " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        try {
            System.out.print("Enter prescription ID: ");
            String prescriptionId = scanner.nextLine();

            System.out.print("Enter patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            logPharmacistAction("Dispensed medication for prescription ID: " + prescriptionId +
                    " to patient ID: " + patientId);
            // Implementation would handle medication dispensing
            System.out.println("Feature coming soon...");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
        }
    }

    private void handleCheckStock() {
        System.out.println("\n=== Medication Stock Levels ===");
        System.out.println("Pharmacist: " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        logPharmacistAction("Checked medication stock levels");
        // Implementation would show current inventory
        System.out.println("Feature coming soon...");
    }

    private void handleUpdateInventory() {
        System.out.println("\n=== Update Inventory ===");
        System.out.println("Pharmacist: " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        System.out.print("Enter medication ID: ");
        try {
            String medicationId = scanner.nextLine();
            System.out.print("Enter quantity change (+/-): ");
            int quantityChange = Integer.parseInt(scanner.nextLine());

            logPharmacistAction("Updated inventory for medication ID: " + medicationId +
                    " by " + quantityChange + " units");
            // Implementation would allow updating medication stock
            System.out.println("Feature coming soon...");
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity format.");
        }
    }

    private void handleViewPatientHistory() {
        System.out.println("\n=== Patient Medication History ===");
        System.out.println("Pharmacist: " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        System.out.print("Enter patient ID: ");
        try {
            int patientId = Integer.parseInt(scanner.nextLine());
            logPharmacistAction("Viewed medication history for patient ID: " + patientId);
            // Implementation would show patient's medication history
            System.out.println("Feature coming soon...");
        } catch (NumberFormatException e) {
            System.out.println("Invalid patient ID format.");
        }
    }

    private void logPharmacistAction(String action) {
        String logMessage = String.format("Pharmacist Action - Name: %s, Hospital: %d - %s",
                userContext.getName(),
                userContext.getHospitalID(),
                action
        );
        System.out.println("LOG: " + LocalDateTime.now() + " - " + logMessage);
    }
}