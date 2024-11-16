package org.hms.views;

import org.hms.App;
import org.hms.entities.Colour;
import org.hms.entities.PatientContext;
import org.hms.entities.UserContext;
import org.hms.services.authentication.AuthenticationResult;
import org.hms.entities.User;
import org.hms.utils.PasswordUtils;

import java.util.Scanner;

public class AuthenticationMenu extends AbstractMenu {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private final Scanner scanner;

    public AuthenticationMenu(App app) {
        this.app = app;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Hospital Management System ===" + Colour.RESET);
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Select an option: ");

            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println(Colour.RED + "Please enter a valid option." + Colour.RESET);
                    continue;
                }

                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        System.out.println(Colour.GREEN + "Thank you for using the Hospital Management System. Goodbye!" + Colour.RESET);
                        app.setCurrentMenu(null);
                        return;
                    default:
                        System.out.println(Colour.RED + "Invalid option. Please enter 1 or 2." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }
    }

    private void handleLogin() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Login ===" + Colour.RESET);

            // Get user ID
            System.out.print("Enter your ID (or 'back' to return): ");
            String id = scanner.nextLine().trim();

            if (id.equalsIgnoreCase("back")) {
                return;
            }

            if (id.isEmpty()) {
                System.out.println(Colour.RED + "ID cannot be empty." + Colour.RESET);
                continue;
            }

            // Get password
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (password.isEmpty()) {
                System.out.println(Colour.RED + "Password cannot be empty." + Colour.RESET);
                continue;
            }

            // Attempt login
            AuthenticationResult result = app.getAuthenticationService().login(id, password);

            if (result.isSuccess()) {
                User user = result.getUser();

                // Handle first-time login and password change
                if (user.isFirstLogin()) {
                    System.out.println(Colour.YELLOW + "\n=== First Time Login - Password Change Required ===" + Colour.RESET);
                    if (!handlePasswordChange(user)) {
                        System.out.println(Colour.RED + "Password change failed. Please try logging in again." + Colour.RESET);
                        continue;
                    }
                }

                // Navigate to appropriate menu
                handleSuccessfulLogin(user);
                return;

            } else {
                System.out.println(Colour.RED + "Login failed: " + result.getMessage() + Colour.RESET);

                // If the message indicates account lockout, provide more information
                if (result.getMessage().contains("locked")) {
                    System.out.println(Colour.YELLOW + "For security reasons, the account has been temporarily locked." + Colour.RESET);
                    System.out.println("Please try again later or contact an administrator for assistance.");
                }
            }
        }
    }

    private boolean handlePasswordChange(User user) {
        System.out.println("\nFor security reasons, you must change your password.");
        System.out.println(Colour.CYAN + "Password Requirements:" + Colour.RESET);
        System.out.println(PasswordUtils.getPasswordValidationMessage());

        int attempts = 0;
        final int MAX_PASSWORD_CHANGE_ATTEMPTS = 3;

        while (attempts < MAX_PASSWORD_CHANGE_ATTEMPTS) {
            System.out.print("\nEnter current password: ");
            String oldPassword = scanner.nextLine();

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            if (!PasswordUtils.isPasswordValid(newPassword)) {
                System.out.println(Colour.RED + "\nPassword does not meet the requirements:" + Colour.RESET);
                System.out.println(PasswordUtils.getPasswordValidationMessage());
                attempts++;
                if (attempts < MAX_PASSWORD_CHANGE_ATTEMPTS) {
                    System.out.println(Colour.YELLOW + "Attempts remaining: " +
                            (MAX_PASSWORD_CHANGE_ATTEMPTS - attempts) + Colour.RESET);
                }
                continue;
            }

            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println(Colour.RED + "Passwords do not match." + Colour.RESET);
                attempts++;
                if (attempts < MAX_PASSWORD_CHANGE_ATTEMPTS) {
                    System.out.println(Colour.YELLOW + "Attempts remaining: " +
                            (MAX_PASSWORD_CHANGE_ATTEMPTS - attempts) + Colour.RESET);
                }
                continue;
            }

            if (app.getAuthenticationService().changePassword(oldPassword, newPassword)) {
                System.out.println(Colour.GREEN + "\nPassword changed successfully!" + Colour.RESET);
                return true;
            } else {
                System.out.println(Colour.RED + "Failed to change password. Please verify your current password." + Colour.RESET);
                attempts++;
                if (attempts < MAX_PASSWORD_CHANGE_ATTEMPTS) {
                    System.out.println(Colour.YELLOW + "Attempts remaining: " +
                            (MAX_PASSWORD_CHANGE_ATTEMPTS - attempts) + Colour.RESET);
                }
            }
        }

        System.out.println(Colour.RED + "\nMaximum password change attempts exceeded." + Colour.RESET);
        return false;
    }

    private void handleSuccessfulLogin(User user) {
        // Create UserContext using the user ID as both the name and hospital ID
        UserContext userContext = new UserContext(user.getId(), user.getRole(), user.getId());
        app.setUserContext(userContext);

        // Navigate to appropriate menu based on user role
        AbstractMenu nextMenu = null;
        switch (user.getRole()) {
            case PATIENT:
                PatientContext patientContext = new PatientContext(userContext, user.getId());
                nextMenu = new PatientMenu(app, patientContext);
                break;
            case DOCTOR:
                nextMenu = new DoctorMenu(app);
                break;
            case PHARMACIST:
                nextMenu = new PharmacistMenu(app);
                break;
            case ADMINISTRATOR:
                nextMenu = new AdminMenu(app);
                break;
            default:
                System.out.println(Colour.RED + "Unsupported user role: " + user.getRole() + Colour.RESET);
                return;
        }

        // Display welcome message
        System.out.println(Colour.GREEN + "\nWelcome, " + user.getRole() + " " + user.getId() + Colour.RESET);
        System.out.println("Login successful! Loading your dashboard...\n");

        // Set and display the new menu
        app.setCurrentMenu(nextMenu);
        if (nextMenu != null) {
            nextMenu.displayAndExecute();
        }
    }
}