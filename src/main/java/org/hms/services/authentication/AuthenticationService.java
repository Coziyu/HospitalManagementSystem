package org.hms.services.authentication;

import org.hms.entities.User;
import org.hms.entities.UserRole;
import org.hms.services.authentication.AuthenticationResult;
import org.hms.utils.PasswordUtils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The AuthenticationService class provides functionalities for user authentication,
 * user management, and password management.
 * This class manages user login, password changes, account locking due to failed login attempts,
 * and various user-related operations including adding, deleting, and searching users.
 */
public class AuthenticationService {
    /**
     * The root directory for storing application data files.
     * This path is dynamically set to the current working directory of the user,
     * appended with "/data/".
     */
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    /**
     * The file path for the user database, combining the root directory path with the specific filename for storing user data.
     * This constant points to a CSV file that stores user information such as ID, password, role, and first login status.
     */
    private static final String USER_DB_FILE = dataRoot + "users.csv";
    /**
     * A map that associates each UserRole with a specific ID prefix.
     * This is used in the system to generate unique IDs for users based on their role.
     * The keys of the map are UserRole values, and the values are the corresponding ID prefixes.
     * For example, the prefix for DOCTOR is "DOC", for PATIENT is "PAT", etc.
     */
    // Role-specific ID prefixes and counters
    private static final Map<UserRole, String> ID_PREFIXES = Map.of(
            UserRole.DOCTOR, "DOC",
            UserRole.PATIENT, "PAT",
            UserRole.PHARMACIST, "PHARM",
            UserRole.ADMINISTRATOR, "ADMIN"
    );
    /**
     * Tracks user login attempts and manages account lockout timing.
     * <p>
     * This variable is responsible for:
     * - Recording the number of failed login attempts for each user.
     * - Locking out accounts after a specified number of failed login attempts.
     * - Clearing the number of failed attempts and lockout status for users.
     * - Checking if an account is currently locked out.
     * - Providing information on the remaining login attempts and lockout end times.
     */
    private final LoginAttemptTracker loginAttemptTracker;
    /**
     * A map storing user data within the system.
     * The key is a unique identifier for each user, and the value is a {@link User} object
     * representing the user's information including ID, password, role, and login status.
     * This map is used for managing user accounts, handling authentication,
     * and performing various user-related operations in the {@link AuthenticationService}.
     */
    private Map<String, User> users;
    /**
     * Represents the currently authenticated user in the system.
     * This variable is set upon successful login and cleared upon logout.
     */
    private User currentUser;

    /**
     * Constructs an instance of AuthenticationService and initializes its internal state.
     * This includes setting up the user storage mechanism, tracking login attempts,
     * and loading users from a persistent storage.
     * <p>
     * The constructor performs the following operations:
     * - Initializes the users map to store user information.
     * - Sets up an instance of LoginAttemptTracker for managing login attempts and account lockouts.
     * - Calls initializeUserDatabase to set up the user database if it doesn't exist.
     * - Calls loadUsers to load existing user information from the database into memory.
     */
    public AuthenticationService() {
        users = new HashMap<>();
        loginAttemptTracker = new LoginAttemptTracker();
        initializeUserDatabase();
        loadUsers();
    }

    /**
     * Initializes the user database by creating necessary directories and files.
     * If the user database file does not exist, it will be created with a default admin account.
     * The default admin account will have a hashed password.
     * <p>
     * This method ensures that the directory specified by `dataRoot` exists,
     * and initializes the user database file with the necessary headers and a default user.
     * If the file already exists, it skips the initialization.
     * <p>
     * Handles IOException during directory and file creation.
     */
    private void initializeUserDatabase() {
        try {
            Files.createDirectories(Paths.get(dataRoot));

            if (!Files.exists(Paths.get(USER_DB_FILE))) {
                try (PrintWriter writer = new PrintWriter(USER_DB_FILE)) {
                    writer.println("id,password,role,isFirstLogin");
                    // Add default admin account with hashed password
                    String hashedPassword = PasswordUtils.hashPassword("password");
                    writer.println("ADMIN001," + hashedPassword + ",ADMINISTRATOR,true");
                }
            }
        } catch (IOException e) {
            System.out.println("Error initializing user database: " + e.getMessage());
        }
    }

