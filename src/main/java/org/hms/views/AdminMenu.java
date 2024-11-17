package org.hms.views;

import org.hms.App;
import org.hms.entities.Colour;
import org.hms.entities.UserContext;
import org.hms.entities.UserRole;
import org.hms.entities.User;
import org.hms.services.drugdispensary.DrugInventoryTable;
import org.hms.services.logging.AuditLogger;
import org.hms.services.staffmanagement.Staff;
import org.hms.services.staffmanagement.StaffManagementService;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * The AdminMenu class handles the display and execution of menu options specific to the administrator role.
 * It provides functionalities for managing hospital staff, viewing scheduled appointments, managing medication inventory,
 * and other administrative tasks.
 * This class extends AbstractMainMenu and ensures that only users with administrator privileges can access these functionalities.
 */
public class AdminMenu extends AbstractMainMenu {
    /**
     * A Scanner instance used to capture input from the console.
     * This is declared as a final variable to ensure only one
     * instance is created and used throughout the lifetime of the AdminMenu.
     */
    private final Scanner scanner;
    /**
     * The UserContext associated with the current admin session.
     * This variable holds the essential user information needed to
     * validate and authorize administrative actions within the menu.
     */
    private final UserContext userContext;
    /**
     * Service responsible for managing hospital staff operations within
     * the AdminMenu context. This includes adding, updating, and removing staff,
     * as well as other staff-related functionalities.
     * <p>
     * This service interacts with the main application logic to handle staff-specific
     * tasks required by the admin.
     */
    private final StaffManagementService staffManagementService;

    /**
     * Constructs an instance of AdminMenu, initializing its context with the given App instance.
     * This constructor initializes the scanner, userContext, and staffManagementService fields,
     * and ensures that the current user has administrative privileges.
     *
     * @param app the instance of the App class that provides the necessary services and context
     */
    public AdminMenu(App app) {
        this.app = app;
        this.userContext = app.getUserContext();
        this.scanner = new Scanner(System.in);
        this.staffManagementService = app.getStaffManagementService();
        validateAdminAccess();
    }

    /**
     * Validates whether the current user has administrator access.
     * If the user does not have admin privileges or userContext is null,
     * an access denied message is displayed, and the application redirects
     * to the authentication menu.
     */
    private void validateAdminAccess() {
        if (userContext == null || userContext.getUserType() != UserRole.ADMINISTRATOR) {
            System.out.println(Colour.RED + "Access Denied: Administrator privileges required." + Colour.RESET);
            app.setCurrentMenu(new AuthenticationMenu(app));
        }
    }

