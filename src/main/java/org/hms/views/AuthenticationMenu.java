package org.hms.views;

import org.hms.App;
import org.hms.entities.Colour;
import org.hms.entities.PatientContext;
import org.hms.entities.UserContext;
import org.hms.services.authentication.AuthenticationResult;
import org.hms.entities.User;
import org.hms.utils.PasswordUtils;

import java.util.Scanner;

/**
 * AuthenticationMenu is responsible for managing the user authentication flow.
 * <p>
 * This class extends the AbstractMenu and provides functionality for login attempts,
 * handling first-time login scenarios, and delegating users to the appropriate menu
 * based on their roles.
 */
public class AuthenticationMenu extends AbstractMenu {
    /**
     * The maximum number of unsuccessful login attempts allowed.
     *
     * <p>This constant is used in the {@code AuthenticationMenu} class to limit the number
     * of times a user can attempt to log in before being temporarily locked out or
     * required to take additional steps to verify their identity.
     */
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    /**
     * Scanner for reading user input.
     * <p>
     * This is a final instance of the Scanner class used to read input from the
     * standard input stream (typically, the keyboard). It is used within the
     * AuthenticationMenu class to handle various user interactions, such as login,
     * password changes, and other authentication-related tasks.
     */
    private final Scanner scanner;

    /**
     * Constructs an AuthenticationMenu object with the specified App instance.
     *
     * @param app the App instance used to initialize the menu
     */
    public AuthenticationMenu(App app) {
        this.app = app;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the authentication menu for the Hospital Management System and executes the selected option.
     * This menu provides two options:
     * 1. Login: Initiates the login process.
     * 2. Exit: Terminates the application.
     * <p>
     * The method continuously prompts the user for input until a valid option (Login or Exit) is selected.
     * Invalid inputs (non-numeric values, out-of-range numbers, or empty inputs) are handled by displaying
     * appropriate error messages and re-prompting the user.
     * <p>
     * Upon selecting the Exit option, the method terminates and sets the current menu to null.
     */
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

    /**
     * Handles the login process for users. This method repeatedly prompts the
     * user to enter their ID and password until a successful login is achieved
     * or the user opts to go back.
     * <p>
     * The method performs the following steps:
     * 1. Displays a login prompt to the user.
     * 2. Prompts the user to enter their ID. Allows the user to cancel the login
     * process by typing 'back'.
     * 3. Prompts the user to enter their password.
     * 4. Validates the entered credentials using the authentication service.
     * 5. If the login is successful and it is the user's first login, prompts the
     * user to change their password.
     * 6. Navigates to the appropriate menu upon successful login.
     * 7. Displays error messages for failed login attempts, with specific handling
     * for accounts that are locked.
     */
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

    /**
     * Handles the process of changing the user's password. Ensures that the
     * new password meets specified requirements and verifies the user's current
     * password before allowing changes.
     *
     * @param user The user object for which the password change is to be handled.
     * @return true if the password is successfully changed, false otherwise.
     */
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

    /**
     * Handles the actions to be performed after a successful login.
     * This includes setting up the user's context and navigating to the appropriate menu based on the user's role.
     *
     * @param user The User object representing the logged-in user, containing user information such as ID and role.
     */
    private void handleSuccessfulLogin(User user) {
        // Create UserContext using the user ID as both the name and hospital ID
        // TODO: Find the name of Staff Members and Patients, and slot it inside here.
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