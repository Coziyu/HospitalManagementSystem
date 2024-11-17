package org.hms.views;

import org.hms.App;

/**
 * AbstractMenu serves as a base class for different menu implementations in the application.
 * <p>
 * This abstract class provides a common structure for menus, including a reference to the main
 * application instance (`App`). It mandates the implementation of the `displayAndExecute` method,
 * which is responsible for displaying the menu to the user and handling user interactions.
 */
public abstract class AbstractMenu {
    /**
     * Reference to the main application instance.
     * <p>
     * This variable holds the instance of the main application (`App` class),
     * providing access to various services and the current state of the application,
     * such as user context and the current menu being displayed.
     */
    protected App app;

    /**
     * Displays the menu and executes the user's choice.
     * <p>
     * This method is abstract and must be implemented by the concrete
     * menu classes. It displays the menu to the user, reads the user's
     * choice, and executes the corresponding action. If the user's
     * choice is invalid, it should display an error message and prompt
     * the user again.
     */
    public abstract void displayAndExecute();
}
