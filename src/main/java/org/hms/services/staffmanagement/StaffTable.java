package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTable;
import java.io.IOException;

/**
 * The StaffTable class extends the AbstractTable class to manage staff data,
 * specifically providing functionality to save to and load from a CSV file.
 * It uses a default file path, and each entry represents a staff member
 * with fields such as staffId, age, name, role, status, and gender.
 */
public class StaffTable extends AbstractTable<Staff> {

    /**
     * The root directory for data storage.
     */
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";

    /**
     * The default filename for storing staff data.
     */
    private static final String DEFAULT_FILENAME = dataRoot + "staff.csv";

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
        return new Staff("", 0, "", "", "", "");
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
     * Saves the staff data to the default file.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void saveToFile() throws IOException {
        super.saveToFile(DEFAULT_FILENAME);
    }

    /**
     * Loads the staff data from the default file.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void loadFromFile() throws IOException {
        super.loadFromFile(DEFAULT_FILENAME);
    }

    /**
     * Retrieves a staff entry based on a String ID, converting it to an int for compatibility.
     *
     * @param id the ID of the staff entry as a String
     * @return the Staff object if found, otherwise null
     */
    public Staff getEntry(String id) {
        try {
            int numericId = Integer.parseInt(id);
            return super.getEntry(numericId); // Call the existing int-based getEntry method
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving entry: " + e.getMessage());
            return null;
        }
    }

    /**
     * Removes a staff entry based on a String ID, converting it to an int for compatibility.
     *
     * @param id the ID of the staff entry as a String
     * @return true if the removal was successful, false otherwise
     */
    public boolean removeEntry(String id) {
        try {
            int numericId = Integer.parseInt(id);
            return super.removeEntry(numericId); // Call the existing int-based removeEntry method
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format: " + id);
            return false;
        } catch (Exception e) {
            System.err.println("Error removing entry: " + e.getMessage());
            return false;
        }
    }
}
