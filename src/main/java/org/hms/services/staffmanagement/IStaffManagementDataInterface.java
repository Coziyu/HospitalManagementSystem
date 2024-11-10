package org.hms.services.staffmanagement;

import org.hms.services.storage.IDataInterface;

import java.util.List;

public interface IStaffManagementDataInterface extends IDataInterface {
    // Method to add a new staff member
    void addStaff(Staff staff);

    // Method to update existing staff information
    void updateStaff(int staffId, Staff staff);

    // Method to get staff by ID
    Staff getStaff(int staffId);

    // Method to list all active staff members
    List<Staff> listAllStaff();

    // Method to search for staff based on specific criteria
    List<Staff> searchStaff(String criteria);

    // Method to remove a staff member (soft or hard delete)
    void removeStaff(int staffId, boolean softDelete);

    // Method to assign a role to a staff member
    void assignRole(int staffId, String role);

    // Method to change the status of a staff member
    void changeStatus(int staffId, String status);

    // Method to archive a staff member's data
    void archiveStaff(int staffId);

    // Method to get roles associated with a staff member
    List<String> getStaffRoles(int staffId);

    StaffTable getStaffTable();

}