    /**
     * Loads user data from a file into the system.
     * <p>
     * This method reads the user data from a predefined file and populates the
     * internal user map with user objects. Each user record in the file is expected
     * to have four fields: user ID, hashed password, role, and a flag indicating
     * if it is the user's first login.
     * <p>
     * The file format must be a comma-separated values (CSV) file with the following columns:
     * id,password,role,isFirstLogin
     * <p>
     * If a line in the file has exactly four fields, a new User object is created
     * and added to the users map. The users map uses the user ID as the key and
     * the User object as the value.
     * <p>
     * This method uses a try-with-resources statement to ensure that the file
     * reader is properly closed after use.
     * <p>
     * If an IOException occurs during the reading process, an error message
     * is printed to the console.
     *
     * @throws IOException if an I/O error occurs
     */
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DB_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    User user = new User(
                            parts[0],
                            parts[1], // Password is already hashed in the file
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

    /**
     * Persists the current state of users to the user database file.
     * This method writes user details such as id, password (hashed), role, and first login status
     * to a CSV file for storage and future retrieval. The file writing process handles I/O exceptions,
     * printing an error message if any issues occur during saving.
     */
    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(USER_DB_FILE)) {
            writer.println("id,password,role,isFirstLogin");
            for (User user : users.values()) {
                writer.printf("%s,%s,%s,%b%n",
                        user.getId(),
                        user.getPassword(), // Password is already hashed
                        user.getRole().toString(),
                        user.isFirstLogin()
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user with the provided ID and password.
     * This method performs several checks, including verifying user existence,
     * account lock status, and password correctness. If authentication is
     * successful, the user's login attempts are cleared and they are set
     * as the current user.
     *
     * @param id       The unique identifier of the user attempting to log in.
     * @param password The password provided by the user for authentication.
     * @return An {@code AuthenticationResult} object representing the outcome of the authentication attempt.
     */
    public AuthenticationResult login(String id, String password) {
        User user = users.get(id);
        if (user == null) {
            return new AuthenticationResult(false, null, "User not found");
        }

        // Check if account is locked
        if (loginAttemptTracker.isAccountLocked(id)) {
            return new AuthenticationResult(false, null,
                    "Account is locked. Try again after " +
                            loginAttemptTracker.getLockoutEndTime(id).toString());
        }

        // Hash the provided password and compare with stored hash
        String hashedPassword = PasswordUtils.hashPassword(password);
        if (!user.getPassword().equals(hashedPassword)) {
            loginAttemptTracker.recordFailedAttempt(id);
            int remainingAttempts = loginAttemptTracker.getRemainingAttempts(id);

            if (remainingAttempts > 0) {
                return new AuthenticationResult(false, null,
                        "Invalid password. " + remainingAttempts + " attempts remaining");
            } else {
                return new AuthenticationResult(false, null,
                        "Account locked due to too many failed attempts");
            }
        }

        // Successful login
        loginAttemptTracker.clearAttempts(id);
        currentUser = user;
        return new AuthenticationResult(true, user,
                user.isFirstLogin() ? "Password change required" : "Login successful");
    }

    /**
     * Changes the password for the currently logged-in user.
     *
     * @param oldPassword the current password of the user
     * @param newPassword the new password to be set
     * @return true if the password was successfully changed, false otherwise
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }

        // Verify old password
        String hashedOldPassword = PasswordUtils.hashPassword(oldPassword);
        if (!currentUser.getPassword().equals(hashedOldPassword)) {
            return false;
        }

        // Validate new password
        if (!PasswordUtils.isPasswordValid(newPassword)) {
            return false;
        }

        // Update password
        String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
        currentUser.setPassword(hashedNewPassword);
        currentUser.setFirstLogin(false);
        saveUsers();
        return true;
    }

    /**
     * Logs out the current user by setting the currentUser field to null.
     * <p>
     * This method is typically called when a user chooses to log out from the system.
     * After calling this method, no user will be considered as logged in until
     * a subsequent login operation is successfully performed.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Retrieves the current authenticated user.
     *
     * @return the current User object, or null if no user is authenticated
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Generates a unique ID for a user based on their role.
     * The ID is composed of a prefix corresponding to the user role and a numeric suffix
     * that increments from the highest existing numerical suffix for that role.
     *
     * @param role The role of the user for which the unique ID is generated.
     * @return The generated unique ID as a string.
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
                    continue;
                }
            }
        }

        return String.format("%s%03d", prefix, counter);
    }

    /**
     * Adds a new user to the system with the specified role.
     *
     * @param role the role assigned to the new user
     * @return the unique identifier of the newly added user
     */
    public String addUser(UserRole role) {
        String newId = generateUniqueId(role);
        String hashedPassword = PasswordUtils.hashPassword("password"); // Hash default password
        User newUser = new User(newId, hashedPassword, role, true);
        users.put(newId, newUser);
        saveUsers();
        return newId;
    }

    /**
     * Adds a new user with the specified ID and role.
     *
     * @param id   the unique identifier for the new user
     * @param role the role assigned to the new user
     * @return true if the user was added successfully, false if a user with the given ID already exists
     */
    public boolean addUserWithId(String id, UserRole role) {
        if (users.containsKey(id)) {
            return false;
        }
        String hashedPassword = PasswordUtils.hashPassword("password"); // Hash default password
        User newUser = new User(id, hashedPassword, role, true);
        users.put(id, newUser);
        saveUsers();
        return true;
    }

    /**
     * Retrieves a list of all users currently in the system.
     *
     * @return a List containing all User objects
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Fetches a list of users filtered by their specified role.
     *
     * @param role The role used as a filter criterion to retrieve users.
     * @return A list of users that have the specified role.
     */
    public List<User> getUsersByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of Users by their role, with an option to sort them by their ID.
     *
     * @param role     the role of the users to retrieve
     * @param sortById flag indicating whether the result should be sorted by user ID
     * @return a list of users with the specified role, optionally sorted by user ID
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
     * Searches for users whose IDs start with the given prefix.
     *
     * @param idPrefix the prefix to search for in user IDs
     * @return a list of users whose IDs start with the provided prefix
     */
    public List<User> searchUsersByIdPrefix(String idPrefix) {
        return users.values().stream()
                .filter(user -> user.getId().toLowerCase().startsWith(idPrefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a user with the specified ID exists in the user database.
     *
     * @param id the unique identifier for the user
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(String id) {
        return users.containsKey(id);
    }

    /**
     * Deletes a user identified by the given ID from the user database.
     *
     * @param id The unique identifier of the user to be deleted.
     * @return true if the user was successfully deleted, false if the user does not exist.
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
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user to retrieve
     * @return an Optional<User> containing the user if found, otherwise an empty Optional
     */
    public Optional<User> getUser(String id) {
        return Optional.ofNullable(users.get(id));
    }

    /**
     * Retrieves the total number of users currently present in the system.
     *
     * @return the total count of users
     */
    public int getUserCount() {
        return users.size();
    }

    /**
     * Retrieves the count of users that have a specific role.
     *
     * @param role the role for which to count the users
     * @return the number of users with the specified role
     */
    public long getUserCountByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .count();
    }
}