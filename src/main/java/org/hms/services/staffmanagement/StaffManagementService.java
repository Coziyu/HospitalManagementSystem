package org.hms.services.staffmanagement;

import java.util.List;
import java.util.stream.Collectors;

public class StaffManagementService {

    private final StaffTable staffTable;

    public StaffManagementService() {
        this.staffTable = new StaffTable();
        loadFromFile(); // Optionally load from file on initialization
    }

    // Adds a new staff entry
    public boolean addStaff(Staff staff) {
        try {
            staffTable.addEntry(staff);
            staffTable.saveToFile(); // Save after adding
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Updates an existing staff entry
    public boolean updateStaff(int staffId, Staff updatedStaff) {
        Staff existingStaff = staffTable.getEntry(staffId);
        if (existingStaff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        try {
            staffTable.replaceEntry(updatedStaff);
            staffTable.saveToFile(); // Save after updating
        } catch (Exception e) {
            System.err.println("Error updating staff: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Retrieves a specific staff entry
    public Staff getStaff(int staffId) {
        return staffTable.getEntry(staffId);
    }

    // Lists all active staff entries
    public List<Staff> listAllActiveStaff() {
        return staffTable.filterByAttribute(Staff::getStatus, "active").getEntries();
    }

    // Soft or hard delete a staff entry
    public boolean removeStaff(int staffId, boolean softDelete) {
        Staff staff = getStaff(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        if (softDelete) {
            staff.setStatus("inactive");
            return updateStaff(staffId, staff);
        } else {
            try {
                staffTable.removeEntry(staffId);
                staffTable.saveToFile(); // Save after removal
            } catch (Exception e) {
                System.err.println("Error removing staff: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    // Assigns a specific role to the staff member
    public boolean assignRole(int staffId, String role) {
        Staff staff = getStaff(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        staff.setRole(role);
        return updateStaff(staffId, staff);
    }

    // Changes the status (active/inactive) of a staff member
    public boolean changeStatus(int staffId, String status) {
        Staff staff = getStaff(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        staff.setStatus(status);
        return updateStaff(staffId, staff);
    }

    // Archives a staff member’s data
    public boolean archiveStaff(int staffId) {
        return changeStatus(staffId, "archived");
    }

    // Searches for staff based on role, gender, or status
    public List<Staff> searchStaff(String criteria, String value) {
        return staffTable.getEntries().stream()
                .filter(staff -> {
                    switch (criteria.toLowerCase()) {
                        case "role":
                            return staff.getRole().equalsIgnoreCase(value);
                        case "gender":
                            return staff.getGender().equalsIgnoreCase(value);
                        case "status":
                            return staff.getStatus().equalsIgnoreCase(value);
                        default:
                            System.err.println("Invalid search criteria: " + criteria);
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // Save and load methods
    public boolean saveToFile() {
        try {
            staffTable.saveToFile();
        } catch (Exception e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean loadFromFile() {
        try {
            staffTable.loadFromFile();
        } catch (Exception e) {
            System.err.println("Error loading from file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public StaffTable getStaffTable() {
        return staffTable;
    }
}
