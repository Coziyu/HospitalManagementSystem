package org.hms.services.staffmanagement;

import org.hms.services.AbstractService;

import java.util.ArrayList;
import java.util.List;

public class StaffManagementService extends AbstractService<IStaffManagementDataInterface> {

    private final StaffTable staffTable;

    public StaffManagementService(IStaffManagementDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        staffTable = storageServiceInterface.getStaffTable();
    }

    // Adds a new staff entry
    boolean addStaff(Staff staff) {
        try {
            staffTable.addEntry(staff);
        } catch (Exception e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Updates an existing staff entry
    boolean updateStaff(int staffId, Staff updatedStaff) {
        Staff existingStaff = staffTable.getEntry(staffId);
        if (existingStaff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        try {
            staffTable.replaceEntry(updatedStaff);
        } catch (Exception e) {
            System.err.println("Error updating staff: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Retrieves a specific staff entry
    Staff getStaff(int staffId) {
        return staffTable.getEntry(staffId);
    }

    // Lists all active staff entries
    List<Staff> listAllActiveStaff() {
        List<Staff> activeStaff = new ArrayList<>();
        for (Staff staff : staffTable.getEntries()) {
            if ("active".equalsIgnoreCase(staff.getStatus())) {
                activeStaff.add(staff);
            }
        }
        return activeStaff;
    }

    // Soft or hard delete a staff entry
    boolean removeStaff(int staffId, boolean softDelete) {
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
            } catch (Exception e) {
                System.err.println("Error removing staff: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    // Assigns a specific role to the staff member
    boolean assignRole(int staffId, String role) {
        Staff staff = getStaff(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        staff.setRole(role);
        return updateStaff(staffId, staff);
    }

    // Changes the status (active/inactive) of a staff member
    boolean changeStatus(int staffId, String status) {
        Staff staff = getStaff(staffId);
        if (staff == null) {
            System.err.println("Staff ID not found.");
            return false;
        }
        staff.setStatus(status);
        return updateStaff(staffId, staff);
    }

    // Archives a staff member’s data
    boolean archiveStaff(int staffId) {
        return changeStatus(staffId, "archived");
    }

    // Save the staff table to a CSV file
    boolean saveToFile() {
        try {
            staffTable.saveToFile("staff.csv");
        } catch (Exception e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Load the staff table from a CSV file
    boolean loadFromFile() {
        try {
            staffTable.loadFromFile("staff.csv");
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
