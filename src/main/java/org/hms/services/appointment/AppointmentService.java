package org.hms.services.appointment;

import org.hms.services.AbstractService;
import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.storage.StorageService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AppointmentService extends AbstractService<IAppointmentDataInterface> {

    private List<AppointmentInformation> appointments;
    private AppointmentSchedule appointmentSchedule;

    public AppointmentService(IAppointmentDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        StorageService storageService = new StorageService();
        appointments = storageService.readAppointments();
    }

    public void displayOneAppointment(AppointmentInformation appointment) {
        System.out.println("Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Patient ID: " + appointment.getPatientID());
        System.out.println("Doctor ID: " + appointment.getDoctorID());
        System.out.println("Appointment Time Slot: " + appointment.getAppointmentTimeSlot());
        System.out.println("Appointment Status: " + appointment.getAppointmentStatus());
        System.out.println("--------------------------------------------------");
    }

//For patient
public void scheduleAppointment(String patientID, String doctorID,String Date, String timeSlot, AppointmentSchedule schedule) {
    //before calling any function related to schedule/reschedule appointment, use storageservice to get schedule of wanted date first;

    int doctorCol = -1;  // Find doctor column
    int timeSlotRow = -1;
    String[][] matrix = schedule.getMatrix();

    for (int col = 1; col < matrix[0].length; col++) {
        if (matrix[0][col] != null && matrix[0][col].equals(doctorID)) {
            doctorCol = col - 1;
            break;
        }
    }

    for (int row = 1; row < matrix.length; row++) {
        if (matrix[row][0] != null && matrix[row][0].equals(timeSlot)) {
            timeSlotRow = row - 1;
            break;
        }
    }

    String slotValue = schedule.getSlot(timeSlotRow, doctorCol);
    if ("1".equals(slotValue)) {  // If slot is available (1)
        schedule.setSlot(timeSlotRow, doctorCol, patientID);  // Occupy the slot with patientID
        System.out.println("Appointment scheduled successfully for patient " + patientID + " with doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

        String timeSlotString = Date + " " + timeSlot + "-" + timeSlot;
        int appointmentID = appointments.size() + 100;
        addAppointment(timeSlotString, appointmentID, patientID, doctorID);

    } else {
        System.out.println("The selected time slot is not available.");
    }
}

    public void rescheduleAppointment(String patientID, String doctorID,String date, String timeSlot, AppointmentSchedule schedule) {

        int doctorCol = -1;  // Find doctor column
        int timeSlotRow = -1;
        String[][] matrix = schedule.getMatrix();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            if (appointment.getPatientID().equals(patientID)) {
                found = true;
                //setDoctorSchedule(doctorID, date, timeSlot, schedule);  //need to interact with previous date schedule, settle later
                try {
                    // Combine date and timeSlot into a single Date object
                    Date newTimeSlot = dateTimeFormat.parse(date + " " + timeSlot);

                    // Update the appointment's time slot
                    appointment.setAppointmentTimeSlot(newTimeSlot);}catch (ParseException e) {
                    System.out.println("Failed to parse the new time slot: " + e.getMessage());
                }
                appointment.setDoctorID(doctorID);

                for (int col = 1; col < matrix[0].length; col++) {
                    if (matrix[0][col] != null && matrix[0][col].equals(doctorID)) {
                        doctorCol = col - 1;
                        break;
                    }
                }

                for (int row = 1; row < matrix.length; row++) {
                    if (matrix[row][0] != null && matrix[row][0].equals(timeSlot)) {
                        timeSlotRow = row - 1;
                        break;
                    }
                }

                String slotValue = schedule.getSlot(timeSlotRow, doctorCol);
                if ("1".equals(slotValue)) {  // If slot is available (1)
                    schedule.setSlot(timeSlotRow, doctorCol, patientID);  // Occupy the slot with patientID
                    System.out.println("Appointment scheduled successfully for patient " + patientID + " with doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change


                } else {
                    System.out.println("The selected time slot is already occupied.");
                }

            }
        }

        if (!found) {
            System.out.println("No appointments found for patient ID: " + patientID);
        }
    }

    public void viewAppointmentStatus(String patientID) {
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            if (appointment.getPatientID().equals(patientID)) {
                displayOneAppointment(appointment);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found for patient ID: " + patientID);
        }
    }


    public void addAppointment(String timeSlotString, int appointmentID, String patientID, String doctorID) {
        try {
            AppointmentInformation newAppointment = new AppointmentInformation(
                    appointmentID,
                    patientID,
                    doctorID,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm-HH:mm").parse(timeSlotString),
                    AppointmentStatus.PENDING
            );
            appointments.add(newAppointment);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse the time slot: " + e.getMessage());
        }
    }

    public void displayMatrix(AppointmentSchedule appointmentSchedule) {
        String[][] matrix = appointmentSchedule.getMatrix();
        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.print((cell != null ? cell : "") + "\t");
            }
            System.out.println();
        }
    }


