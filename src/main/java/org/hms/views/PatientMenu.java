package org.hms.views;

import org.hms.App;
import org.hms.entities.PatientContext;
import org.hms.entities.Colour;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PatientMenu extends AbstractMainMenu {
    private final PatientContext patientContext;
    private final Scanner scanner;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{8}$");

    public PatientMenu(App app, PatientContext patientContext) {
        this.app = app;
        this.patientContext = patientContext;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayAndExecute() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Patient Menu ===" + Colour.RESET);
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcomes");
            System.out.println("9. Logout");
            System.out.print("Select an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> handleViewMedicalRecord();
                    case 2 -> handleUpdateInformation();
                    case 3 -> handleViewAvailableAppointmentSlot();
                    case 4 -> handleScheduleAppointment();
                    case 5 -> handleRescheduleAppointment();
                    case 6 -> handleCancelAppointment();
                    case 7 -> handleViewScheduledAppointments();
                    case 8 -> handleViewPastAppointmentOutcome();
                    case 9 -> {
                        app.getAuthenticationService().logout();
                        app.setCurrentMenu(new AuthenticationMenu(app));
                        return;
                    }
                    default -> System.out.println(Colour.RED + "Invalid option. Please try again." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }
    }

    private void handleViewMedicalRecord() {
        System.out.println("\n" + Colour.BLUE + "=== Medical Records ===" + Colour.RESET);
        Integer patientID = patientContext.getHospitalID();

        //TODO: For Elijah to refactor this part. Since his method was refactored to use Optional<>
        // Also, is this meant to be `records` or `record` singular? If it's singular, consider
        // something like this line below:
        // Optional<MedicalRecord> record = app.getMedicalRecordService().getMedicalRecord();
        // If it's all the records, then you have to implement the method / declare the method in
        // the DataInterface
        System.out.println("Medical Record Information:");
        System.out.println("- Patient ID: " + patientContext.getPatientID());
        System.out.println("- Personal Information:");
        System.out.println("  • Name: John Doe");
        System.out.println("  • Date of Birth: 01/01/1990");
        System.out.println("  • Gender: Male");
        System.out.println("- Contact Information:");
        System.out.println("  • Phone Number: 91234567");
        System.out.println("  • Email Address: patient@email.com");
        System.out.println("- Blood Type: O+");
        System.out.println("- Past Diagnoses and Treatments");

        String medicalRecords = "placeholder";
        System.out.println(medicalRecords);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // TODO: For Amos to implement
    private void handleUpdateInformation() {
        while (true) {
            System.out.println("\n" + Colour.BLUE + "=== Update Personal Information ===" + Colour.RESET);
            System.out.println("You can update the following information:");
            System.out.println("1. Email Address");
            System.out.println("2. Contact Number");
            System.out.println("3. Return to Main Menu");
            System.out.println("\nNote: Medical information, diagnoses, and treatments cannot be modified.");
            System.out.print("\nSelect an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> updateEmail();
                    case 2 -> updatePhoneNumber();
                    case 3 -> { return; }
                    default -> System.out.println(Colour.RED + "Invalid option. Please try again." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please enter a valid number." + Colour.RESET);
            }
        }
    }

    private void updateEmail() {
        System.out.print("Enter new email address: ");
        String email = scanner.nextLine();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println(Colour.RED + "Invalid email format. Please try again." + Colour.RESET);
            return;
        }

        // TODO: Update email in database
        System.out.println(Colour.GREEN + "Email updated successfully!" + Colour.RESET);
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void updatePhoneNumber() {
        System.out.print("Enter new phone number (8 digits): ");
        String phone = scanner.nextLine();

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            System.out.println(Colour.RED + "Invalid phone number format. Please enter 8 digits." + Colour.RESET);
            return;
        }

        // TODO: Update phone number in database
        System.out.println(Colour.GREEN + "Phone number updated successfully!" + Colour.RESET);
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void handleViewAvailableAppointmentSlot() {
        System.out.println("\n" + Colour.BLUE + "=== View Available Appointment Slots ===" + Colour.RESET);

        // Get next 7 days that have available slots
        LocalDate currentDate = LocalDate.now();
        System.out.println("Available appointment dates:");

        try {
            for (int i = 0; i < 7; i++) {
                LocalDate date = currentDate.plusDays(i);
                // TODO: Check if there are available slots for this date
                boolean hasAvailableSlots = true; // This should be replaced with actual availability check

                if (hasAvailableSlots) {
                    System.out.printf("%d. %s (%s)%n",
                            i + 1,
                            date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            date.getDayOfWeek());
                }
            }

            System.out.print("\nSelect a date (1-7) or 0 to return: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 7) {
                    LocalDate selectedDate = currentDate.plusDays(choice - 1);
                    try {
                        String dateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        app.getAppointmentService().displayMatrix(dateStr);
                    } catch (Exception e) {
                        System.out.println(Colour.RED + "Error displaying appointment matrix: " + e.getMessage() + Colour.RESET);
                    }
                } else if (choice == 0) {
                    return;
                } else {
                    System.out.println(Colour.RED + "Invalid selection. Please choose between 0-7." + Colour.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Invalid selection. Please enter a number." + Colour.RESET);
            }
        } catch (Exception e) {
            System.out.println(Colour.RED + "Error processing dates: " + e.getMessage() + Colour.RESET);
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private LocalDate parseDate(String dateStr) {
        try {
            // Parse date in DD/MM/YYYY format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println(Colour.RED + "Invalid date format. Please use DD/MM/YYYY." + Colour.RESET);
            return null;
        }
    }

    private void handleScheduleAppointment() {
        System.out.println("\n" + Colour.BLUE + "=== Schedule Appointment ===" + Colour.RESET);

        try {
            // First show available dates
            LocalDate currentDate = LocalDate.now();
            System.out.println("Available dates for appointment:");

            for (int i = 0; i < 7; i++) {
                LocalDate date = currentDate.plusDays(i);
                System.out.printf("%d. %s (%s)%n",
                        i + 1,
                        date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        date.getDayOfWeek());
            }

            System.out.print("\nSelect a date (1-7) or 0 to return: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > 7) {
                System.out.println(Colour.RED + "Invalid selection. Please choose between 1-7." + Colour.RESET);
                return;
            }

            LocalDate selectedDate = currentDate.plusDays(choice - 1);
            String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            try {
                app.getAppointmentService().displayMatrix(formattedDate);

                System.out.print("Enter Doctor ID (or 0 to cancel): ");
                String doctorID = scanner.nextLine();

                if (doctorID.equals("0")) {
                    return;
                }

                System.out.print("Enter desired time slot (or 0 to cancel): ");
                String timeslot = scanner.nextLine();

                if (timeslot.equals("0")) {
                    return;
                }

                String patientID = Integer.toString(patientContext.getPatientID());

                try {
                    app.getAppointmentService().scheduleAppointment(
                            patientID,
                            doctorID,
                            formattedDate,
                            timeslot,
                            app.getAppointmentService().getAppointmentSchedule(formattedDate)
                    );

                    System.out.println(Colour.GREEN + "Appointment scheduled for: " +
                            selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + Colour.RESET);
                } catch (Exception e) {
                    System.out.println(Colour.RED + "Error scheduling appointment: " + e.getMessage() + Colour.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colour.RED + "Error displaying appointment matrix: " + e.getMessage() + Colour.RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input. Please enter a number." + Colour.RESET);
        } catch (Exception e) {
            System.out.println(Colour.RED + "Error processing request: " + e.getMessage() + Colour.RESET);
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void handleRescheduleAppointment() {
        System.out.println("\n" + Colour.BLUE + "=== Reschedule Appointment ===" + Colour.RESET);
        System.out.println("Your current appointments:");
        String patientID = Integer.toString(patientContext.getPatientID());
        app.getAppointmentService().viewAppointmentStatus(patientID);
        // TODO: Implement rescheduling logic
        System.out.println("Feature coming soon...");

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void handleCancelAppointment() {
        System.out.println("\n" + Colour.BLUE + "=== Cancel Appointment ===" + Colour.RESET);
        System.out.println("Your current appointments:");
        String patientID = Integer.toString(patientContext.getPatientID());
        app.getAppointmentService().viewAppointmentStatus(patientID);
        // TODO: Implement cancellation logic
        System.out.println("Feature coming soon...");

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void handleViewScheduledAppointments() {
        System.out.println("\n" + Colour.BLUE + "=== Scheduled Appointments ===" + Colour.RESET);
        String patientID = Integer.toString(patientContext.getPatientID());
        app.getAppointmentService().viewAppointmentStatus(patientID);
        System.out.println("\nAppointment Status Legend:");
        System.out.println("- Confirmed: Doctor has accepted the appointment");
        System.out.println("- Pending: Awaiting doctor confirmation");
        System.out.println("- Canceled: Appointment has been canceled");
        System.out.println("- Completed: Appointment has been completed");

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void handleViewPastAppointmentOutcome() {
        System.out.println("\n" + Colour.BLUE + "=== Past Appointment Outcomes ===" + Colour.RESET);
        String patientID = Integer.toString(patientContext.getPatientID());
        // TODO: Implement viewing past appointment outcomes
        System.out.println("Feature coming soon...");

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}