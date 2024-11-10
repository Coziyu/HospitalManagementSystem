package org.hms;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        // Start with the AuthenticationMenu
        app.initialise();
        // Run the application
        app.run();
    }
}