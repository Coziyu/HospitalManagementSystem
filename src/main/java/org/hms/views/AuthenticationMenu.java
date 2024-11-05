package org.hms.views;

import org.hms.App;
import org.hms.entities.PatientContext;
import org.hms.entities.UserContext;
import org.hms.UserRole;

import java.util.Scanner;

public class AuthenticationMenu extends AbstractMenu {
    public AuthenticationMenu(App app) {
        this.app = app;
    }

    @Override
    public void displayAndExecute() {
        Scanner scanner = new Scanner(System.in);

        // TODO: This should be changed to do actual authentication.
        // Prompt for User details
        System.out.println("Enter your name");
        String name = scanner.nextLine();
        System.out.println("Enter your user type 1) Patient, 2) Doctor, 3) Pharmacist, 4) Admin: ");
        UserRole userRole = null;

        while (true) {
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1: {
                        userRole = UserRole.PAITENT;
                        break;
                    }
                    case 2: {
                        userRole = UserRole.DOCTOR;
                        break;
                    }
                    case 3: {
                        userRole = UserRole.PHARMACIST;
                        break;
                    }
                    case 4: {
                        userRole = UserRole.ADMINISTRATOR;
                        break;
                    }
                    default: {
                        System.out.println("Invalid choice");
                    }
                }
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Enter your ID");
        int ID = scanner.nextInt();

        // We can create contexts for other UserTypes.
        UserContext userContext = new UserContext(name, userRole, ID);
        PatientContext patientContext = new PatientContext(userContext, 444);
        //TODO: Remember to add assertion for UserContext to not be null.

        System.out.println("Switching to MainMenu");
        app.setCurrentMenu(new PatientMenu(app, patientContext));
    }

}
