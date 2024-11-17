package org.hms.views;

/**
 * AbstractMainMenu provides the common behavior for main menus in the application.
 * <p>
 * This class extends the AbstractMenu and contains shared functionality
 * for handling user logouts. Concrete subclasses representing specific
 * user roles (e.g., DoctorMenu, PatientMenu) should extend this class
 * and implement the necessary methods to display and execute menu actions
 * specific to the user's role.
 */
public abstract class AbstractMainMenu extends AbstractMenu {
    /**
     * Logs out the current user and transitions to the authentication menu.
     *
     * This method is responsible for logging out the current user by
     * setting the current menu to an instance of `AuthenticationMenu`.
     * This action effectively transitions the*/
    public void logout() {
        app.setCurrentMenu(new AuthenticationMenu(app));
    }
}
