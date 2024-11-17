package org.hms.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Utility class for password-related operations including hashing and validation.
 */
public class PasswordUtils {
    /**
     * A compiled regular expression pattern to check if a string contains
     * at least one uppercase letter.
     */
    private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");
    /**
     * Pattern to check for the presence of at least one lowercase letter in a string.
     */
    private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
    /**
     * Compiled regular expression pattern to match any digit (0-9).
     * Useful for validating passwords to ensure they contain at least one number.
     */
    private static final Pattern HAS_NUMBER = Pattern.compile("\\d");
    /**
     * A compiled regular expression pattern that matches any special character.
     * Special characters include: !@#$%^&*(),.?":{}|<>
     */
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    /**
     * Hashes the given password using SHA-256 algorithm.
     *
     * @param password The plain text password to be hashed.
     * @return The hashed password represented as a hexadecimal string.
     * @throws RuntimeException If the SHA-256 algorithm is not available.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Validates that a given password meets the required criteria.
     * <p>
     * The password must:
     * - Be at least 8 characters long
     * - Contain at least one uppercase letter
     * - Contain at least one lowercase letter
     * - Contain at least one number
     * - Contain at least one special character (!@#$%^&*(),.?":{}|<>)
     *
     * @param password the password to be validated
     * @return true if the password is valid according to the specified criteria, false otherwise
     */
    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = HAS_UPPER.matcher(password).find();
        boolean hasLower = HAS_LOWER.matcher(password).find();
        boolean hasNumber = HAS_NUMBER.matcher(password).find();
        boolean hasSpecial = HAS_SPECIAL.matcher(password).find();

        return hasUpper && hasLower && hasNumber && hasSpecial;
    }

    /**
     * Provides a message detailing the requirements for a valid password.
     *
     * @return A string describing the rules that a password must follow.
     */
    public static String getPasswordValidationMessage() {
        return "Password must:\n" +
                "- Be at least 8 characters long\n" +
                "- Contain at least one uppercase letter\n" +
                "- Contain at least one lowercase letter\n" +
                "- Contain at least one number\n" +
                "- Contain at least one special character (!@#$%^&*(),.?\":{}|<>)";
    }
}