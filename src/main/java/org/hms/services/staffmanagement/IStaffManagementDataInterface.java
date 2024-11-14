package org.hms.services.staffmanagement;

import org.hms.services.storage.IDataInterface;
import java.util.List;

/**
 * IStaffManagementDataInterface defines the methods required for managing staff data,
 * including adding, updating, retrieving, and searching for staff members.
 */
public interface IStaffManagementDataInterface extends IDataInterface {

    /**
     * Adds a new staff member.
     *
     * @param staff the Staff object representing the new staff member
     */
    void addStaff(Staff staff);

    /**
     * Updates existing staff information.
     *
     * @param staffId the ID of the staff member to update
     * @param staff the updated Staff object
     */
    void updateStaff(int staffId, Staff staff);

    /**
     * Retrieves a staff member by their ID.
     *
     * @param staffId the ID of the staff member to retrieve
     * @return the Staff object if found, otherwise null
     */
    Staff getStaff(int staffId);

    /**
     * Lists all active staff members.
     *
     * @return a List of all active Staff objects
     */
    List<Staff> listAllStaff();

    /**
     * Searches for staff members based on a specific criterion.
     *
     * @param criteria the search criteria (e.g., role, gender, status)
     * @return a List of Staff objects matching the search criteria
     */
    List<Staff> searchStaff(String criteria);

    /**
     * Removes a staff member, with an option for soft or hard deletion.
     *
     * @param staffId the ID of the staff member to remove
     * @param softDelete true for a soft delete (mark as inactive), false for a hard delete
     */
    void removeStaff(int staffId, boolean softDelete);

    /**
     * Assigns a role to a staff member.
     *
     * @param staffId the ID of the staff member
     * @param role the role to be assigned to the staff member
     */
    void assignRole(int staffId, String role);

    /**
     * Changes the status (e.g., active, inactive, archived) of a staff member.
     *
     * @param staffId the ID of the staff member
     * @param status the new status to assign to the staff member
     */
    void changeStatus(int staffId, String status);

    /**
     * Archives a staff member's data by marking them with an "archived" status.
     *
     * @param staffId the ID of the staff member to archive
     */
    void archiveStaff(int staffId);

    /**
     * Retrieves a list of roles associated with a specific staff member.
     *
     * @param staffId the ID of the staff member
     * @return a List of role names associated with the staff member
     */
    List<String> getStaffRoles(int staffId);

    /**
     * Retrieves the StaffTable instance used for managing staff data.
     *
     * @return the StaffTable instance
     */
    StaffTable getStaffTable();
}
