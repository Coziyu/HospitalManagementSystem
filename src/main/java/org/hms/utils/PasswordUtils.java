package org.hms.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class PasswordUtils {
    private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
    private static final Pattern HAS_NUMBER = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

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

    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = HAS_UPPER.matcher(password).find();
        boolean hasLower = HAS_LOWER.matcher(password).find();
        boolean hasNumber = HAS_NUMBER.matcher(password).find();
        boolean hasSpecial = HAS_SPECIAL.matcher(password).find();

        return hasUpper && hasLower && hasNumber && hasSpecial;
    }

    public static String getPasswordValidationMessage() {
        return "Password must:\n" +
                "- Be at least 8 characters long\n" +
                "- Contain at least one uppercase letter\n" +
                "- Contain at least one lowercase letter\n" +
                "- Contain at least one number\n" +
                "- Contain at least one special character (!@#$%^&*(),.?\":{}|<>)";
    }
}