package org.hms.services.staffmanagement;

import java.util.List;
import java.util.Scanner;

public class StaffManagementServiceTestWithSwitch {

    public static void main(String[] args) {
        // Initialize StaffManagementService with StaffTable backed by staff.csv
        StaffManagementService service = new StaffManagementService();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nSelect a test to run:");
            System.out.println("1 - Add Staff");
            System.out.println("2 - Get Staff");
            System.out.println("3 - Update Staff");
            System.out.println("4 - List All Active Staff");
            System.out.println("5 - List All Staff"); // New option for listing all staff
            System.out.println("6 - Soft Delete Staff");
            System.out.println("7 - Hard Delete Staff");
            System.out.println("8 - Assign Role");
            System.out.println("9 - Change Status");
            System.out.println("10 - Archive Staff");
            System.out.println("11 - Save to File");
            System.out.println("12 - Load from File");
            System.out.println("13 - Search Staff");
            System.out.println("0 - Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter Staff ID (String), Age, Name, Role, Status, and Gender: ");
                    String staffId = scanner.nextLine();  // Change to String input for staffId
                    int age = scanner.nextInt();
                    String name = scanner.next();
                    String role = scanner.next();
                    String status = scanner.next();
                    String gender = scanner.next();
                    Staff newStaff = new Staff(staffId, age, name, role, status, gender);  // Pass staffId as String
                    boolean addSuccess = service.addStaff(newStaff);
                    System.out.println("Add Staff: " + (addSuccess ? "Success" : "Failed"));
                    break;

                case 2:
                    System.out.print("Enter Staff ID (String) to retrieve: ");
                    String getId = scanner.nextLine();  // Change to String input for staffId
                    Staff retrievedStaff = service.getStaff(getId);  // Pass staffId as String
                    System.out.println("Get Staff: " + (retrievedStaff != null ? retrievedStaff : "Staff not found"));
                    break;

                case 3:
                    System.out.print("Enter Staff ID (String) to update: ");
                    String updateId = scanner.nextLine();  // Change to String input for staffId
                    System.out.print("Enter new Age, Name, Role, Status, and Gender: ");
                    int newAge = scanner.nextInt();
                    String newName = scanner.next();
                    String newRole = scanner.next();
                    String newStatus = scanner.next();
                    String newGender = scanner.next();
                    Staff updatedStaff = new Staff(updateId, newAge, newName, newRole, newStatus, newGender);  // Pass staffId as String
                    boolean updateSuccess = service.updateStaff(updateId, updatedStaff);  // Pass staffId as String
                    System.out.println("Update Staff: " + (updateSuccess ? "Success" : "Failed"));
                    break;

                case 4:
                    List<Staff> activeStaff = service.listAllActiveStaff();
                    System.out.println("List All Active Staff:");
                    activeStaff.forEach(System.out::println);
                    break;

                case 5: // New case for listing all staff
                    List<Staff> allStaff = service.listAllStaff();
                    System.out.println("List All Staff:");
                    allStaff.forEach(System.out::println);
                    break;

                case 6:
                    System.out.print("Enter Staff ID (String) to soft delete: ");
                    String softDeleteId = scanner.nextLine();  // Change to String input for staffId
                    boolean softDeleteSuccess = service.removeStaff(softDeleteId, true);  // Pass staffId as String
                    System.out.println("Soft Delete Staff: " + (softDeleteSuccess ? "Success" : "Failed"));
                    break;

                case 7:
                    System.out.print("Enter Staff ID (String) to hard delete: ");
                    String hardDeleteId = scanner.nextLine();  // Change to String input for staffId
                    boolean hardDeleteSuccess = service.removeStaff(hardDeleteId, false);  // Pass staffId as String
                    System.out.println("Hard Delete Staff: " + (hardDeleteSuccess ? "Success" : "Failed"));
                    break;

                case 8:
                    System.out.print("Enter Staff ID (String) to assign role: ");
                    String roleId = scanner.nextLine();  // Change to String input for staffId
                    System.out.print("Enter new Role: ");
                    String newRoleAssign = scanner.nextLine();
                    boolean roleAssignSuccess = service.assignRole(roleId, newRoleAssign);  // Pass staffId as String
                    System.out.println("Assign Role: " + (roleAssignSuccess ? "Success" : "Failed"));
                    break;

                case 9:
                    System.out.print("Enter Staff ID (String) to change status: ");
                    String statusId = scanner.nextLine();  // Change to String input for staffId
                    System.out.print("Enter new Status: ");
                    String newStatusChange = scanner.nextLine();
                    boolean statusChangeSuccess = service.changeStatus(statusId, newStatusChange);  // Pass staffId as String
                    System.out.println("Change Status: " + (statusChangeSuccess ? "Success" : "Failed"));
                    break;

                case 10:
                    System.out.print("Enter Staff ID (String) to archive: ");
                    String archiveId = scanner.nextLine();  // Change to String input for staffId
                    boolean archiveSuccess = service.archiveStaff(archiveId);  // Pass staffId as String
                    System.out.println("Archive Staff: " + (archiveSuccess ? "Success" : "Failed"));
                    break;

                case 11:
                    boolean saveSuccess = service.saveToFile();
                    System.out.println("Save to File: " + (saveSuccess ? "Success" : "Failed"));
                    break;

                case 12:
                    boolean loadSuccess = service.loadFromFile();
                    System.out.println("Load from File: " + (loadSuccess ? "Success" : "Failed"));
                    break;

                case 13:
                    System.out.print("Enter search criteria (role, gender, status): ");
                    String criteria = scanner.nextLine().toLowerCase();
                    System.out.print("Enter value to search for: ");
                    String value = scanner.nextLine();
                    List<Staff> searchResults = service.searchStaff(criteria, value);
                    System.out.println("Search Results:");
                    if (searchResults.isEmpty()) {
                        System.out.println("No matching staff found.");
                    } else {
                        searchResults.forEach(System.out::println);
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
