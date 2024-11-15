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
        return new Staff(0,"", 0, "", "", "", "");
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
}
