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
            System.out.println("1. View Appointment Outcome Records");
            System.out.println("2. Dispense Drug for Prescription");
            System.out.println("3. Check Drug Stock");
            System.out.println("4. Request Drug Replenishment");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleViewAppointmentOutcomes();
                    case 2 -> handleDispenseDrug();
                    case 3 -> handleCheckDrugStock();
                    case 4 -> handleDrugReplenishRequest();
                    case 5 -> {
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

    // TODO: For Nich & Yingjie to work on.
    private void handleViewAppointmentOutcomes() {
        System.out.println("Feature coming soon");
    }


    // TODO: For Nich to implement
    private void handleDispenseDrug() {
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

    // TODO: For Nich to implement
    private void handleCheckDrugStock() {
        System.out.println("\n=== Drug Stock Levels ===");
        System.out.println("Pharmacist: " + userContext.getName());
        System.out.println("Hospital ID: " + userContext.getHospitalID());

        logPharmacistAction("Checked drug stock levels");
        // Implementation would show current inventory
        System.out.println("Feature coming soon...");
    }

    // TODO: For Nich to implement
    private void handleDrugReplenishRequest() {

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