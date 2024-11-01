package org.hms.views;

import org.hms.App;
import org.hms.UserContext;
import org.hms.UserType;

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
        UserType userType = null;

        while (true) {
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1: {
                        userType = UserType.PAITENT;
                        break;
                    }
                    case 2: {
                        userType = UserType.DOCTOR;
                        break;
                    }
                    case 3: {
                        userType = UserType.PHARMACIST;
                        break;
                    }
                    case 4: {
                        userType = UserType.ADMINISTRATOR;
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
        UserContext userContext = new UserContext(name, userType, ID);

        System.out.println("Switching to MainMenu");
        app.setCurrentMenu(new PatientMenu(app));
    }

}
