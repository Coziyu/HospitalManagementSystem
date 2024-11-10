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
            System.out.println("5 - Soft Delete Staff");
            System.out.println("6 - Hard Delete Staff");
            System.out.println("7 - Assign Role");
            System.out.println("8 - Change Status");
            System.out.println("9 - Archive Staff");
            System.out.println("10 - Save to File");
            System.out.println("11 - Load from File");
            System.out.println("0 - Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter Staff ID, Age, Name, Role, Status, and Gender: ");
                    int staffId = scanner.nextInt();
                    int age = scanner.nextInt();
                    String name = scanner.next();
                    String role = scanner.next();
                    String status = scanner.next();
                    String gender = scanner.next();
                    Staff newStaff = new Staff(staffId, age, name, role, status, gender);
                    boolean addSuccess = service.addStaff(newStaff);
                    System.out.println("Add Staff: " + (addSuccess ? "Success" : "Failed"));
                    break;

                case 2:
                    System.out.print("Enter Staff ID to retrieve: ");
                    int getId = scanner.nextInt();
                    Staff retrievedStaff = service.getStaff(getId);
                    System.out.println("Get Staff: " + (retrievedStaff != null ? retrievedStaff : "Staff not found"));
                    break;

                case 3:
                    System.out.print("Enter Staff ID to update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter new Age, Name, Role, Status, and Gender: ");
                    int newAge = scanner.nextInt();
                    String newName = scanner.next();
                    String newRole = scanner.next();
                    String newStatus = scanner.next();
                    String newGender = scanner.next();
                    Staff updatedStaff = new Staff(updateId, newAge, newName, newRole, newStatus, newGender);
                    boolean updateSuccess = service.updateStaff(updateId, updatedStaff);
                    System.out.println("Update Staff: " + (updateSuccess ? "Success" : "Failed"));
                    break;

                case 4:
                    List<Staff> activeStaff = service.listAllActiveStaff();
                    System.out.println("List All Active Staff:");
                    activeStaff.forEach(System.out::println);
                    break;

                case 5:
                    System.out.print("Enter Staff ID to soft delete: ");
                    int softDeleteId = scanner.nextInt();
                    boolean softDeleteSuccess = service.removeStaff(softDeleteId, true);
                    System.out.println("Soft Delete Staff: " + (softDeleteSuccess ? "Success" : "Failed"));
                    break;

                case 6:
                    System.out.print("Enter Staff ID to hard delete: ");
                    int hardDeleteId = scanner.nextInt();
                    boolean hardDeleteSuccess = service.removeStaff(hardDeleteId, false);
                    System.out.println("Hard Delete Staff: " + (hardDeleteSuccess ? "Success" : "Failed"));
                    break;

                case 7:
                    System.out.print("Enter Staff ID to assign role: ");
                    int roleId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new Role: ");
                    String newRoleAssign = scanner.nextLine();
                    boolean roleAssignSuccess = service.assignRole(roleId, newRoleAssign);
                    System.out.println("Assign Role: " + (roleAssignSuccess ? "Success" : "Failed"));
                    break;

                case 8:
                    System.out.print("Enter Staff ID to change status: ");
                    int statusId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new Status: ");
                    String newStatusChange = scanner.nextLine();
                    boolean statusChangeSuccess = service.changeStatus(statusId, newStatusChange);
                    System.out.println("Change Status: " + (statusChangeSuccess ? "Success" : "Failed"));
                    break;

                case 9:
                    System.out.print("Enter Staff ID to archive: ");
                    int archiveId = scanner.nextInt();
                    boolean archiveSuccess = service.archiveStaff(archiveId);
                    System.out.println("Archive Staff: " + (archiveSuccess ? "Success" : "Failed"));
                    break;

                case 10:
                    boolean saveSuccess = service.saveToFile();
                    System.out.println("Save to File: " + (saveSuccess ? "Success" : "Failed"));
                    break;

                case 11:
                    boolean loadSuccess = service.loadFromFile();
                    System.out.println("Load from File: " + (loadSuccess ? "Success" : "Failed"));
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
