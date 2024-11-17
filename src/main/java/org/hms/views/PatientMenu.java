package org.hms.views;

import org.hms.App;
import org.hms.entities.PatientContext;
import org.hms.entities.Colour;
import org.hms.services.appointment.AppointmentStatus;

import java.awt.*;
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
                    case 7 -> handleViewUpcomingAppointments();
                    case 8 -> handleViewPastAppointmentOutcome();
                    case 10 -> handleAddDoctor();
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

    private void handleAddDoctor(){
        System.out.print("Enter Doctor ID: ");
        String docID = scanner.nextLine();
        //System.out.print("Enter date: ");
        //String date = scanner.nextLine();
        app.getAppointmentService().updateAllSchedulesWithNewDoctor(docID);
    }

    private void handleViewPastAppointmentOutcome() {
        String patientID = (app.getUserContext().getHospitalID());
        app.getAppointmentService().displayAppointmentOutcomesByPatient(patientID);

    }

    private void handleCancelAppointment() {
        String patientID = (app.getUserContext().getHospitalID());
        AppointmentStatus status = app.getAppointmentService().getCurrentAppointmentStatus(patientID);
        if (status != AppointmentStatus.CONFIRMED && status != AppointmentStatus.PENDING) {
            System.out.println("No appointment can be canceled");
            return;
        }



        String doctorID = app.getAppointmentService().getDoctorID(patientID);
        String[] DateAndSlot = app.getAppointmentService().getAppointmentDateTime(patientID);
        app.getAppointmentService().setAppointmentToCanceled(patientID);

        app.getAppointmentService().resumeDoctorSchedule(doctorID, DateAndSlot[0], DateAndSlot[1]);
        System.out.println("Appointment Canceled");
        //
    }

    private void handleRescheduleAppointment() {
        handleCancelAppointment();
        handleScheduleAppointment();
    }

    private void handleViewAvailableAppointmentSlot() {
        //System.out.println("\n=== Schedule Appointment ===");
        System.out.print("Enter date (YYYYMMDD): ");
        try {
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);

            if (date.isBefore(LocalDate.now())) {
                System.out.println("Cannot view appointment slots in the past.");
                return;
            }

            app.getAppointmentService().displaySchedule(dateStr);
        }catch (DateTimeParseException e) {}
    }

    // TODO: For implementation
    private void handleScheduleAppointment() {
        //System.out.println("Feature coming soon...");
        System.out.println("\n=== Schedule Appointment ===");
        System.out.print("Enter date (YYYYMMDD): ");
        try {
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);

            if (date.isBefore(LocalDate.now())) {
                System.out.println("Cannot schedule appointments in the past.");
                return;
            }

            //dateStr = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
            app.getAppointmentService().displaySchedule(dateStr);
            String patientID = (app.getUserContext().getHospitalID());
            System.out.println("Enter doctor ID : ");
            String doctorID = scanner.nextLine();
            System.out.println("Enter time slot : ");
            String timeslot = scanner.nextLine();
            // TODO: scheduleAppointment requires doctorID, patientID, and timeSlot too.
            if(app.getAppointmentService().checkExistingAppointment(patientID)){
            app.getAppointmentService().scheduleAppointment(patientID, doctorID, dateStr,timeslot, app.getAppointmentService().getAppointmentSchedule(dateStr));
            System.out.println("Appointment scheduled for: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));}
            else{
                System.out.println("can not schedule more than 1 appointment");}

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYYMMDD.");
        }
    }


    /**
     * Handles the "View Medical Record" option.
     * Will display the patient's medical record.
     * If the patient does not have a medical record,
     * a blank record will be displayed.
     */
    private void handleViewMedicalRecord() {
        System.out.println("\n" + Colour.BLUE + "=== Medical Records for Patient: " + patientContext.getHospitalID() + "===" + Colour.RESET);
        String patientID = patientContext.getHospitalID();

        System.out.println(Colour.GREEN + " == Personal Particulars == " + Colour.RESET);
        String personalParticulars = app.getMedicalRecordService().getPatientPersonalParticulars(patientID);
        System.out.println(personalParticulars);

        System.out.println(Colour.GREEN + " == Contact Information == " + Colour.RESET);
        String contactInfo = app.getMedicalRecordService().getPatientContactInformation(patientID);
        System.out.println(contactInfo);

        System.out.println(Colour.GREEN + " == Medical History == " + Colour.RESET);
        String medicalRecordString = app.getMedicalRecordService().getPatientMedicalRecord(patientID);
        System.out.println(medicalRecordString);
    }

    // TODO: For Yingjie to implement
    private void handleViewUpcomingAppointments() {
        System.out.println("\n=== Upcoming Appointments ===");
        String patienID = (app.getUserContext().getHospitalID());
        app.getAppointmentService().viewUpcomingAppointments(patienID);

    }

    private void handleUpdateInformation() {
        System.out.println("\n" + Colour.BLUE + "=== Update Personal Information ===" + Colour.RESET);
        String patientID = (app.getUserContext().getHospitalID());

        System.out.println(Colour.GREEN + " == Contact Information == " + Colour.RESET);
        String contactInfo = app.getMedicalRecordService().getPatientContactInformation(patientID);
        System.out.println(contactInfo);


        // Choose what information to update
        try {
            while (true) {
                System.out.println("What information would you like to update?");
                System.out.println("1. Phone Number");
                System.out.println("2. Email");
                System.out.println("3. Address");
                System.out.println("4. Back to Main Menu");
                System.out.print("Select an option: ");

                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter new phone number: ");
                        String newPhoneNumber = scanner.nextLine();
                        app.getMedicalRecordService().updatePatientPhoneNumber(patientID, newPhoneNumber);
                        System.out.println(Colour.GREEN + "Phone number updated successfully." + Colour.RESET);
                    }
                    case 2 -> {
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        app.getMedicalRecordService().updatePatientEmail(patientID, newEmail);
                        System.out.println(Colour.GREEN + "Email updated successfully." + Colour.RESET);
                    }
                    case 3 -> {
                        System.out.print("Enter new address: ");
                        String newAddress = scanner.nextLine();
                        app.getMedicalRecordService().updatePatientAddress(patientID, newAddress);
                        System.out.println(Colour.GREEN + "Address updated successfully." + Colour.RESET);
                    }
                    case 4 -> {
                        return;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }


    }
    // TODO: Decide if deprecated
    private void handleViewPrescriptions() {
        System.out.println("\n=== Current Prescriptions ===");
        // Implementation would use a PrescriptionService to get prescriptions
        System.out.println("Feature coming soon...");
    }
}