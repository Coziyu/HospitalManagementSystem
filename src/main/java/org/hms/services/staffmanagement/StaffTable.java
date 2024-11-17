package org.hms.services.staffmanagement;

import org.hms.entities.AbstractTable;
import org.hms.entities.User;
import org.hms.entities.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The StaffTable class extends the AbstractTable class to manage staff data,
 * specifically providing functionality to save to and load from a CSV file.
 * It uses a default file path, and each entry represents a staff member
 * with fields such as staffId, age, name, role, status, and gender.
 */
public class StaffTable extends AbstractTable<Staff> {


    /**
     * The root directory where all data files are stored. This path is constructed
     * using the current user directory and appending "/data/". It is used as a
     * base directory for loading and saving data files specific to the StaffTable.
     */
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    /**
     * A constant that holds the file path for the users' CSV file.
     * The file path is constructed by appending "users.csv" to the data root directory.
     */
    private static final String USERS_FILE = dataRoot + "users.csv";

    /**
     * Default constructor for the StaffTable class.
     * Initializes a new instance by invoking the superclass's constructor
     * to set up necessary fields and structures for managing staff entries.
     */
    public StaffTable() {
        super();
    }

    /**
     * Constructor for the StaffTable class.
     *
     * @param filePath the file path where the staff data will be stored or loaded from
     */
    public StaffTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    /**
     * Parses a string representation of a user role and returns the corresponding UserRole enum.
     *
     * @param role the string representation of the user role
     * @return the UserRole enum corresponding to the given role
     * @throws IllegalArgumentException if the provided role does not correspond to any UserRole
     */
    private static UserRole parseUserRole(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    /**
     * Retrieves the headers for the staff table.
     *
     * @return an array of strings representing the headers for the staff table
     */
    @Override
    protected String[] getHeaders() {
        return new String[]{"staffId", "age", "name", "role", "status", "gender"};
    }

    /**
     * Creates a valid template for a new Staff entry with default values.
     *
     * @return A new Staff object with a unique ID and default values for all other fields.
     */
    @Override
    protected Staff createValidEntryTemplate() {
        return new Staff(getUnusedID(), "", 0, "", "", "", "");
    }

    /**
     * Creates an empty StaffTable instance.
     *
     * @return a new instance of StaffTable.
     */
    @Override
    protected AbstractTable<Staff> createEmpty() {
        return new StaffTable();
    }

    /**
     * Saves the StaffTable to the designated file.
     * <p>
     * This method overrides the superclass method to save the current state of the staff table,
     * including headers and individual staff entries, to a CSV file. The file location is determined
     * by the class's filePath property.
     *
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void saveToFile() throws IOException {
        super.saveToFile();
    }

    /**
     * Loads the staff entries from a predefined file. This method overrides the
     * parent class's implementation to load staff data specifically from the designated file.
     *
     * @throws IOException if an I/O error occurs during reading from the file
     */
    public void loadFromFile() throws IOException {
        super.loadFromFile();
    }

    /**
     * Loads user data from the USERS_FILE into a Map.
     * Each user is represented as a line in the file, with fields separated by commas.
     * The first line of the file is assumed to be a header and is skipped.
     * If a line contains exactly 4 fields, it is parsed into a User object and added to the map.
     *
     * @return a Map where the key is the user ID and the value is the corresponding User object
     */
    private Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    User user = new User(
                            parts[0],
                            parts[1],
                            UserRole.valueOf(parts[2].toUpperCase()),
                            Boolean.parseBoolean(parts[3])
                    );
                    users.put(user.getId(), user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        return users;
    }

    /**
     * Saves the provided users to a file.
     *
     * @param users a map containing users to be saved, where the key is the user's ID and the value is the User object
     */
    private void saveUsers(Map<String, User> users) {
        try (PrintWriter writer = new PrintWriter(USERS_FILE)) {
            writer.println("id,password,role,isFirstLogin");
            for (User user : users.values()) {
                writer.printf("%s,%s,%s,%b%n",
                        user.getId(),
                        user.getPassword(),
                        user.getRole().toString(),
                        user.isFirstLogin()
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Adds a new staff entry to the staff table and updates the associated user information.
     *
     * @param staff The Staff object to be added.
     */
    @Override
    public void addEntry(Staff staff) {
        try {
            super.addEntry(staff);
            saveToFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, User> users = loadUsers();
        users.put(staff.getStaffId(), new User(staff.getStaffId(), "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", parseUserRole(staff.getRole()), true));
        saveUsers(users);

    }

    /**
     * Removes an entry with the specified tableEntryID from the staff table.
     * This method will update the underlying data file and remove the associated user.
     *
     * @param tableEntryID the unique identifier of the table entry to be removed
     * @return true if the entry is successfully removed, false if the entry with the specified ID is not found
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
                saveToFile(); // Ensure users.csv is updated

                Map<String, User> users = loadUsers();
                users.remove(staff.getStaffId());
                saveUsers(users);

                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false; // Return false if staff not found
    }

}
