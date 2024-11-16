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

public class AuthenticationService {
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    private static final String USER_DB_FILE = dataRoot + "users.csv";
    private Map<String, User> users;
    private User currentUser;
    private final LoginAttemptTracker loginAttemptTracker;

    // Role-specific ID prefixes and counters
    private static final Map<UserRole, String> ID_PREFIXES = Map.of(
            UserRole.DOCTOR, "DOC",
            UserRole.PATIENT, "PAT",
            UserRole.PHARMACIST, "PHARM",
            UserRole.ADMINISTRATOR, "ADMIN"
    );

    public AuthenticationService() {
        users = new HashMap<>();
        loginAttemptTracker = new LoginAttemptTracker();
        initializeUserDatabase();
        loadUsers();
    }

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

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

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

    public String addUser(UserRole role) {
        String newId = generateUniqueId(role);
        String hashedPassword = PasswordUtils.hashPassword("password"); // Hash default password
        User newUser = new User(newId, hashedPassword, role, true);
        users.put(newId, newUser);
        saveUsers();
        return newId;
    }

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

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<User> getUsersByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }

    public List<User> getUsersByRole(UserRole role, boolean sortById) {
        Stream<User> userStream = users.values().stream()
                .filter(user -> user.getRole() == role);

        if (sortById) {
            userStream = userStream.sorted(Comparator.comparing(User::getId));
        }

        return userStream.collect(Collectors.toList());
    }

    public List<User> searchUsersByIdPrefix(String idPrefix) {
        return users.values().stream()
                .filter(user -> user.getId().toLowerCase().startsWith(idPrefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean userExists(String id) {
        return users.containsKey(id);
    }

    public boolean deleteUser(String id) {
        if (!users.containsKey(id)) {
            return false;
        }
        users.remove(id);
        saveUsers();
        return true;
    }

    public Optional<User> getUser(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public int getUserCount() {
        return users.size();
    }

    public long getUserCountByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .count();
    }
}