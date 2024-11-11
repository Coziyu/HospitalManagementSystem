package org.hms.views;

import org.hms.App;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.entities.User;

import java.util.Scanner;
import java.time.LocalDateTime;

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
            System.out.println("Access Denied: Administrator privileges required.");
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n=== Administrator Menu ===");
            System.out.println("Logged in as: " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("1. Manage Users");
            System.out.println("2. View System Logs");
            System.out.println("3. Configure System Settings");
            System.out.println("4. Generate Reports");
            System.out.println("5. Manage Departments");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleManageUsers();
                    case 2 -> handleViewLogs();
                    case 3 -> handleConfigureSettings();
                    case 4 -> handleGenerateReports();
                    case 5 -> handleManageDepartments();
                    case 6 -> {
                        logAdminAction("Logged out");
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

    private void handleManageUsers() {
        while (true) {
            System.out.println("\n=== User Management ===");
            System.out.println("Managing users for Hospital: " + userContext.getHospitalID());
            System.out.println("1. Add New User");
            System.out.println("2. Modify User");
            System.out.println("3. Deactivate User");
            System.out.println("4. List Users");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleAddUser();
                    case 2 -> handleModifyUser();
                    case 3 -> handleDeactivateUser();
                    case 4 -> handleListUsers();
                    case 5 -> {
                        logAdminAction("Exited user management");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void handleAddUser() {
        System.out.println("\n=== Add New User ===");
        System.out.println("Adding user for Hospital: " + userContext.getHospitalID());

        try {
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();

            System.out.println("Select user role:");
            System.out.println("1. Doctor");
            System.out.println("2. Patient");
            System.out.println("3. Pharmacist");
            System.out.println("4. Administrator");
            System.out.print("Enter choice: ");

            int roleChoice = Integer.parseInt(scanner.nextLine());
            UserRole role = switch (roleChoice) {
                case 1 -> UserRole.DOCTOR;
                case 2 -> UserRole.PATIENT;
                case 3 -> UserRole.PHARMACIST;
                case 4 -> UserRole.ADMINISTRATOR;
                default -> throw new IllegalArgumentException("Invalid role selection");
            };

            // Here you would call your user service to create the user
            logAdminAction("Added new user: " + userId + " with role: " + role);
            System.out.println("User added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding user: " + e.getMessage());
            logAdminAction("Failed to add user: " + e.getMessage());
        }
    }

    private void handleModifyUser() {
        System.out.println("\n=== Modify User ===");
        System.out.println("Modifying user for Hospital: " + userContext.getHospitalID());

        System.out.print("Enter user ID to modify: ");
        String userId = scanner.nextLine();
        logAdminAction("Attempted to modify user: " + userId);
        System.out.println("Feature coming soon...");
    }

    private void handleDeactivateUser() {
        System.out.println("\n=== Deactivate User ===");
        System.out.println("Deactivating user for Hospital: " + userContext.getHospitalID());

        System.out.print("Enter user ID to deactivate: ");
        String userId = scanner.nextLine();
        logAdminAction("Attempted to deactivate user: " + userId);
        System.out.println("Feature coming soon...");
    }

    private void handleListUsers() {
        System.out.println("\n=== User List ===");
        System.out.println("Listing users for Hospital: " + userContext.getHospitalID());
        logAdminAction("Viewed user list");
        System.out.println("Feature coming soon...");
    }

    private void handleViewLogs() {
        System.out.println("\n=== System Logs ===");
        System.out.println("Viewing logs for Hospital: " + userContext.getHospitalID());
        logAdminAction("Viewed system logs");
        System.out.println("Feature coming soon...");
    }

    private void handleConfigureSettings() {
        System.out.println("\n=== System Configuration ===");
        System.out.println("Configuring settings for Hospital: " + userContext.getHospitalID());
        logAdminAction("Accessed system configuration");
        System.out.println("Feature coming soon...");
    }

    private void handleGenerateReports() {
        System.out.println("\n=== Generate Reports ===");
        System.out.println("Generating reports for Hospital: " + userContext.getHospitalID());
        logAdminAction("Generated reports");
        System.out.println("Feature coming soon...");
    }

    private void handleManageDepartments() {
        System.out.println("\n=== Department Management ===");
        System.out.println("Managing departments for Hospital: " + userContext.getHospitalID());
        logAdminAction("Accessed department management");
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