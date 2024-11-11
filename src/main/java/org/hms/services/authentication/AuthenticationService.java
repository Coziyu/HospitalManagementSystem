package org.hms.services.authentication;

import org.hms.entities.User;
import org.hms.entities.UserRole;
import org.hms.services.authentication.AuthenticationResult;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AuthenticationService {
    private static final String USER_DB_FILE = "users.csv";
    private Map<String, User> users;
    private User currentUser;

    public AuthenticationService() {
        users = new HashMap<>();
        loadUsers();
    }

    private void loadUsers() {
        try {
            if (!Files.exists(Paths.get(USER_DB_FILE))) {
                try (PrintWriter writer = new PrintWriter(USER_DB_FILE)) {
                    writer.println("id,password,role,isFirstLogin");
                }
                return;
            }

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

    public void addUser(String id, UserRole role) {
        User newUser = new User(id, "password", role, true);
        users.put(id, newUser);
        saveUsers();
    }
}