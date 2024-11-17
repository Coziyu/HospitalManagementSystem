package org.hms;

/**
 * Main class to start the Hospital Management System (HMS) application.
 */
public class Main {
    public static void main(String[] args) {
        App app = new App();
        // Start with the AuthenticationMenu
        app.initialise();
        // Run the application
        app.run();
    }
}