package org.hms.views;

import org.hms.App;

public abstract class AbstractMenu {
    protected App app;

    /**
     * Displays the menu and executes the user's choice.
     *
     * This method is abstract and must be implemented by the concrete
     * menu classes. It displays the menu to the user, reads the user's
     * choice, and executes the corresponding action. If the user's
     * choice is invalid, it should display an error message and prompt
     * the user again.
     */
    public abstract void displayAndExecute();
}
