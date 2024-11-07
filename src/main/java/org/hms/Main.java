package org.hms;


// Main class to start the application
public class Main {
    public static void main(String[] args) {
        App app = new App();
        // Start with the LogInMenu
        app.initialise();
        // Run the application
        app.run();
    }
}

