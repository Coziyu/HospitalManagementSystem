package org.hms.services.logging;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The AuditLogger class provides functionality for logging user actions
 * in a hospital management system. It maintains an audit log in CSV format
 * which includes details like timestamp, user name, user role, hospital ID,
 * and action performed.
 */
public class AuditLogger {
    /**
     * The root directory where audit log data files are stored.
     * This directory is located within the current user's working directory.
     */
    private static final String DATA_ROOT = System.getProperty("user.dir") + "/data/";
    /**
     * The file path where the audit log is stored.
     * This file is used to keep a record of user actions in the
     * hospital management system.
     */
    private static final String AUDIT_LOG_FILE = DATA_ROOT + "audit_log.csv";
    /**
     * Formatter for timestamps used in the audit log entries.
     * Timestamps are formatted in the pattern "yyyy-MM-dd HH:mm:ss".
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * A reentrant lock used to ensure thread-safe access to the audit log file.
     * This lock is applied whenever an action is logged to prevent concurrent modification issues.
     */
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * Initializes the audit log file by creating it if it does not exist and
     * ensuring that it is set up with the correct headers.
     * It also ensures the directory structure exists.
     * <p>
     * If the file already exists, the method does nothing.
     * <p>
     * If an IOException occurs during this process, an error message is printed to the standard error stream.
     * <p>
     * The audit log file is expected to be located at the path specified by the AUDIT_LOG_FILE constant,
     * and its directory structure will be created if it does not already exist.
     * <p>
     * The file is intended to be a CSV file with the headers:
     * "timestamp, user_name, user_role, hospital_id, action".
     */
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

    /**
     * Logs a user action to the audit log file. This method records the action performed
     * by a user in a hospital management system, along with the timestamp, user's name, user role,
     * and hospital ID.
     *
     * @param userName   the name of the user performing the action
     * @param userRole   the role of the user performing the action
     * @param hospitalId the ID of the hospital where the action is performed
     * @param action     a description of the action performed by the user
     */
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

        } catch (IOException e) {
            System.err.println("Error writing to audit log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the file path of the audit log file.
     *
     * @return The file path of the audit log as a String.
     */
    public static String getLogFilePath() {
        return AUDIT_LOG_FILE;
    }
}