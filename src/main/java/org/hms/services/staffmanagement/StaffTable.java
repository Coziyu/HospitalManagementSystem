package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The StaffTable class extends the AbstractTable class to manage staff data,
 * specifically providing functionality to save to and load from a CSV file.
 * It uses a default file path, and each entry represents a staff member
 * with fields such as staffId, age, name, role, status, and gender.
 */
public class StaffTable extends AbstractTable<Staff> {


    public StaffTable() {
        super();
    }

    public StaffTable(String filePath){
        super();
        this.filePath = filePath;
    }

    /**
     * The root directory for data storage.
     */
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";

    /**
     * The default filenames for staff and user data.
     */
    private static final String USERS_FILE = dataRoot + "users.csv";

    /**
     * Provides the headers for the staff table.
     *
     * @return an array of header names, representing the attributes of a staff entry.
     */
    @Override
    protected String[] getHeaders() {
        return new String[]{"staffId", "age", "name", "role", "status", "gender"};
    }

    /**
     * Creates a template for a valid staff entry with default values.
     *
     * @return a Staff object with default values.
     */
    @Override
    protected Staff createValidEntryTemplate() {
        return new Staff(getUnusedID(), "", 0, "", "", "", "");
    }

    /**
     * Creates an empty instance of StaffTable.
     *
     * @return a new StaffTable instance.
     */
    @Override
    protected AbstractTable<Staff> createEmpty() {
        return new StaffTable();
    }

    /**
     * Saves the staff data to the default file and updates users.csv.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void saveToFile() throws IOException {
        super.saveToFile();
        syncUsersFile();
    }

    /**
     * Loads the staff data from the default file.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void loadFromFile() throws IOException {
        super.loadFromFile();
        syncUsersFile();
    }

    /**
     * Synchronizes the users.csv file with staff.csv by matching the id and staffId columns.
     */
    private void syncUsersFile() {
        List<String> userLines = new ArrayList<>();
        userLines.add("id,password,role,isFirstLogin"); // Add headers

        for (Staff staff : getEntries()) {
            userLines.add(String.format("%s,%s,%s,%s",
                    staff.getStaffId(),
                    "password",
                    staff.getRole(),
                    "true")); // Default isFirstLogin to true
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (String line : userLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error synchronizing users.csv: " + e.getMessage());
        }
    }

    /**
     * Adds a new entry to the staff table and updates users.csv.
     *
     * @param staff the Staff object to add
     */
    @Override
    public void addEntry(Staff staff) {
        try {
            super.addEntry(staff);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        syncUsersFile();
    }

    /**
     * Removes an entry from the staff table and users.csv.
     *
     * @param tableEntryID the numeric ID of the entry to remove
     */
    @Override
    public boolean removeEntry(int tableEntryID) {
        Staff staff = getEntries().stream()
                .filter(entry -> entry.getTableEntryID() == tableEntryID)
                .findFirst()
                .orElse(null);

        if (staff != null) {
            try {
                super.removeEntry(tableEntryID); // Call parent class method
                syncUsersFile(); // Ensure users.csv is updated
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false; // Return false if staff not found
    }

}
