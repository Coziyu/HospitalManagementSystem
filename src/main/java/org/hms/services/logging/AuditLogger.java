package org.hms.services.logging;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class AuditLogger {
    private static final String DATA_ROOT = System.getProperty("user.dir") + "/data/";
    private static final String AUDIT_LOG_FILE = DATA_ROOT + "audit_log.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ReentrantLock lock = new ReentrantLock();

    private static void initializeLogFile() {
        try {
            Files.createDirectories(Paths.get(DATA_ROOT));
            if (!Files.exists(Paths.get(AUDIT_LOG_FILE))) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_LOG_FILE))) {
                    writer.println("timestamp,user_name,user_role,hospital_id,action");
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing audit log file: " + e.getMessage());
        }
    }

    public static void logAction(String userName, String userRole, String hospitalId, String action) {
        lock.lock();
        try {
            initializeLogFile();

            // Escape any commas in the action description to maintain CSV format
            action = action.replace(",", ";");

            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String logEntry = String.format("%s,%s,%s,%s,%s%n",
                    timestamp,
                    userName,
                    userRole,
                    hospitalId,
                    action
            );

            Files.write(
                    Paths.get(AUDIT_LOG_FILE),
                    logEntry.getBytes(),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE
            );

            // Also print to console for immediate feedback
            System.out.println("LOG: " + timestamp + " - " + userName + " (" + userRole + ") - " + action);

        } catch (IOException e) {
            System.err.println("Error writing to audit log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public static String getLogFilePath() {
        return AUDIT_LOG_FILE;
    }
}