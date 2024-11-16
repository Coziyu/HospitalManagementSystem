package org.hms.services.staffmanagement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StaffManagementService provides a set of operations for managing staff
 * members, including adding, updating, removing, searching, and changing
 * staff details and roles.
 */
public class StaffManagementService {

    private final StaffTable staffTable;

    /**
     * Initializes the StaffManagementService with a StaffTable instance and
     * optionally loads data from a file.
     */
    public StaffManagementService() {
        this.staffTable = new StaffTable();
        loadFromFile(); // Optionally load from file on initialization
    }

    /**
     * Adds a new staff entry to the staff table and saves the changes to a file.
     *
     * @param staff the Staff object to be added
     * @return true if the staff was added successfully, false otherwise
     */
    public boolean addStaff(Staff staff) {
        if (staffTable.getEntries().stream()
                .anyMatch(existingStaff -> existingStaff.getStaffId().equals(staff.getStaffId()))) {
            System.err.println("Error adding staff: Duplicate staffId " + staff.getStaffId());
            return false;
        }
        try {
            staffTable.addEntry(staff);
            staffTable.saveToFile();
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * Updates an existing staff entry in the staff table and saves the changes to a file.
     *
     * @param staffId the alphanumerical ID of the staff to update
     * @param updatedStaff the updated Staff object
     * @return true if the update was successful, false if the staff was not found
     */
    public boolean updateStaff(String staffId, Staff updatedStaff) {
        Staff existingStaff = getStaffByStaffId(staffId);
        if (existingStaff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        try {
            // Retain the existing numeric ID to ensure compatibility
            updatedStaff.setTableEntryID(existingStaff.getTableEntryID());

            // Replace the existing entry directly
            if (!staffTable.replaceEntry(updatedStaff)) {
                System.err.println("Error updating staff: Entry not found for replacement.");
                return false;
            }

            // Save changes to the file
            staffTable.saveToFile();
        } catch (Exception e) {
            System.err.println("Error updating staff: " + e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * Retrieves a specific staff entry based on alphanumerical staff ID.
     *
     * @param staffId the alphanumerical ID of the staff to retrieve
     * @return the Staff object if found, otherwise null
     */
    public Staff getStaffByStaffId(String staffId) {
        return staffTable.getEntries().stream()
                .filter(staff -> staff.getStaffId().equals(staffId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lists all active staff entries.
     *
     * @return a List of Staff objects with a status of "active"
     */
    public List<Staff> listAllActiveStaff() {
        return staffTable.getEntries().stream()
                .filter(staff -> "active".equalsIgnoreCase(staff.getStatus())) // Case-insensitive comparison
                .collect(Collectors.toList());
    }

    /**
     * Lists all staff entries without filtering.
     *
     * @return a List of all Staff objects in the staff table
     */
    public List<Staff> listAllStaff() {
        return staffTable.getEntries();
    }

    /**
     * Removes a staff entry based on alphanumerical staff ID. Supports both soft and hard deletion.
     *
     * @param staffId the alphanumerical ID of the staff to remove
     * @param softDelete true for soft delete (mark as inactive), false for hard delete
     * @return true if the removal was successful, false otherwise
     */
    public boolean removeStaff(String staffId, boolean softDelete) {
        Staff staff = getStaffByStaffId(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        if (softDelete) {
            staff.setStatus("inactive");
            return updateStaff(staffId, staff);
        } else {
            try {
                staffTable.removeEntry(staff.getTableEntryID()); // Use numeric ID to remove
                staffTable.saveToFile(); // Save after removal
            } catch (Exception e) {
                System.err.println("Error removing staff: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Assigns a specific role to the staff member.
     *
     * @param staffId the alphanumerical ID of the staff to assign the role to
     * @param role the role to be assigned to the staff member
     * @return true if the role was assigned successfully, false otherwise
     */
    public boolean assignRole(String staffId, String role) {
        Staff staff = getStaffByStaffId(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        staff.setRole(role);
        return updateStaff(staffId, staff);
    }

    /**
     * Changes the status (e.g., active/inactive) of a staff member.
     *
     * @param staffId the alphanumerical ID of the staff to change the status
     * @param status the new status to be assigned to the staff member
     * @return true if the status was changed successfully, false otherwise
     */
    public boolean changeStatus(String staffId, String status) {
        Staff staff = getStaffByStaffId(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        staff.setStatus(status);
        return updateStaff(staffId, staff);
    }

    /**
     * Archives a staff member’s data by changing their status to "archived".
     *
     * @param staffId the alphanumerical ID of the staff to archive
     * @return true if the archiving was successful, false otherwise
     */
    public boolean archiveStaff(String staffId) {
        return changeStatus(staffId, "archived");
    }

    /**
     * Searches for staff based on a specified criterion such as role, gender, or status.
     *
     * @param criteria the criterion to filter by ("role", "gender", or "status")
     * @param value the value to match for the specified criterion
     * @return a List of Staff objects that match the search criteria
     */
    public List<Staff> searchStaff(String criteria, String value, int minAge, int maxAge) {
        return staffTable.getEntries().stream()
                .filter(staff -> {
                    switch (criteria.toLowerCase()) {
                        case "role":
                            return staff.getRole().equalsIgnoreCase(value);
                        case "gender":
                            return staff.getGender().equalsIgnoreCase(value);
                        case "status":
                            return staff.getStatus().equalsIgnoreCase(value);
                        case "age":
                            return staff.getAge() >= minAge && staff.getAge() <= maxAge;
                        default:
                            System.err.println("Invalid search criteria: " + criteria);
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Overloaded searchStaff method for searching without age range filtering.
     *
     * @param criteria the criterion to filter by ("role", "gender", or "status")
     * @param value the value to match for the specified criterion
     * @return a List of Staff objects that match the search criteria
     */
    public List<Staff> searchStaff(String criteria, String value) {
        // Pass default age range (no filtering) to the main method
        return searchStaff(criteria, value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    /**
     * Saves the current staff data to a file.
     *
     * @return true if the save operation was successful, false otherwise
     */
    public boolean saveToFile() {
        try {
            staffTable.saveToFile();
        } catch (Exception e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Loads staff data from a file.
     *
     * @return true if the load operation was successful, false otherwise
     */
    public boolean loadFromFile() {
        try {
            staffTable.loadFromFile();
        } catch (Exception e) {
            System.err.println("Error loading from file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Retrieves the StaffTable instance used by this service.
     *
     * @return the StaffTable instance
     */
    public StaffTable getStaffTable() {
        return staffTable;
    }
}