    /**
     * Displays the administrator menu and handles the execution of selected options.
     * The options provided in the menu include:
     * <ol>
     *   <li>View and manage hospital staff</li>
     *   <li>View scheduled appointments details</li>
     *   <li>View and manage medication inventory</li>
     *   <li>Approve or reject replenishment requests</li>
     *   <li>Logout</li>
     * </ol>
     * The method will prompt the administrator to select an option and will
     * continuously display the menu until the logout option is selected.
     * This method also prints a low stock alert message upon entering the menu.
     */
    @Override
    public void displayAndExecute() {
        printLowStockAlertMessage();
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Administrator Menu ===" + Colour.RESET);
            System.out.println("Logged in as: " + userContext.getName());
            System.out.println("Hospital ID: " + userContext.getHospitalID());
            System.out.println("Date: " + LocalDate.now());
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Scheduled Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve/Reject Replenishment Requests");
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

    /**
     * Prints a low stock alert message if there are any drugs running low in stock.
     * This method retrieves the drug entries that are low in stock from the drug dispensary service
     * and prints them in an ASCII table format to the console with a yellow-colored alert message.
     * If there are no drugs running low in stock, the method does nothing.
     */
    private void printLowStockAlertMessage() {
        DrugInventoryTable lowStockView = app.getDrugDispensaryService().getLowStockDrugs();
        if (!lowStockView.getEntries().isEmpty()) {
            System.out.println(Colour.YELLOW + "Low stock alert: The following drugs are running low in stock: ");
            System.out.print(lowStockView.toPrintString() + Colour.RESET);
        }
    }

    /**
     * Handles the action of viewing all scheduled appointments.
     * <p>
     * This method calls the AppointmentService to display all scheduled appointments.
     * It is intended to be used by administrative users to view detailed appointment information.
     * The method does not take any parameters and does not return any values.
     */
    private void handleViewAppointments() {
        app.getAppointmentService().displayAllAppointments();
    }


    /**
     * Handles the drug inventory management operations.
     * This method displays the Manage Drug Inventory menu and allows the user
     * to choose options related to viewing, adding, deleting, updating drug quantities,
     * updating low stock thresholds, or exiting back to the main menu.
     * Users must enter a valid number to select an option, and invalid inputs will
     * prompt an error message.
     * <p>
     * Options include:
     * 1. Viewing the current drug stock.
     * 2. Adding a new drug to the inventory.
     * 3. Deleting an existing drug from the inventory.
     * 4. Updating the quantity of an existing drug.
     * 5. Updating the low stock threshold for alerts.
     * 6. Exiting to the main menu.
     * <p>
     * The method continuously prompts the user until the exit option (6) is selected.
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
     * Handles the update of the low stock threshold for a drug in the inventory.
     * This method interacts with the user through the console to select a drug entry and set a new low stock threshold.
     * It displays a list of drug entries, prompts the user to select an entry by ID, and then prompts for a new threshold value.
     * The updated threshold is applied and confirmed or a failure message is displayed.
     * <p>
     * The method uses color coding for console messages to indicate different types of output such as headers, errors, and confirmations.
     * It validates user inputs to ensure they are correct and handles invalid entries gracefully.
     * <p>
     * The Drug Dispansary Service is utilized to perform actions such as fetching the current drug inventory, validating entry IDs,
     * retrieving drug names and setting new low stock thresholds.
     * <p>
     * If the update is successful, the action is logged and a confirmation message is shown to the user. If there is a failure, an error
     * message is displayed prompting the user to try again.
     * <p>
     * The user can return to the previous menu by entering -1 as the drug EntryID.
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
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Handles the process of updating the quantity of drugs in the dispensary inventory.
     * This method displays the current drug inventory, prompts the user to select a drug entry by its ID,
     * and allows the quantity of the selected drug to be updated.
     * <p>
     * The user can input the entry ID of the drug they wish to update. If the entered ID is valid,
     * they are then prompted to input the new quantity for that drug. The inventory is updated accordingly.
     * <p>
     * If the user enters an invalid ID, they are informed of the error and prompted to try again.
     * If the update is successful, a confirmation message is displayed; otherwise, an error message
     * is shown. The method also logs the update action if it is successful.
     * <p>
     * This method runs in a loop until a valid update is made or the user chooses to exit by entering -1.
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
                } else {
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
     * Handles the process of deleting a drug from the dispensary inventory.
     * <p>
     * This method displays the current drug inventory to the user and prompts
     * for the EntryID of the drug to be deleted. The user can enter -1 to go back.
     * It performs validation on the entered EntryID and attempts to remove the
     * drug from the inventory if the EntryID is valid. The method logs the actions
     * taken and provides feedback to the user about the success or failure of the
     * deletion process.
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
     * Displays the entire drug inventory in the console.
     * <p>
     * This method prints the drug inventory title with a blue color,
     * retrieves the current drug inventory as a string from the drug dispensary service,
     * and prints the result in the console.
     */
    private void handleDisplayAllDrugs() {
        System.out.println(Colour.BLUE + "=== Drug Inventory ===" + Colour.RESET);
        String drugInventoryString = app.getDrugDispensaryService().getDrugInventoryAsString();
        System.out.println(drugInventoryString);
    }

    /**
     * Handles the addition of a new drug to the dispensary.
     * <p>
     * This method prompts the administrator to enter the name, quantity,
     * and low stock alert threshold for a new drug. It handles user input
     * and validation, ensuring numeric entries where necessary. If the drug
     * already exists in the system, it notifies the user and terminates the
     * process. If all inputs are valid and the drug doesn't already exist,
     * it proceeds to add the new drug to the dispensary via the DrugDispensaryService.
     * <p>
     * The method also provides user feedback for successful or failed drug
     * additions using colored console output to enhance readability.
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
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }

        while (lowStockAlertThreshold < -1) {
            try {
                System.out.println("Enter Low Stock Alert Threshold: (-1 to disable) ");
                lowStockAlertThreshold = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }

        boolean success = app.getDrugDispensaryService().addNewDrug(drugName, quantity, lowStockAlertThreshold);
        if (success) {
            System.out.println(Colour.GREEN + "Drug added successfully." + Colour.RESET);
        } else {
            System.out.println(Colour.RED + "Failed to add drug. Please try again." + Colour.RESET);
        }
    }


    /**
     * Handles the process of approving or rejecting drug replenishment requests.
     * <p>
     * This method displays all current replenishment requests and prompts the admin
     * to approve or reject individual requests by their EntryID. The admin can also
     * approve all requests at once by entering 'a'. The selection and action are
     * validated and appropriate messages are displayed based on success or failure.
     * <p>
     * The admin's actions are logged for audit purposes.
     */
    private void handleApproveReplenishmentRequests() {
        // Print out the list of all replenishment requests
        System.out.println(Colour.BLUE + "=== Replenishment Requests ===" + Colour.RESET);
        System.out.println(app.getDrugDispensaryService().getDrugReplenishRequestsAsString());

        while (true) {
            System.out.println("Select the replenishment request EntryID to approve: (-1 to go back) (a to approve all) ");
            try {
                String rawInput = scanner.nextLine();
                if (rawInput.equals("a")) {
                    app.getDrugDispensaryService().approveAllReplenishRequests();
                    logAdminAction("Approved all replenishment requests");
                    System.out.println(Colour.GREEN + "All replenishment requests approved successfully." + Colour.RESET);
                    return;
                }
                int choice = Integer.parseInt(rawInput);
                // Check that entry is a valid entry
                boolean valid = app.getDrugDispensaryService().isValidReplenishRequestID(choice);
                if (!valid) {
                    if (choice == -1) {
                        return;
                    }
                    System.out.println(Colour.RED + "Invalid EntryID. Please try again." + Colour.RESET);
                    continue;
                }

                System.out.println("You have selected EntryID: " + choice + ". Approve or Reject?");
                System.out.println("1. Approve");
                System.out.println("2. Reject");
                System.out.print("Enter your choice: ");
                int option = Integer.parseInt(scanner.nextLine());
                if (option == 1) {
                    app.getDrugDispensaryService().processReplenishRequest(choice, true);
                    logAdminAction("Approved replenishment request with EntryID " + choice);
                    System.out.println(Colour.GREEN + "Replenishment request approved successfully." + Colour.RESET);
                } else if (option == 2) {
                    app.getDrugDispensaryService().processReplenishRequest(choice, false);
                    logAdminAction("Rejected replenishment request with EntryID " + choice);
                    System.out.println(Colour.GREEN + "Replenishment request rejected successfully." + Colour.RESET);
                } else {
                    System.out.println(Colour.RED + "Invalid option. Please try again." + Colour.RESET);
                }
                System.out.println(Colour.BLUE + "=== Replenishment Requests ===" + Colour.RESET);
                System.out.println(app.getDrugDispensaryService().getDrugReplenishRequestsAsString());
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }
    }


    /**
     * Handles the management of hospital staff, providing options to add, update,
     * remove, view, and filter staff members. It also allows returning to the main menu.
     * This method runs in a loop until the user chooses to exit to the main menu.
     * It presents the user with a menu of options related to staff management and processes
     * the user's input to call the appropriate handler methods.
     */
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

    /**
     * Handles the process of adding a new staff member to the system.
     * <p>
     * This method prompts the user for the necessary details to create a new
     * staff member, including staff ID, full name, age, role, status, and gender.
     * It processes the input to construct a Staff object and then attempts to
     * add this new staff member to the system. If the addition is successful
     * and the role is "Doctor", it updates all schedules with the new doctor.
     * <p>
     * In case of any invalid numeric input or other exceptions during the
     * process, appropriate error messages are displayed to the user.
     * <p>
     * Positively adds a new staff member and logs the admin action if successful.
     * Otherwise, it informs the user of the failure and possible reasons.
     * <p>
     * Exceptions:
     * - NumberFormatException: If numeric inputs such as age or role choice
     * are not valid integers.
     * - IllegalArgumentException: If an invalid role choice is made.
     * - Exception: For generic error handling.
     */
    private void handleAddStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Add New Staff Member ===" + Colour.RESET);
        try {
            // Prompt for all inputs in the order of constructor parameters
            System.out.print("Enter staff ID: ");
            String staffId = scanner.nextLine();

            System.out.print("Enter full name: ");
            String name = scanner.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.println("Select staff role:");
            System.out.println("1. Doctor");
            System.out.println("2. Pharmacist");
            System.out.print("Enter choice: ");
            int roleChoice = Integer.parseInt(scanner.nextLine());
            String role = switch (roleChoice) {
                case 1 -> "Doctor";
                case 2 -> "Pharmacist";
                default -> throw new IllegalArgumentException("Invalid role selection");
            };

            System.out.print("Enter status (e.g., active, inactive): ");
            String status = scanner.nextLine();

            System.out.print("Enter gender (M/F): ");
            String gender = scanner.nextLine();

            // Construct the Staff object with the inputs
            Staff newStaff = new Staff(staffId, age, name, role, status, gender);

            // Attempt to add the new staff to the system
            boolean success = staffManagementService.addStaff(newStaff);

            // Provide feedback to the user
            if (success) {
                if (role.equals("Doctor")) {
                    app.getAppointmentService().updateAllSchedulesWithNewDoctor(staffId);
                }
                logAdminAction("Added new staff member: " + staffId);
                System.out.println(Colour.GREEN + "Staff member added successfully!" + Colour.RESET);
            } else {
                System.out.println(Colour.RED + "Failed to add staff member. Staff ID might already exist." + Colour.RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input for numeric fields. Please enter valid numbers." + Colour.RESET);
        } catch (Exception e) {
            System.out.println(Colour.RED + "Error adding staff member: " + e.getMessage() + Colour.RESET);
        }
    }


    /**
     * Handles the update process for staff information.
     * <p>
     * This method prompts the admin to input the staff ID they wish to update and
     * fetches the corresponding staff details from the system. The admin is then
     * prompted to enter new values for the staff member's name, age, and role.
     * If an input is left blank, the corresponding field is not updated.
     * <p>
     * The updated staff information is then saved back to the system. If the update
     * is successful, an appropriate message is displayed and an admin action is logged.
     * If the update fails or an error occurs, an error message is displayed.
     * <p>
     * Exceptions are caught, and relevant error messages are displayed to the console.
     */
    private void handleUpdateStaff() {
        System.out.println(Colour.BLUE + "=== Update Staff Information ===" + Colour.RESET);
        try {
            System.out.print("Enter staff ID to update: ");
            String staffId = scanner.nextLine();

            Staff existingStaff = staffManagementService.getStaffByStaffId(staffId);
            if (existingStaff == null) {
                System.out.println(Colour.RED + "Staff ID not found." + Colour.RESET);
                return;
            }

            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                existingStaff.setName(name);
            }

            System.out.print("Enter new age (leave blank to keep current): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                existingStaff.setAge(Integer.parseInt(ageInput));
            }

            System.out.print("Enter new role (Doctor/Pharmacist, leave blank to keep current): ");
            String role = scanner.nextLine();
            if (!role.isEmpty()) {
                existingStaff.setRole(role);
            }

            boolean success = staffManagementService.updateStaff(staffId, existingStaff);
            if (success) {
                logAdminAction("Updated staff member: " + staffId);
                System.out.println(Colour.GREEN + "Staff information updated successfully!" + Colour.RESET);
            } else {
                System.out.println(Colour.RED + "Failed to update staff information." + Colour.RESET);
            }
        } catch (Exception e) {
            System.out.println(Colour.RED + "Error updating staff information: " + e.getMessage() + Colour.RESET);
        }
    }


    /**
     * Handles the removal of a staff member from the system.
     * <p>
     * This method prompts the admin to input the ID of the staff member
     * to be removed and whether the removal should be a soft delete
     * (marking the staff member as inactive) or a hard delete. It then
     * attempts to remove the staff member using the staffManagementService.
     * <p>
     * If the removal is successful, a success message is displayed and
     * the action is logged. If the removal fails, an error message is
     * displayed indicating the potential non-existence of the staff ID.
     * <p>
     * Any exceptions encountered during the process will be caught, and
     * an appropriate error message will be displayed.
     */
    private void handleRemoveStaff() {
        System.out.println(Colour.BLUE + "=== Remove Staff Member ===" + Colour.RESET);
        try {
            System.out.print("Enter staff ID to remove: ");
            String staffId = scanner.nextLine();

            System.out.print("Soft delete (mark as inactive)? (Y/N): ");
            boolean softDelete = scanner.nextLine().equalsIgnoreCase("Y");

            boolean success = staffManagementService.removeStaff(staffId, softDelete);
            if (success) {
                logAdminAction("Removed staff member: " + staffId);
                System.out.println(Colour.GREEN + "Staff member removed successfully!" + Colour.RESET);
            } else {
                System.out.println(Colour.RED + "Failed to remove staff member. Staff ID might not exist." + Colour.RESET);
            }
        } catch (Exception e) {
            System.out.println(Colour.RED + "Error removing staff member: " + e.getMessage() + Colour.RESET);
        }
    }


    /**
     * Displays the list of all staff members in a formatted table.
     * <p>
     * This method retrieves the list of all staff members from the
     * `staffManagementService` and prints the details of each staff member
     * in a tabular format to the console.
     * <p>
     * If no staff members are found, a message indicating this will be printed.
     * The output is colorized using ANSI escape codes as defined in the `Colour` class.
     * Additionally, an action log entry is created stating that the staff list was viewed.
     */
    private void handleViewStaff() {
        System.out.println(Colour.BLUE + "=== Staff List ===" + Colour.RESET);
        List<Staff> staffList = staffManagementService.listAllStaff();

        if (staffList.isEmpty()) {
            System.out.println(Colour.YELLOW + "No staff members found." + Colour.RESET);
            return;
        }

        System.out.printf("%-10s %-20s %-5s %-10s %-10s %-10s%n", "ID", "Name", "Age", "Gender", "Role", "Status");
        for (Staff staff : staffList) {
            System.out.printf("%-10s %-20s %-5d %-10s %-10s %-10s%n",
                    staff.getStaffId(), staff.getName(), staff.getAge(), staff.getGender(), staff.getRole(), staff.getStatus());
        }

        logAdminAction("Viewed staff list");
    }


    /**
     * Handles the filtering of staff based on user-selected criteria such as role, gender, or age range.
     * <p>
     * The method displays a set of filtering options to the user, prompts for input based on the chosen option,
     * and outputs a list of staff members matching the given criteria. It catches and handles invalid user input.
     */
    private void handleFilterStaff() {
        System.out.println("\n" + Colour.BLUE + "=== Filter Staff ===" + Colour.RESET);
        System.out.println("Filter by:");
        System.out.println("1. Role");
        System.out.println("2. Gender");
        System.out.println("3. Age Range");
        System.out.print("Enter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            List<Staff> filteredStaff;

            switch (choice) {
                case 1: // Filter by Role
                    System.out.print("Enter role (e.g., Doctor, Pharmacist): ");
                    String role = scanner.nextLine();
                    filteredStaff = staffManagementService.searchStaff("role", role);
                    break;

                case 2: // Filter by Gender
                    System.out.print("Enter gender (M/F): ");
                    String gender = scanner.nextLine();
                    filteredStaff = staffManagementService.searchStaff("gender", gender);
                    break;

                case 3: // Filter by Age Range
                    System.out.print("Enter minimum age: ");
                    int minAge = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter maximum age: ");
                    int maxAge = Integer.parseInt(scanner.nextLine());
                    filteredStaff = staffManagementService.searchStaff("age", "", minAge, maxAge);
                    break;

                default:
                    System.out.println(Colour.RED + "Invalid choice. Please try again." + Colour.RESET);
                    return;
            }

            // Display filtered staff
            if (filteredStaff.isEmpty()) {
                System.out.println(Colour.YELLOW + "No staff members match the given criteria." + Colour.RESET);
            } else {
                System.out.printf("%-10s %-20s %-5s %-10s %-10s %-10s%n", "ID", "Name", "Age", "Gender", "Role", "Status");
                for (Staff staff : filteredStaff) {
                    System.out.printf("%-10s %-20s %-5d %-10s %-10s %-10s%n",
                            staff.getStaffId(), staff.getName(), staff.getAge(), staff.getGender(), staff.getRole(), staff.getStatus());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input. Please enter a valid number." + Colour.RESET);
        }
    }


    /**
     * Logs an administrative action performed by the current user within the hospital management system.
     *
     * @param action a description of the administrative action performed
     */
    private void logAdminAction(String action) {
        AuditLogger.logAction(
                userContext.getName(),
                "ADMINISTRATOR",
                String.valueOf(userContext.getHospitalID()),
                action
        );
    }
}