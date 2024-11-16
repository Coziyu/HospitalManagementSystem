package org.hms.views;

import org.hms.App;
import org.hms.entities.Colour;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.entities.User;
import org.hms.services.drugdispensary.DrugInventoryTable;

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
        printLowStockAlertMessage();
        while (true) {
            System.out.println(Colour.BLUE + "=== Administrator Menu ===" + Colour.RESET);
            System.out.println("Logged in as: " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("1. View and Manage Users");
            System.out.println("2. View Scheduled Appointment Details");
            System.out.println("3. View and Manage Drug Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleManageUsers();
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
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Prints a low stock alert message indicating which drugs are running low in inventory.
     * This method retrieves drugs that are marked as low in stock from the DrugDispensaryService,
     * and if there are any such drugs, it prints their details to the console in a formatted string.
     */
    private void printLowStockAlertMessage() {
        DrugInventoryTable lowStockView = app.getDrugDispensaryService().getLowStockDrugs();
        if (!lowStockView.getEntries().isEmpty()) {
            System.out.println(Colour.YELLOW + "Low stock alert: The following drugs are running low in stock: ");
            System.out.print(lowStockView.toPrintString() + Colour.RESET);
        }
    }

    /**
     * Handles the "View Scheduled Appointment Details" option by displaying all appointments to the console.
     * This method simply delegates to the AppointmentService's displayAllAppointments method.
     * TODO: Look into beautifying print.
     */
    private void handleViewAppointments() {
        app.getAppointmentService().displayAllAppointments();
    }


    /**
     * Handles the "Manage Drug Inventory" option by presenting a sub-menu to the admin user and
     * delegating to specific methods based on the user's selection.
     * <p>
     * This method displays a menu with options to view drug stock, add a new drug, delete a drug,
     * update drug quantity, or update the low stock threshold.
     * <p>
     * The method loops until the user selects the "Back to Main Menu" option, at which point it logs
     * the exit and returns.
     */
    private void handleManageDrugInventory() {
        while (true) {
            System.out.println(Colour.BLUE + "=== Manage Drug Inventory ===" + Colour.RESET);
            System.out.println("1. View Drug Stock");
            System.out.println("2. Add New Drug");
            System.out.println("3. Delete Drug");
            System.out.println("4. Update Drug Quantity");
            System.out.println("5. Update Low Stock Threshold");
            System.out.println("6. Back to Main Menu");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleDisplayAllDrugs();
                    case 2 -> handleAddNewDrug();
                    case 3 -> handleDeleteDrug();
                    case 4 -> handleUpdateDrugQuantity();
                    case 5 -> handleUpdateLowStockThreshold();
                    case 6 -> {
                        logAdminAction("Exited drug inventory management");
                        return;
                    }
                    default -> System.out.println(Colour.RED + "Invalid option. Please try again." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Updates the low stock threshold for a specific drug in the inventory.
     * This method displays a list of drugs in the inventory and prompts the
     * administrator to select a drug EntryID to update. It then requests the new
     * low stock threshold for the selected drug, validates the entry, and
     * updates the low stock threshold in the inventory. If successful, it logs
     * the action; otherwise, it outputs an error message.
     */
    private void handleUpdateLowStockThreshold() {
        System.out.println(Colour.BLUE + "=== Update Low Stock Threshold ===" + Colour.RESET);
        while (true) {
            System.out.println(app.getDrugDispensaryService().getDrugInventoryAsString());
            System.out.println("Select the drug EntryID to update: (-1 to go back)");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                // Check that entry is a valid entry
                boolean valid = app.getDrugDispensaryService().isValidDrugEntryID(choice);
                if (!valid) {
                    if (choice == -1) {
                        return;
                    }
                    System.out.println(Colour.RED + "Invalid EntryID. Please try again." + Colour.RESET);
                    continue;
                }

                System.out.println("Enter the new low stock threshold: ");
                int newThreshold = Integer.parseInt(scanner.nextLine());
                String drugName = app.getDrugDispensaryService().getDrugName(choice);

                boolean success = app.getDrugDispensaryService().setDrugLowStockAlertThreshold(drugName, newThreshold);

                if (success) {
                    logAdminAction("Updated low stock threshold for " + drugName + " to " + newThreshold);
                    System.out.println(Colour.GREEN + "Low stock threshold for " + drugName + " updated to " + newThreshold + Colour.RESET);
                } else {
                    System.out.println("Failed to update low stock threshold for " + drugName + ". Please try again.");
                }
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Updates the quantity of a specific drug in the inventory.
     * This method displays a list of drug replenish requests and prompts the
     * administrator to select a drug EntryID to update. It then requests the new
     * quantity for the selected drug, validates the entry, and updates the drug
     * stock quantity in the inventory. If successful, it logs the action; otherwise,
     * it outputs an error message.
     */
    private void handleUpdateDrugQuantity() {
        System.out.println(Colour.BLUE + "=== Update Drug Quantity ===" + Colour.RESET);
        while (true) {
            System.out.println(app.getDrugDispensaryService().getDrugInventoryAsString());
            System.out.println("Select the drug EntryID to update: (-1 to go back)");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                // Check that entry is a valid entry
                boolean valid = app.getDrugDispensaryService().isValidDrugEntryID(choice);
                if (!valid) {
                    if (choice == -1) {
                        return;
                    }
                    System.out.println(Colour.RED + "Invalid EntryID. Please try again." + Colour.RESET);
                    continue;
                }

                System.out.println("Enter the new quantity: ");
                int newQuantity = Integer.parseInt(scanner.nextLine());
                String drugName = app.getDrugDispensaryService().getDrugName(choice);

                boolean success = app.getDrugDispensaryService().setDrugStockQuantity(drugName, newQuantity);

                if (!success) {
                    System.out.println("Failed to update quantity. Please try again.");
                }
                else {
                    System.out.println(Colour.GREEN + "Quantity updated successfully." + Colour.RESET);
                    logAdminAction("Updated quantity for " + drugName + " to " + newQuantity);
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Allows the administrator to delete a drug from the inventory.
     * This method prompts the administrator to select a drug EntryID from the list of drugs
     * in the inventory, and then attempts to delete the selected drug from the inventory.
     * If the deletion is successful, a success message is printed; otherwise, an error message
     * is printed.
     */
    private void handleDeleteDrug() {
        System.out.println(Colour.BLUE + "=== Delete Drug ===" + Colour.RESET);
        while (true) {
            System.out.println(app.getDrugDispensaryService().getDrugInventoryAsString());
            System.out.println("Select the drug EntryID to delete: (-1 to go back)  ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                // Check that entry is a valid entry
                boolean valid = app.getDrugDispensaryService().isValidDrugEntryID(choice);
                if (!valid) {
                    if (choice == -1) {
                        return;
                    }
                    System.out.println(Colour.RED + "Invalid EntryID. Please try again." + Colour.RESET);
                    continue;
                }
                        
                boolean success = app.getDrugDispensaryService().removeDrugFromInventory(choice);

                if (success) {
                    logAdminAction("Deleted drug with EntryID " + choice);
                    System.out.println(Colour.GREEN + "Drug deleted successfully." + Colour.RESET);
                } else {
                    System.out.println("Failed to delete drug. Please try again.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Displays the current drug inventory.
     * This method displays a list of all drugs in the inventory, including their EntryID,
     * name, quantity, and whether they are low in stock.
     */
    private void handleDisplayAllDrugs() {
        System.out.println(Colour.BLUE + "=== Drug Inventory ===" + Colour.RESET);
        String drugInventoryString = app.getDrugDispensaryService().getDrugInventoryAsString();
        System.out.println(drugInventoryString);
    }

    /**
     * Prompts the user for a drug name, quantity, and low stock alert threshold.
     * Then, adds the drug to the inventory using the provided parameters.
     * If the drug already exists, displays an error message and returns.
     * If the drug is successfully added, displays a success message.
     * If the drug cannot be added for any reason, displays an error message.
     */
    private void handleAddNewDrug() {

        System.out.println(Colour.BLUE + "=== Add New Drug ===" + Colour.RESET);
        System.out.println("Enter Drug Name: ");
        String drugName = scanner.nextLine();
        int quantity = -1;
        int lowStockAlertThreshold = -2;
        // Check if the drug already exists
        if (app.getDrugDispensaryService().doesDrugExist(drugName)) {
            System.out.println(Colour.RED + "Drug already exists." + Colour.RESET);
            return;
        }

        while (quantity < 0) {
            try {
                System.out.println("Enter Drug Quantity: ");
                quantity = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }

        while (lowStockAlertThreshold < -1) {
            try {
                System.out.println("Enter Low Stock Alert Threshold: (-1 to disable) ");
                lowStockAlertThreshold = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }

        boolean success = app.getDrugDispensaryService().addNewDrug(drugName, quantity, lowStockAlertThreshold);
        if (success) {
            System.out.println(Colour.GREEN + "Drug added successfully." + Colour.RESET);
        }
        else {
            System.out.println(Colour.RED + "Failed to add drug. Please try again." + Colour.RESET);
        }
    }



    // TODO: For Nich to implement
    private void handleApproveReplenishmentRequests() {

    }

    // TODO: For Amos to implement Staff related methods.
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