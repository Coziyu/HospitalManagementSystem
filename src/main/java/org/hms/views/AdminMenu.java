package org.hms.views;

import org.hms.App;
import org.hms.entities.Colour;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.entities.User;
import org.hms.services.drugdispensary.DrugInventoryTable;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class AdminMenu extends AbstractMainMenu {
    private final Scanner scanner;
    private final UserContext userContext;

    public AdminMenu(App app) {
        this.app = app;
        this.userContext = app.getUserContext();
        this.scanner = new Scanner(System.in);
        validateAdminAccess();
    }

    private void validateAdminAccess() {
        if (userContext == null || userContext.getUserType() != UserRole.ADMINISTRATOR) {
            System.out.println(Colour.RED + "Access Denied: Administrator privileges required." + Colour.RESET);
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    @Override
    public void displayAndExecute() {
        printLowStockAlertMessage();
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Administrator Menu ===" + Colour.RESET);
            System.out.println("Logged in as: " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("Date: " + LocalDate.now());
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleManageHospitalStaff();
                    case 2 -> handleViewAppointments();
                    case 3 -> handleManageDrugInventory();
                    case 4 -> handleApproveReplenishmentRequests();
                    case 5 -> {
                        logAdminAction("Logged out");
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

    // TODO: For Nich to implement
    private void printLowStockAlertMessage() {
        DrugInventoryTable lowStockView = app.getDrugDispensaryService().getLowStockDrugs();
        if (!lowStockView.getEntries().isEmpty()) {
            System.out.println(Colour.YELLOW + "Low stock alert: The following drugs are running low in stock: ");
            System.out.print(lowStockView.toPrintString() + Colour.RESET);
        }
    }

    // TODO: For Yingjie to implement
    private void handleViewAppointments() {
        app.getAppointmentService().displayAllAppointments();
    }
    // TODO: For Nich to implement
    private void handleManageDrugInventory() {

    }
    // TODO: For Nich to implement
    private void handleApproveReplenishmentRequests() {

    }

    // TODO: For Amos to implement Staff related methods.
    private void handleManageHospitalStaff() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Hospital Staff Management ===" + Colour.RESET);
            System.out.println("Managing staff for Hospital: " + userContext.getHospitalID());
            System.out.println("1. Add New Staff Member");
            System.out.println("2. Update Staff Information");
            System.out.println("3. Remove Staff Member");
            System.out.println("4. View Staff List");
            System.out.println("5. Filter Staff");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleAddStaff();
                    case 2 -> handleUpdateStaff();
                    case 3 -> handleRemoveStaff();
                    case 4 -> handleViewStaff();
                    case 5 -> handleFilterStaff();
                    case 6 -> {
                        logAdminAction("Exited staff management");
                        return;
                    }
                    default -> System.out.println(Colour.RED + "Invalid option. Please try again." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }
    }

    private void handleAddStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Add New Staff Member ===" + Colour.RESET);
        try {
            System.out.print("Enter staff ID: ");
            String staffId = scanner.nextLine();

            System.out.println("Select staff role:");
            System.out.println("1. Doctor");
            System.out.println("2. Pharmacist");
            System.out.print("Enter choice: ");

            int roleChoice = Integer.parseInt(scanner.nextLine());
            UserRole role = switch (roleChoice) {
                case 1 -> UserRole.DOCTOR;
                case 2 -> UserRole.PHARMACIST;
                default -> throw new IllegalArgumentException("Invalid role selection");
            };

            // Add additional staff information
            System.out.print("Enter full name: ");
            String name = scanner.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter gender (M/F): ");
            String gender = scanner.nextLine();

            // TODO: Implement actual staff creation
            logAdminAction("Added new staff member: " + staffId + " with role: " + role);
            System.out.println(Colour.GREEN + "Staff member added successfully!" + Colour.RESET);
        } catch (Exception e) {
            System.out.println(Colour.RED + "Error adding staff member: " + e.getMessage() + Colour.RESET);
        }
    }

    private void handleUpdateStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Update Staff Information ===" + Colour.RESET);
        System.out.print("Enter staff ID to update: ");
        String staffId = scanner.nextLine();
        // TODO: Implement staff information update
        logAdminAction("Updated staff member: " + staffId);
        System.out.println("Feature coming soon...");
    }

    private void handleRemoveStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Remove Staff Member ===" + Colour.RESET);
        System.out.print("Enter staff ID to remove: ");
        String staffId = scanner.nextLine();
        // TODO: Implement staff removal
        logAdminAction("Removed staff member: " + staffId);
        System.out.println("Feature coming soon...");
    }

    private void handleViewStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Staff List ===" + Colour.RESET);
        // TODO: Implement staff listing
        System.out.println("\nDoctors:");
        System.out.println("ID\tName\tGender\tAge");
        System.out.println("---------------------------");

        System.out.println("\nPharmacists:");
        System.out.println("ID\tName\tGender\tAge");
        System.out.println("---------------------------");

        logAdminAction("Viewed staff list");
    }

    private void handleFilterStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Filter Staff ===" + Colour.RESET);
        System.out.println("Filter by:");
        System.out.println("1. Role");
        System.out.println("2. Gender");
        System.out.println("3. Age Range");

        // TODO: Implement staff filtering
        System.out.println("Feature coming soon...");
    }

    private void logAdminAction(String action) {
        String logMessage = String.format("Admin Action - User: %s, Hospital: %d - %s",
                userContext.getName(),
                userContext.getHospitalID(),
                action
        );
        System.out.println("LOG: " + LocalDateTime.now() + " - " + logMessage);
        // In a real system, this would write to a secure audit log
    }
}