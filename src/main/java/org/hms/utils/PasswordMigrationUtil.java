package org.hms.utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PasswordMigrationUtil {
    private static final String DATA_ROOT = System.getProperty("user.dir") + "/data/";
    private static final String USER_DB_FILE = DATA_ROOT + "users.csv";

    public static void main(String[] args) {
        try {
            migratePasswords();
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void migratePasswords() throws IOException {
        // Step 1: Create backup
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = USER_DB_FILE + "." + timestamp + ".backup";

        System.out.println("Creating backup of users.csv...");
        Files.copy(Paths.get(USER_DB_FILE), Paths.get(backupFile), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Backup created: " + backupFile);

        // Step 2: Read all users
        List<String[]> users = new ArrayList<>();
        boolean firstLine = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DB_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    users.add(new String[]{"id", "password", "role", "isFirstLogin"}); // Header
                    continue;
                }
                users.add(line.split(","));
            }
        }

        // Step 3: Hash passwords
        System.out.println("\nMigrating passwords...");
        for (int i = 1; i < users.size(); i++) { // Skip header
            String[] user = users.get(i);
            String oldPassword = user[1];
            String hashedPassword = PasswordUtils.hashPassword(oldPassword);
            user[1] = hashedPassword;
            System.out.println("Migrated password for user: " + user[0]);
        }

        // Step 4: Write updated data
        System.out.println("\nWriting updated data to users.csv...");
        try (PrintWriter writer = new PrintWriter(USER_DB_FILE)) {
            for (String[] user : users) {
                writer.println(String.join(",", user));
            }
        }

        System.out.println("\nMigration completed successfully!");
        System.out.println("Original file backed up to: " + backupFile);
        System.out.println("Users.csv now contains hashed passwords");
    }
}