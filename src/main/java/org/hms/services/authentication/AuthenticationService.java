package org.hms.services.authentication;

import org.hms.entities.User;
import org.hms.entities.UserRole;
import org.hms.services.authentication.AuthenticationResult;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthenticationService {
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    private static final String USER_DB_FILE = dataRoot + "users.csv";
    private Map<String, User> users;
    private User currentUser;

    // Role-specific ID prefixes and counters
    private static final Map<UserRole, String> ID_PREFIXES = Map.of(
            UserRole.DOCTOR, "DOC",
            UserRole.PATIENT, "PAT",
            UserRole.PHARMACIST, "PHARM",
            UserRole.ADMINISTRATOR, "ADMIN"
    );

    public AuthenticationService() {
        users = new HashMap<>();
        initializeUserDatabase();
        loadUsers();
    }

    private void initializeUserDatabase() {
        try {
            Files.createDirectories(Paths.get(dataRoot));

            if (!Files.exists(Paths.get(USER_DB_FILE))) {
                try (PrintWriter writer = new PrintWriter(USER_DB_FILE)) {
                    writer.println("id,password,role,isFirstLogin");
                    // Add default admin account for first-time setup
                    writer.println("ADMIN001,password,ADMINISTRATOR,true");
                }
            }
        } catch (IOException e) {
            System.out.println("Error initializing user database: " + e.getMessage());
        }
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DB_FILE))) {
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
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(USER_DB_FILE)) {
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

    public AuthenticationResult login(String id, String password) {
        User user = users.get(id);
        if (user == null) {
            return new AuthenticationResult(false, null, "User not found");
        }

        if (!user.getPassword().equals(password)) {
            return new AuthenticationResult(false, null, "Invalid password");
        }

        currentUser = user;
        return new AuthenticationResult(true, user, user.isFirstLogin() ? "Password change required" : "Login successful");
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }

        if (!currentUser.getPassword().equals(oldPassword)) {
            return false;
        }

        currentUser.setPassword(newPassword);
        currentUser.setFirstLogin(false);
        saveUsers();
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Generates a new unique ID for a user based on their role
     * @param role The role of the new user
     * @return A unique ID string
     */
    private String generateUniqueId(UserRole role) {
        String prefix = ID_PREFIXES.get(role);
        int counter = 1;

        // Find the highest existing number for this role
        for (String existingId : users.keySet()) {
            if (existingId.startsWith(prefix)) {
                try {
                    int existingNumber = Integer.parseInt(existingId.substring(prefix.length()));
                    counter = Math.max(counter, existingNumber + 1);
                } catch (NumberFormatException e) {
                    // Skip if the number part isn't properly formatted
                    continue;
                }
            }
        }

        // Format: PREFIX + padded number (e.g., DOC001)
        return String.format("%s%03d", prefix, counter);
    }

    /**
     * Adds a new user to the system with an automatically generated ID
     * @param role The role of the new user
     * @return The generated user ID
     */
    public String addUser(UserRole role) {
        String newId = generateUniqueId(role);
        User newUser = new User(newId, "password", role, true);
        users.put(newId, newUser);
        saveUsers();
        return newId;
    }

    /**
     * Attempts to add a user with a specific ID
     * @param id Desired user ID
     * @param role User role
     * @return true if successful, false if ID already exists
     */
    public boolean addUserWithId(String id, UserRole role) {
        if (users.containsKey(id)) {
            return false;
        }
        User newUser = new User(id, "password", role, true);
        users.put(id, newUser);
        saveUsers();
        return true;
    }

    /**
     * Gets all users in the system
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Gets users filtered by role
     * @param role Role to filter by
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    /**
     * Gets users filtered by role and sorted by ID
     * @param role Role to filter by
     * @param sortById Whether to sort by ID
     * @return List of filtered and optionally sorted users
     */
    public List<User> getUsersByRole(UserRole role, boolean sortById) {
        Stream<User> userStream = users.values().stream()
                .filter(user -> user.getRole() == role);

        if (sortById) {
            userStream = userStream.sorted(Comparator.comparing(User::getId));
        }

        return userStream.collect(Collectors.toList());
    }

    /**
     * Searches for users based on ID prefix
     * @param idPrefix The prefix to search for
     * @return List of users whose IDs start with the given prefix
     */
    public List<User> searchUsersByIdPrefix(String idPrefix) {
        return users.values().stream()
                .filter(user -> user.getId().toLowerCase().startsWith(idPrefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a user ID already exists
     * @param id The ID to check
     * @return true if the ID exists, false otherwise
     */
    public boolean userExists(String id) {
        return users.containsKey(id);
    }

    /**
     * Deletes a user from the system
     * @param id The ID of the user to delete
     * @return true if user was deleted, false if user was not found
     */
    public boolean deleteUser(String id) {
        if (!users.containsKey(id)) {
            return false;
        }
        users.remove(id);
        saveUsers();
        return true;
    }

    /**
     * Gets a user by their ID
     * @param id The ID of the user to get
     * @return Optional containing the user if found, empty otherwise
     */
    public Optional<User> getUser(String id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Returns total count of users in the system
     * @return Total number of users
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Returns count of users by role
     * @param role Role to count
     * @return Number of users with the specified role
     */
    public long getUserCountByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .count();
    }
}