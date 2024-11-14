package org.hms.services.appointment;

import org.hms.services.storage.StorageService;

import java.util.Scanner;

public class AppointmentTest {

    public static void main(String[] args) {
        // Specify the path to your CSV file
        //String filePath = "/E:/Appointments.csv/";
        StorageService storageService = new StorageService();
        AppointmentService appointmentService = new AppointmentService(storageService);

        // Create an instance of AppointmentService with the file path


        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        AppointmentSchedule schedule = new AppointmentSchedule(3, 3);
        //AppointmentCsvTool csvTool = new AppointmentCsvTool(filePath);
        AppointmentSchedule newschedule = storageService.loadSchedule("2024-08-09");

        // Set doctor IDs in the first row
        schedule.setDoctorID(0, "DoctorA");
        schedule.setDoctorID(1, "DoctorB");
        schedule.setDoctorID(2, "DoctorC");

        // Set time slots in the first column
        schedule.setTimeSlot(0, "09:00");
        schedule.setTimeSlot(1, "10:00");
        schedule.setTimeSlot(2, "11:00");

        // Set availability and patient IDs
        schedule.setSlot(0, 0, "1");               // 09:00, DoctorA: available
        schedule.setSlot(0, 1, "Patient123");      // 09:00, DoctorB: occupied by Patient123
        schedule.setSlot(0, 2, "0");               // 09:00, DoctorC: unavailable
        schedule.setSlot(1, 0, "Patient456");      // 10:00, DoctorA: occupied by Patient456
        schedule.setSlot(1, 1, "1");               // 10:00, DoctorB: available
        schedule.setSlot(1, 2, "1");               // 10:00, DoctorC: available
        schedule.setSlot(2, 0, "0");               // 11:00, DoctorA: unavailable
        schedule.setSlot(2, 1, "0");               // 11:00, DoctorB: unavailable
        schedule.setSlot(2, 2, "Patient789");

        while (true) {
            System.out.println("\n--- Appointment Service ---");
            System.out.println("1. Display all appointments");
            System.out.println("2. View appointments by patient ID");
            System.out.println("3. Add a new appointment");
            System.out.println("4. Exit");
            System.out.println("5. Check your pending appointments");
            System.out.println("6. Settle your pending appointments");
            System.out.println("7. View upcoming appointments");
            System.out.println("8. Display matrix");
            System.out.println("9. Add appointment");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    // Display all appointments
                    appointmentService.displayAllAppointments();
                    break;

                case 2:
                    // View appointments for a specific patient
                    System.out.print("Enter patient ID: ");
                    String patientID = scanner.nextLine();
                    appointmentService.viewAppointmentStatus(patientID);
                    break;

                case 3:
                    // Add a new appointment
                    try {
                        System.out.print("Enter appointment ID: ");
                        int appointmentID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter patient ID: ");
                        String newPatientID = scanner.nextLine();

                        System.out.print("Enter doctor ID: ");
                        String doctorID = scanner.nextLine();

                        System.out.print("Enter time slot (format: yyyy-MM-dd HH:mm-HH:mm): ");
                        String timeSlot = scanner.nextLine();

                        System.out.print("Enter appointment status (e.g., PENDING, CONFIRMED): ");
                        String statusInput = scanner.nextLine();
                        AppointmentStatus status = AppointmentStatus.valueOf(statusInput.toUpperCase());

                        // Add appointment
                        appointmentService.addAppointment(timeSlot, appointmentID, newPatientID, doctorID);
                        System.out.println("Appointment added successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status or input. Please try again.");
                    }
                    break;

                case 4:
                    // Exit
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                case 5:
                    // View pending appointments for a specific doctor
                    System.out.print("Enter doctor ID: ");
                    String doctorID = scanner.nextLine();
                    appointmentService.viewRequest(doctorID);
                    break;

                case 6:
                    // Settle a pending appointment
                    System.out.print("Enter your doctor ID: ");
                    doctorID = scanner.nextLine();
                    System.out.print("Enter the appointment ID to settle: ");
                    int appointmentID = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    appointmentService.manageAppointmentRequests(appointmentID, doctorID);
                    break;

                case 7:
                    // View upcoming confirmed appointments for a specific doctor
                    System.out.print("Enter doctor ID: ");
                    doctorID = scanner.nextLine();
                    //appointmentService.viewDoctorSchedule(doctorID);
                    break;

                case 8:
                    //appointmentService.displayMatrix(newschedule);
                    break;

                case 9:
                    appointmentService.scheduleAppointment("P1000", "DoctorB","2024-11-01", "10:00", schedule);
                    //appointmentService.scheduleAppointment("P2000", "DoctorC","2024-11-01", "10:00", schedule);
                    break;

                case 10:

                    appointmentService.rescheduleAppointment("P1000", "DoctorC","2024-11-01", "10:00", schedule);
                    break;

                case 11:
                    storageService.writeScheduleToCSV(newschedule,"2024-08-09");
                    break;




                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}