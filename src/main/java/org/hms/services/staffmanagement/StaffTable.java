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
    }

    /**
     * Loads the staff data from the default file.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void loadFromFile() throws IOException {
        super.loadFromFile();
    }

    /**
     * Loads user data from users.csv into the users map.
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
     * Saves user data to users.csv
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
     * Adds a new entry to the staff table and updates users.csv.
     *
     * @param staff the Staff object to add
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
     * Converts a string representation of a user role to a UserRole enum value.
     *
     * @param role the string representation of the user role
     * @return the corresponding UserRole enum value
     */
    private static UserRole parseUserRole(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
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