//For doctor
    public void viewRequest(String doctorID) {
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID) && appointment.getAppointmentStatus() == AppointmentStatus.PENDING) {
                displayOneAppointment(appointment);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No pending appointments found for doctor ID: " + doctorID);
        }
    }

    public void manageAppointmentRequests(int appointmentID, String doctorID) {
        boolean found = false;
        Scanner scanner = new Scanner(System.in);

        for (AppointmentInformation appointment : appointments) {
            // Check if the appointment ID and doctor ID match and the status is PENDING
            if (appointment.getAppointmentID() == appointmentID
                    && appointment.getDoctorID().equals(doctorID)
                    && appointment.getAppointmentStatus() == AppointmentStatus.PENDING) {

                displayOneAppointment(appointment);

                // Prompt the doctor to update the status
                System.out.print("Enter new status (CONFIRMED/CANCELLED): ");
                String newStatusInput = scanner.nextLine().toUpperCase();

                // Validate and update the status
                if (newStatusInput.equals("CONFIRMED") || newStatusInput.equals("CANCELLED")) {
                    AppointmentStatus newStatus = AppointmentStatus.valueOf(newStatusInput);
                    appointment.setAppointmentStatus(newStatus);
                    System.out.println("Appointment status updated successfully to " + newStatus + ".");
                } else {
                    System.out.println("Invalid status entered. Please enter CONFIRMED or CANCELLED.");
                }

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("No PENDING appointment found with ID: " + appointmentID + " for doctor ID: " + doctorID);
        }
    }

    public void viewDoctorSchedule(String doctorID) {
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            // Check if the appointment's doctor ID matches and the status is CONFIRMED
            if (appointment.getDoctorID().equals(doctorID) && appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                displayOneAppointment(appointment);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No upcoming confirmed appointments found for doctor ID: " + doctorID);
        }
    }

    /*public AppointmentOutcome keyInOutcome(String appointmentID, String patientID) {
        Scanner scanner = new Scanner(System.in);

        // Ask for type of appointment
        System.out.print("Enter the type of appointment: ");
        String typeOfAppointment = scanner.nextLine();

        // Ask for consultation notes
        System.out.print("Enter consultation notes: ");
        String consultationNotes = scanner.nextLine();

        // Create a list to store prescribed medications
        ArrayList<DrugDispenseRequest> prescribedMedication = new ArrayList<>();
        // Ask user to input drugs
        System.out.print("Enter the number of drugs to prescribe: ");
        int numDrugs = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numDrugs; i++) {
            System.out.println("\nEntering details for drug " + (i + 1));

            // Ask for drug name
            System.out.print("Enter drug name: ");
            String drugName = scanner.nextLine();

            // Ask for quantity to add
            System.out.print("Enter quantity: ");
            int addQuantity = Integer.parseInt(scanner.nextLine());

            // Ask for additional notes
            System.out.print("Enter notes for this drug: ");
            String notes = scanner.nextLine();

            // Create a new DrugDispenseRequest object and add it to the list
            int dummyID = 1000;
            DrugDispenseRequest drugRequest = new DrugDispenseRequest(dummyID,drugName, addQuantity, notes);
            prescribedMedication.add(drugRequest);
        }

        // Create and return the AppointmentOutcome object
        return new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
    }*/

    public void setDoctorSchedule(String doctorID,String Date, String timeSlot, AppointmentSchedule schedule){
        int doctorCol = -1;  // Find doctor column
        int timeSlotRow = -1;
        String[][] matrix = schedule.getMatrix();

        for (int col = 1; col < matrix[0].length; col++) {
            if (matrix[0][col] != null && matrix[0][col].equals(doctorID)) {
                doctorCol = col - 1;
                break;
            }
        }

        for (int row = 1; row < matrix.length; row++) {
            if (matrix[row][0] != null && matrix[row][0].equals(timeSlot)) {
                timeSlotRow = row - 1;
                break;
            }
        }

        String slotValue = schedule.getSlot(timeSlotRow, doctorCol);

        if ("0".equals(slotValue)) {  // If slot is available (1)
            schedule.setSlot(timeSlotRow, doctorCol, "1");  // Occupy the slot with patientID
            //System.out.println("Schedule set successful for doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

        } else {
            System.out.println("Fail to change schedule");
        }

    }

    public void cancelDoctorSchedule(String doctorID,String Date, String timeSlot, AppointmentSchedule schedule){
        int doctorCol = -1;  // Find doctor column
        int timeSlotRow = -1;
        String[][] matrix = schedule.getMatrix();

        for (int col = 1; col < matrix[0].length; col++) {
            if (matrix[0][col] != null && matrix[0][col].equals(doctorID)) {
                doctorCol = col - 1;
                break;
            }
        }

        for (int row = 1; row < matrix.length; row++) {
            if (matrix[row][0] != null && matrix[row][0].equals(timeSlot)) {
                timeSlotRow = row - 1;
                break;
            }
        }

        String slotValue = schedule.getSlot(timeSlotRow, doctorCol);

        if ("1".equals(slotValue)) {  // If slot is available (1)
            schedule.setSlot(timeSlotRow, doctorCol, "0");  // Occupy the slot with patientID
            //System.out.println("Schedule disabled successfully for doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

        } else {
            System.out.println("Fail to change schedule");
        }

    }

//For admin
    public void displayAllAppointments() {
        for (AppointmentInformation appointment : appointments) {
            displayOneAppointment(appointment);
        }
    }


    public boolean scheduleAppointment(int date) {
        return true;
    }



    public boolean updatePrescriptionStatus(String appointmentID) {
        return true;
    }
}
