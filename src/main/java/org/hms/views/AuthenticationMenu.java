package org.hms.views;

import org.hms.App;
import org.hms.entities.Colour;
import org.hms.entities.PatientContext;
import org.hms.entities.UserContext;
import org.hms.services.authentication.AuthenticationResult;
import org.hms.entities.User;

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
        System.out.println(Colour.BLUE + "=== Hospital Management System ===" + Colour.RESET);
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Select an option: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    System.out.println("Goodbye!");
                    app.setCurrentMenu(null);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void handleLogin() {
        int attempts = 0;
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.print("Enter your ID (e.g., PAT001, DOC001): ");
            String id = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            AuthenticationResult result = app.getAuthenticationService().login(id, password);

            if (result.isSuccess()) {
                User user = result.getUser();
                if (user.isFirstLogin()) {
                    if (!handlePasswordChange(user)) {
                        continue;
                    }
                }
                navigateToAppropriateMenu(user);
                return;
            } else {
                System.out.println(Colour.RED + "Login failed: " + result.getMessage() + Colour.RESET);
                attempts++;
                if (attempts < MAX_LOGIN_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - attempts));
                }
            }
        }
        System.out.println(Colour.RED + "Maximum login attempts exceeded. Please try again later." + Colour.RESET);
    }

    private boolean handlePasswordChange(User user) {
        System.out.println("=== First Time Login - Password Change Required ===");

        while (true) {
            System.out.print("Enter current password: ");
            String oldPassword = scanner.nextLine();

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println(Colour.RED + "New passwords do not match. Please try again." + Colour.RESET);
                continue;
            }

            if (newPassword.length() < 6) {
                System.out.println("Password must be at least 6 characters long.");
                continue;
            }

            if (app.getAuthenticationService().changePassword(oldPassword, newPassword)) {
                System.out.println("Password changed successfully!");
                return true;
            } else {
                System.out.println("Failed to change password. Please verify your current password.");
                return false;
            }
        }
    }

    private void navigateToAppropriateMenu(User user) {
        // Create UserContext with hospital ID parsed from user ID
        String hospitalId = user.getId(); // Default hospital ID
        UserContext userContext = new UserContext(user.getId(), user.getRole(), hospitalId);
        app.setUserContext(userContext);

        switch (user.getRole()) {
            case PATIENT:
                PatientContext patientContext = new PatientContext(userContext, user.getId());
                app.setCurrentMenu(new PatientMenu(app, patientContext));
                break;
            case DOCTOR:
                app.setCurrentMenu(new DoctorMenu(app));
                break;
            case PHARMACIST:
                app.setCurrentMenu(new PharmacistMenu(app));
                break;
            case ADMINISTRATOR:
                app.setCurrentMenu(new AdminMenu(app));
                break;
            default:
                System.out.println("Unsupported user role: " + user.getRole());
                return;
        }

        System.out.println(Colour.GREEN +"Welcome, " + user.getRole() + " " + user.getId() + Colour.RESET);
    }
}