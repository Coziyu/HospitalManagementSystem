package org.hms.services.appointment;

import org.hms.entities.Colour;
import org.hms.services.AbstractService;
import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.drugdispensary.DrugRequestStatus;
import org.hms.services.storage.StorageService;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * AppointmentService provides functionalities to manage medical appointments.
 * This includes scheduling, rescheduling, and canceling appointments.
 * It also provides methods for viewing and updating appointment details.
 */
public class AppointmentService extends AbstractService<IAppointmentDataInterface> {

    /**
     * A list of appointment details managed by the AppointmentService.
     * Each appointment is represented as an instance of
     * {@link AppointmentInformation}, encapsulating details such as
     * appointment ID, patient ID, doctor ID, appointment time slot, and status.
     */
    private List<AppointmentInformation> appointments;
    /**
     * Represents the schedule for appointments within the appointment service.
     * This field encapsulates the data and methods related to appointment scheduling.
     * It is used to manage the schedule for appointments, including creation, modification,
     * and retrieval of appointment times and related information.
     */
    private AppointmentSchedule appointmentSchedule;
    /**
     * List of all appointment outcomes managed by the AppointmentService.
     * Each entry represents the outcome of a specific appointment, including
     * details such as diagnosis, prescribed medications, and consultation notes.
     */
    private ArrayList<AppointmentOutcome> appointmentOutcomes;


    /**
     * Constructs an AppointmentService instance with the provided data interface.
     * Initializes the appointment service by reading existing appointments and their outcomes from CSV storage.
     *
     * @param dataInterface an instance of IAppointmentDataInterface for accessing appointment-related data
     */
    public AppointmentService(IAppointmentDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        StorageService storageService = new StorageService();
        appointments = storageService.readAppointments();
        appointmentOutcomes = storageService.readAppointmentOutcomesFromCSV();
    }

    /**
     * Displays the details of a single appointment.
     *
     * @param appointment An object containing the information of the appointment to be displayed.
     */
    public void displayOneAppointment(AppointmentInformation appointment) {
        System.out.println("Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Patient ID: " + appointment.getPatientID());
        System.out.println("Doctor ID: " + appointment.getDoctorID());
        System.out.println("Appointment Time Slot: " + appointment.getAppointmentTimeSlot());
        System.out.println("Appointment Status: " + appointment.getAppointmentStatus());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Retrieves the appointment schedule for a given date.
     *
     * @param date The date for which the appointment schedule is requested.
     * @return The appointment schedule for the specified date.
     */
    public AppointmentSchedule getAppointmentSchedule(String date) {
        return storageServiceInterface.loadSchedule(date);
    }

    /**
     * Retrieves the doctor's unique identifier associated with a patient's appointment.
     * The method searches through the list of appointments to find a matching patient ID with
     * an appointment status of either PENDING or CONFIRMED.
     *
     * @param patientID The unique identifier for the patient whose doctor's ID is to be retrieved.
     * @return The doctor's unique identifier if a matching appointment with the patient ID is found and the appointment is PENDING or CONFIRMED;
     * null if no such appointment is found.
     */
    //For patient
    public String getDoctorID(String patientID) {
        for (AppointmentInformation appointment : appointments) {
            // Check if the patientID matches
            if (appointment.getPatientID().equals(patientID)) {
                if ((appointment.getAppointmentStatus() == AppointmentStatus.PENDING) || (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED)) {
                    // Return the doctor's ID for the matching appointment
                    return appointment.getDoctorID();
                }
            }
        }
        // If no appointment is found for the given patientID
        System.out.println("No appointment found for patient ID: " + patientID);
        return null; // or throw an exception if preferred
    }

    /**
     * Gets the current appointment status for a patient identified by the given patientID.
     *
     * @param patientID The unique identifier for the patient whose appointment status is to be retrieved.
     * @return The current status of the patient's appointment. If no appointment is found, returns null.
     */
    public AppointmentStatus getCurrentAppointmentStatus(String patientID) {
        for (AppointmentInformation appointment : appointments) {
            // Check if the patientID matches
            if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING
                    || appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                return appointment.getAppointmentStatus();
            }
        }
        // If no appointment is found for the given patientID
        System.out.println("No appointment found for patient ID: " + patientID);
        return null; // or throw an exception if preferred
    }

    /**
     * Checks for an existing active appointment for a given patient.
     * <p>
     * An appointment is considered active if its status is not COMPLETED or CANCELLED.
     *
     * @param patientID The unique identifier for the patient.
     * @return {@code true} if there are no active appointments for the specified patient,
     * {@code false} if there is an active appointment.
     */
    public Boolean checkExistingAppointment(String patientID) {
        for (AppointmentInformation appointment : appointments) {
            // Check if the patientID matches
            if (appointment.getPatientID().equals(patientID)) {
                // Check if the appointment status is not COMPLETED
                if (appointment.getAppointmentStatus() != AppointmentStatus.COMPLETED
                        && appointment.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
                    return false; // An active appointment exists
                }
            }
        }
        // No active appointment found or all appointments are COMPLETED
        return true;
    }

    /**
     * Retrieves the date and time slot of the appointment for a given patient.
     *
     * @param patientID the ID of the patient whose appointment details are being requested
     * @return an array of Strings where the first element is the appointment date in "yyyyMMdd" format
     * and the second element is the appointment time slot in "HH:mm" format.
     * Returns null if no matching appointment is found.
     */
    public String[] getAppointmentDateTime(String patientID) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        for (AppointmentInformation appointment : appointments) {
            // Check if the patientID matches
            if (appointment.getPatientID().equals(patientID)) {
                if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING || appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                    // Format the date and time slot
                    String date = dateFormatter.format(appointment.getAppointmentTimeSlot());
                    String timeSlot = timeFormatter.format(appointment.getAppointmentTimeSlot());
                    return new String[]{date, timeSlot};
                }
            }
        }
        // If no appointment is found for the given patientID
        System.out.println("No appointment found for patient ID: " + patientID);
        return null; // or throw an exception if preferred
    }//return array of string, index 0 is date, index 1 is timeslot

    /**
     * Resumes the doctor's schedule by setting a specific time slot to "available".
     *
     * @param doctorID The unique identifier for the doctor.
     * @param Date     The date for which the schedule needs to be resumed.
     * @param timeSlot The time slot that needs to be set to "available".
     */
    public void resumeDoctorSchedule(String doctorID, String Date, String timeSlot) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(Date);
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

        if (!("available".equals(slotValue))) {  // If slot is available (1)
            schedule.setSlot(timeSlotRow, doctorCol, "available");
            storageServiceInterface.writeScheduleToCSV(schedule, Date);// Occupy the slot with patientID
            //System.out.println("Schedule set successful for doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

        } else {
            System.out.println("Fail to change schedule");
        }

    }

    /**
     * Sets the status of the first confirmed or pending appointment for the given patient ID to CANCELED.
     * If no matching appointment is found, the method does nothing.
     *
     * @param patientID the ID of the patient whose appointment is to be canceled
     */
    public void setAppointmentToCanceled(String patientID) {
        for (AppointmentInformation appointment : appointments) {
            // Check if the patientID matches
            if (appointment.getPatientID().equals(patientID)) {
                // Set the appointment status to CANCELED for the first match
                if (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED || appointment.getAppointmentStatus() == AppointmentStatus.PENDING) {
                    appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
                    storageServiceInterface.writeAppointmentsToCsv(appointments);
                    return; // Exit after setting the first matching appointment
                } else {
                    continue;
                }
            }
            // If no appointment is found for the given patientID
            //System.out.println("No appointment found for patient ID: " + patientID);
        }
    }


    /**
     * Schedules an appointment for a patient with a specific doctor at a given date and time slot.
     *
     * @param patientID The ID of the patient for whom the appointment is being scheduled.
     * @param doctorID  The ID of the doctor with whom the appointment is being scheduled.
     * @param Date      The date for which the appointment is scheduled.
     * @param timeSlot  The specific time slot for the appointment.
     * @param schedule  The current appointment schedule.
     * @return true if the appointment was successfully scheduled, false otherwise.
     */
    public boolean scheduleAppointment(String patientID, String doctorID, String Date, String timeSlot, AppointmentSchedule schedule) {
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
        if ("available".equals(slotValue)) {  // If slot is available (1)
            schedule.setSlot(timeSlotRow, doctorCol, patientID);
            storageServiceInterface.writeScheduleToCSV(schedule, Date);// Occupy the slot with patientID
            System.out.println("Appointment scheduled successfully for patient " + patientID + " with doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

            String timeSlotString = Date + " " + timeSlot + "-" + timeSlot;
            int appointmentID = appointments.size() + 100;
            addAppointment(timeSlotString, appointmentID, patientID, doctorID);
            return true;

        } else {
            System.out.println("The selected time slot is not available.");
            return false;
        }
    }

    /**
     * Reschedules an appointment for a patient with a specified doctor at a new date and time slot.
     *
     * @param patientID The ID of the patient whose appointment is being rescheduled.
     * @param doctorID  The ID of the doctor with whom the appointment is being rescheduled.
     * @param date      The new date for the rescheduled appointment in YYYYMMDD format.
     * @param timeSlot  The new time slot for the rescheduled appointment in HH:mm format.
     */
    public void rescheduleAppointment(String patientID, String doctorID, String date, String timeSlot) {


        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(date);
        int doctorCol = -1;  // Find doctor column
        int timeSlotRow = -1;
        String[][] matrix = schedule.getMatrix();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            if (appointment.getPatientID().equals(patientID)) {
                found = true;
                //setDoctorSchedule(doctorID, date, timeSlot, schedule);  //need to interact with previous date schedule, settle later
                try {
                    // Combine date and timeSlot into a single Date object
                    Date newTimeSlot = dateTimeFormat.parse(date + " " + timeSlot);

                    // Update the appointment's time slot
                    appointment.setAppointmentTimeSlot(newTimeSlot);
                    storageServiceInterface.writeAppointmentsToCsv(appointments);
                } catch (ParseException e) {
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
                if ("available".equals(slotValue)) {  // If slot is available (1)
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

    /**
     * Displays the appointment status for a specified patient ID.
     * If appointments are found for the given patient ID, it displays each appointment's details.
     * If no appointments are found, it notifies that no appointments were found for the provided patient ID.
     *
     * @param patientID The unique identifier of the patient whose appointment status needs to be viewed.
     */
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

    /**
     * Displays the details of the upcoming appointments for the specified patient.
     * An appointment is considered upcoming if its status is either PENDING or CONFIRMED.
     *
     * @param patientID the unique identifier of the patient whose upcoming appointments are to be viewed.
     */
    public void viewUpcomingAppointments(String patientID) {
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            if (appointment.getPatientID().equals(patientID)) {
                // Check if the appointment status is PENDING or CONFIRMED
                if (appointment.getAppointmentStatus() == AppointmentStatus.PENDING
                        || appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                    displayOneAppointment(appointment); // Display the appointment details
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No upcoming appointments found for patient ID: " + patientID);
        }
    }

    /**
     * Adds a new appointment to the system. This method takes in the details of the appointment,
     * including the time slot, appointment ID, patient ID, and doctor ID, and creates
     * an appointment record with a pending status. The appointment record is then saved
     * to persistent storage.
     *
     * @param timeSlotString the time slot for the appointment in the format "yyyyMMdd HH:mm-HH:mm"
     * @param appointmentID  the unique identifier for the appointment
     * @param patientID      the unique identifier for the patient
     * @param doctorID       the unique identifier for the doctor
     */
    public void addAppointment(String timeSlotString, int appointmentID, String patientID, String doctorID) {
        try {
            AppointmentInformation newAppointment = new AppointmentInformation(
                    appointmentID,
                    patientID,
                    doctorID,
                    new SimpleDateFormat("yyyyMMdd HH:mm-HH:mm").parse(timeSlotString),
                    AppointmentStatus.PENDING
            );
            appointments.add(newAppointment);
            storageServiceInterface.writeAppointmentsToCsv(appointments);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse the time slot: " + e.getMessage());
        }
    }

    /**
     * Displays the appointment matrix for a given date.
     *
     * @param date The date for which the appointment matrix is to be displayed.
     */
    public void displayMatrix(String date) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(date);
        String[][] matrix = schedule.getMatrix();
        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.print((cell != null ? cell : "") + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Displays the appointment schedule for a given date, listing available time slots
     * and corresponding staff members.
     *
     * @param date The date for which the appointment schedule is to be displayed, in yyyy-MM-dd format.
     */
    public void displaySchedule(String date) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(date);
        String[][] matrix = schedule.getMatrix();

        // Store the headers from the first row for reference
        String[] headers = matrix[0];

        // Start loop from 1 to skip the first row
        for (int i = 1; i < matrix.length; i++) {
            String[] row = matrix[i];

            // Skip if row[0] (first column) is null, to avoid printing unintended empty rows
            if (row[0] == null) {
                continue;
            }

            // Display the first column (time slot) as is
            System.out.print(row[0]);

            // For the rest of the columns
            for (int j = 1; j < row.length; j++) {
                // Only add a tab and print header if the cell contains "1"
                if ("available".equals(row[j])) {
                    //System.out.print("\t" + headers[j]);
                    String doctorName = storageServiceInterface.getStaffNameByID(headers[j]);
                    //TODO: For nich to format this properly
                    System.out.print("\t" + doctorName + "(ID = " + headers[j] + ")");
                    //Not sure can work or not until the the staff.csv and schedule csv files have same doctors
                }
            }
            System.out.println(); // Move to the next row

        }
    }

    /**
     * Displays the appointment outcomes for a given patient ID.
     *
     * @param patientID the unique identifier of the patient whose appointment outcomes are to be displayed
     */
    public void displayAppointmentOutcomesByPatient(String patientID) {
        boolean found = false;
        System.out.println("Appointment Outcomes for Patient ID: " + patientID);
        System.out.println("--------------------------------------------------");

        for (AppointmentOutcome outcome : appointmentOutcomes) {
            // Check if the patient ID matches
            if (outcome.getPatientID().equals(patientID)) {
                found = true;

                // Display the details of the appointment outcome
                System.out.println("Appointment ID: " + outcome.getAppointmentID());
                System.out.println("Type of Appointment: " + outcome.getTypeOfAppointment());
                System.out.println("Consultation Notes: " + outcome.getConsultationNotes());
                System.out.println("Prescribed Medication:");

                if (outcome.getPrescribedMedication().isEmpty()) {
                    System.out.println("No prescribed medication.");
                } else {
                    for (DrugDispenseRequest drug : outcome.getPrescribedMedication()) {
                        System.out.println("- Drug Name: " + drug.getDrugName());
                        System.out.println("  Quantity: " + drug.getQuantity());
                        System.out.println("  Status: " + drug.getStatus());
                    }
                }
                System.out.println("--------------------------------------------------");
            }
        }

        if (!found) {
            System.out.println("No appointment outcomes found for patient ID: " + patientID);
        }
    }


    /**
     * Retrieves the patient ID associated with a given appointment ID.
     *
     * @param appointmentID the unique identifier for the appointment
     * @return the patient ID associated with the appointment, or null if no matching appointment is found
     */
    //For doctor
    public String getPatienIDbyAppointmentID(String appointmentID) {
        for (AppointmentInformation appointment : appointments) {
            // Check if the appointment ID matches
            if (String.valueOf(appointment.getAppointmentID()).equals(appointmentID)) {
                return appointment.getPatientID(); // Return the patient ID if found
            }
        }

        // If no appointment matches, return a suitable message or null
        System.out.println("No appointment found with ID: " + appointmentID);
        return null;
    }

    /**
     * Displays all pending appointment requests for a given doctor based on their ID.
     * If no pending requests are found, it prints a message to the console.
     *
     * @param doctorID the unique identifier of the doctor whose pending appointment requests are to be viewed
     * @return true if there are pending appointments for the specified doctor, false otherwise
     */
    public boolean viewRequest(String doctorID) {
        boolean found = false;
        for (AppointmentInformation appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID) && appointment.getAppointmentStatus() == AppointmentStatus.PENDING) {
                displayOneAppointment(appointment);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No pending appointments found for doctor ID: " + doctorID);
            return false;
        }
        return true;
    }

    /**
     * Manages appointment requests by allowing a doctor to update the status of a pending appointment.
     *
     * @param appointmentID The unique identifier of the appointment to be managed.
     * @param doctorID      The unique identifier of the doctor handling the appointment request.
     */
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
                    storageServiceInterface.writeAppointmentsToCsv(appointments);
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

    /**
     * Displays the confirmed appointments for a specific doctor on a given date.
     * If no confirmed appointments are found, a message will be printed indicating so.
     *
     * @param doctorID The unique identifier of the doctor whose schedule is to be viewed.
     * @param date     The specific date for which the doctor's confirmed appointments are to be viewed.
     */
    public void viewDoctorSchedule(String doctorID, String date) {
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


    /**
     * Sets the schedule for a doctor by making a specific time slot available.
     *
     * @param doctorID The unique identifier of the doctor whose schedule is being set.
     * @param Date     The date for which the schedule is being set.
     * @param timeSlot The specific time slot that is being set as available.
     */
    public void setDoctorSchedule(String doctorID, String Date, String timeSlot) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(Date);
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

        if ("unavailable".equals(slotValue)) {  // If slot is available (1)
            schedule.setSlot(timeSlotRow, doctorCol, "available");
            storageServiceInterface.writeScheduleToCSV(schedule, Date);// Occupy the slot with patientID
            //System.out.println("Schedule set successful for doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

        } else {
            System.out.println("Fail to change schedule");
        }

    }

    /**
     * Cancels the schedule of a doctor for a specific time slot on a given date.
     *
     * @param doctorID Identifier of the doctor whose schedule is to be canceled.
     * @param Date     The date for which the doctor's schedule is to be canceled.
     * @param timeSlot The specific time slot that needs to be canceled.
     */
    public void cancelDoctorSchedule(String doctorID, String Date, String timeSlot) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(Date);
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

        if ("available".equals(slotValue)) {  // If slot is available (1)
            schedule.setSlot(timeSlotRow, doctorCol, "unavailable");
            storageServiceInterface.writeScheduleToCSV(schedule, Date);// Occupy the slot with patientID
            //System.out.println("Schedule disabled successfully for doctor " + doctorID + " at " + timeSlot + " on " + "2024-11-01" + ".");//hard code the date here,need change

        } else {
            System.out.println("Fail to change schedule");
        }

    }

    /**
     * Displays the daily schedule of a specific doctor for a given date.
     *
     * @param doctorID the unique identifier of the doctor
     * @param date     the specific date for which the schedule is to be viewed
     * @return true if the doctor's schedule for the given date is found and displayed successfully, false otherwise
     */
    public boolean viewDoctorDailySchedule(String doctorID, String date) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(date);
        String[][] matrix = schedule.getMatrix();
        boolean found = false;

        // Find the column corresponding to the doctorID
        int doctorCol = -1;
        for (int col = 1; col < matrix[0].length; col++) {
            if (matrix[0][col] != null && matrix[0][col].equals(doctorID)) {
                doctorCol = col;
                found = true;
                break;
            }
        }

        // If the doctorID is not found in the schedule
        if (!found) {
            System.out.println("No schedule found for Doctor ID: " + doctorID + " on " + date);
            return false;
        }

        // Print the time slots and the corresponding schedule for the doctor
        /*System.out.println("Schedule for Doctor ID: " + doctorID + " on " + date);
        System.out.println("--------------------------------------------------");
        System.out.println("Time Slot\tDetails");*/

        for (int row = 1; row < matrix.length; row++) { // Skip the header row
            String timeSlot = matrix[row][0]; // First column: Time slot
            String details = matrix[row][doctorCol]; // Corresponding column for doctorID
            if (details.equals("available")) {
                details = Colour.GREEN + "Available" + Colour.RESET;
            } else if (details.equals("unavailable")) {
                details = Colour.RED + "Unavailable" + Colour.RESET;
            } else {
                details = Colour.YELLOW + details + Colour.RESET;
            }
            System.out.println(timeSlot + "\t" + details);
        }

        return true;
    }

    /**
     * Records the outcome of an appointment by creating an AppointmentOutcome object.
     *
     * @param appointmentID        the ID of the appointment
     * @param patientID            the ID of the patient
     * @param typeOfAppointment    the type of the appointment
     * @param consultationNotes    notes from the consultation during the appointment
     * @param prescribedMedication a list of prescribed medications during the appointment
     * @return the created AppointmentOutcome object
     */
    public AppointmentOutcome keyInOutcome(String appointmentID, String patientID, String typeOfAppointment, String consultationNotes, ArrayList<DrugDispenseRequest> prescribedMedication) {

        int dummyID = 1000;
        // Create and return the AppointmentOutcome object
        return new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
    }

    /**
     * Displays the list of confirmed appointments for a specific doctor on a given date.
     *
     * @param date     the specified date in the format yyyyMMdd to filter appointments.
     * @param doctorID the unique identifier of the doctor whose appointments are to be displayed.
     */
    public void displayAppointmentsForDoctor(String date, String doctorID) {
        boolean found = false;
        System.out.println("Appointments for Doctor ID: " + doctorID + " on " + date);
        System.out.println("--------------------------------------------------");

        for (AppointmentInformation appointment : appointments) {
            // Check if the appointment matches the given date and doctor ID
            String appointmentDate = new SimpleDateFormat("yyyyMMdd").format(appointment.getAppointmentTimeSlot());
            if (appointmentDate.equals(date) && appointment.getDoctorID().equals(doctorID) && appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                displayOneAppointment(appointment);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found for Doctor ID: " + doctorID + " on " + date);
        }
    }

    /**
     * Displays all confirmed appointments for a given doctor based on their doctor ID.
     * If no confirmed appointments are found, it will print a corresponding message.
     *
     * @param doctorID The unique identifier of the doctor for whom the confirmed appointments
     *                 are to be displayed.
     * @return true if at least one confirmed appointment is found for the given doctor ID,
     * false otherwise.
     */
    public boolean displayAllAppointmentsForDoctor(String doctorID) {
        boolean found = false;
        System.out.println("All Confirmed Appointments for Doctor ID: " + doctorID);
        System.out.println("--------------------------------------------------");

        for (AppointmentInformation appointment : appointments) {
            // Check if the appointment matches the given doctor ID and is CONFIRMED
            if (appointment.getDoctorID().equals(doctorID) && appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                displayOneAppointment(appointment);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No confirmed appointments found for Doctor ID: " + doctorID);
        }
        return found;
    }


    /**
     * Adds a new appointment outcome to the list of appointment outcomes.
     *
     * @param outcome The appointment outcome to add.
     */
    public void addAppointmentOutcome(AppointmentOutcome outcome) {
        appointmentOutcomes.add(outcome);
        //Need to add a function to write the new outcome to last row of CSV
    }

    /**
     * Displays all appointments available in the system. This method iterates over
     * the list of appointments and calls the displayOneAppointment method to
     * output the details of each appointment. This functionality is intended for
     * administrative users.
     */
    //For admin
    public void displayAllAppointments() {
        for (AppointmentInformation appointment : appointments) {
            displayOneAppointment(appointment);
        }
    }


    /**
     * Creates a new appointment outcome and records it.
     *
     * @param appointmentID        The ID of the appointment.
     * @param patientID            The ID of the patient.
     * @param typeOfAppointment    The type of the appointment (e.g., consultation, follow-up).
     * @param consultationNotes    Notes from the consultation.
     * @param prescribedMedication List of medications prescribed during the appointment.
     * @return The newly created AppointmentOutcome object.
     */
    //For pharmacist
    //These three methods will be used together to create a new appointment outcome
    public AppointmentOutcome createNewAppointmentOutcome(String appointmentID, String patientID, String typeOfAppointment, String consultationNotes, ArrayList<DrugDispenseRequest> prescribedMedication) {
        AppointmentOutcome outcome = new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
        appointmentOutcomes.add(outcome);
        storageServiceInterface.writeAppointmentOutcomeToCSV(outcome);

        return outcome;
    }

    /**
     * Creates a new ArrayList of DrugDispenseRequest objects.
     *
     * @return A new ArrayList containing DrugDispenseRequest objects.
     */
    public ArrayList<DrugDispenseRequest> createNewArrayOfDrugDispenseRequest() {
        ArrayList<DrugDispenseRequest> newList = new ArrayList<>();
        return newList;
    }

    /**
     * Adds a new drug dispense request to the given list of requests.
     *
     * @param list        The list of DrugDispenseRequest objects to add the new request to.
     * @param drugName    The name of the drug to be dispensed.
     * @param addQuantity The quantity of the drug to be dispensed.
     */
    public void addDrugDispenseRequest(ArrayList<DrugDispenseRequest> list, String drugName, int addQuantity) {
        // Create a new DrugDispenseRequest instance
        DrugDispenseRequest drugRequest = storageServiceInterface.createNewDrugDispenseRequest(drugName, addQuantity);
        list.add(drugRequest);
    }
    //


    /**
     * Updates the status of the prescribed medications for a given appointment ID.
     * If any medication is in pending status, it is updated to dispensed.
     *
     * @param appointmentID the ID of the appointment for which the prescription status needs to be updated
     * @return true if at least one medication status was updated, otherwise false
     */
    public boolean updatePrescriptionStatus(String appointmentID) {
        boolean updated = false;

        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.getAppointmentID().equals(appointmentID)) {
                for (DrugDispenseRequest drugRequest : outcome.getPrescribedMedication()) {
                    if (drugRequest.getStatus() == DrugRequestStatus.PENDING) {
                        //add condition here if don't want to dispense all
                        drugRequest.setStatus(DrugRequestStatus.DISPENSED);
                        updated = true;  // Mark as updated if any status changes
                    }
                }
                break;
            }
        }

        return true;
    }

    /**
     * Updates all appointment outcomes by writing them into a CSV file.
     *
     * @return a boolean indicating whether the operation was successful
     */
    public boolean updateAppointmentOutcometoCSV() {
        boolean updated = false;
        storageServiceInterface.writeAllAppointmentOutcomesToCSV(appointmentOutcomes);
        updated = true;
        return updated;

    }

    /**
     * Displays the list of pending prescriptions for all appointment outcomes.
     * The method iterates over the stored appointment outcomes and checks
     * each prescribed medication to determine if any drugs are pending.
     * If there are pending drugs, their details are printed to the console.
     * Otherwise, it indicates that there are no pending drugs for the appointment.
     */
    public void displayPendingPrescriptions() {
        System.out.println("Pending Prescriptions:");
        int havePendingDrug = 0;
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            System.out.println("Appointment ID: " + outcome.getAppointmentID());
            System.out.println("Patient ID: " + outcome.getPatientID());
            for (DrugDispenseRequest drugRequest : outcome.getPrescribedMedication()) {
                if (drugRequest.getStatus() == DrugRequestStatus.PENDING) {
                    havePendingDrug = 1;
                    System.out.println("Drug Name: " + drugRequest.getDrugName());
                    System.out.println("Quantity: " + drugRequest.getQuantity());
                    System.out.println("Status: " + drugRequest.getStatus());
                    System.out.println();
                }
            }
            if (havePendingDrug == 0) {
                System.out.println("No Pending Drug");

            }
            System.out.println("------------------------");
            havePendingDrug = 0;
        }
    }

    /**
     * Returns a list of patient IDs that have pending drug requests.
     *
     * @return A list of patient IDs as strings that have pending drug requests.
     */
    public List<String> getPatientIDsWithPendingDrugRequest() {
        List<String> patientIDs = new ArrayList<>();

        for (AppointmentOutcome outcome : appointmentOutcomes) {
            for (DrugDispenseRequest drugRequest : outcome.getPrescribedMedication()) {
                if (drugRequest.getStatus() == DrugRequestStatus.PENDING) {
                    if (!patientIDs.contains(outcome.getPatientID())) {   // check if they are already in the list
                        patientIDs.add(outcome.getPatientID());    //append if they are not in the list
                    }
                    break; // Stop checking other drug requests for this appointment since we found a pending one.
                }
            }
        }

        return patientIDs;
    }

    /**
     * Retrieves a list of appointment outcomes that have a pending prescription for a given patient ID.
     *
     * @param patientID the ID of the patient for whom to retrieve appointment outcomes with pending prescriptions
     * @return a list of AppointmentOutcome objects that have pending prescriptions for the specified patient
     */
    public List<AppointmentOutcome> getAppointmentOutcomesPendingPrescriptionByPatientID(String patientID) {
        List<AppointmentOutcome> outcomes = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.getPatientID().equals(patientID)) {
                for (DrugDispenseRequest drugRequest : outcome.getPrescribedMedication()) {
                    if (drugRequest.getStatus() == DrugRequestStatus.PENDING) {
                        outcomes.add(outcome);
                        break;
                    }
                }
            }
        }

        return outcomes;
    }

    /**
     * Retrieves a list of pending drug dispense requests for the specified patient ID.
     *
     * @param patientID the unique identifier of the patient whose drug requests are to be retrieved.
     * @return an ArrayList containing DrugDispenseRequest objects that are pending for the specified patient.
     * If there are no pending requests, an empty list is returned with a console message indicating
     * no pending requests for the given patient ID.
     */
    public ArrayList<DrugDispenseRequest> getDrugRequestsByPatientID(String patientID) {
        ArrayList<DrugDispenseRequest> pendingDrugRequests = new ArrayList<>();
        ArrayList<DrugDispenseRequest> drugRequests = new ArrayList<>();
        for (AppointmentOutcome outcome : appointmentOutcomes) {
            if (outcome.getPatientID().equals(patientID)) {
                drugRequests = outcome.getPrescribedMedication();
                for (DrugDispenseRequest drugRequest : drugRequests) {
                    if (drugRequest.getStatus() == DrugRequestStatus.PENDING) {
                        pendingDrugRequests.add(drugRequest);
                    }
                }
            }
        }

        if (pendingDrugRequests.isEmpty()) {
            System.out.println("No pending drug requests for Patient ID: " + patientID);
        }
        return pendingDrugRequests;
    }

    /**
     * Updates the status of an appointment to COMPLETED if it is currently CONFIRMED.
     *
     * @param appointmentID The unique identifier of the appointment to be completed.
     * @param doctorID      The identifier of the doctor associated with the appointment.
     * @return {@code true} if the appointment status was successfully updated to COMPLETED, {@code false} otherwise.
     */
    //For update appointmentStatus to COMPLETED
    public boolean completeAnAppointment(String appointmentID, String doctorID) {
        boolean updated = false;
        for (AppointmentInformation appointment : appointments) {
            // Check if the appointmentID and doctorID match
            if (String.valueOf(appointment.getAppointmentID()).equals(appointmentID)
                    && appointment.getDoctorID().equals(doctorID)) {

                // Check if the appointmentStatus is CONFIRMED
                if (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
                    // Update the status to COMPLETED
                    appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
                    storageServiceInterface.writeAppointmentsToCsv(appointments);
                    System.out.println("Appointment ID: " + appointmentID + " for Doctor ID: " + doctorID + " has been completed.");
                    updated = true;
                } else {
                    System.out.println("Appointment ID: " + appointmentID + " is not in CONFIRMED status and cannot be completed.");
                }
                break; // Exit the loop as the appointment has been found
            }
        }

        if (!updated) {
            System.out.println("No matching appointment found for Appointment ID: " + appointmentID + " and Doctor ID: " + doctorID);
        }

        return updated;
    }

    /**
     * Adds a new doctor to the appointment schedule for the specified date.
     * This method updates the schedule by adding the doctor to the matrix and setting
     * all their time slots to "available".
     *
     * @param doctorID The unique identifier of the doctor to be added to the schedule.
     * @param date     The date of the schedule to which the doctor is to be added.
     */
    //For admin to add staff and update to the schedule
    public void addNewDoctorToSchedule(String doctorID, String date) {
        AppointmentSchedule schedule = storageServiceInterface.loadSchedule(date);

        String[][] oldMatrix = schedule.getMatrix();
        int oldRows = oldMatrix.length;
        int oldCols = oldMatrix[0].length;

        String[][] newMatrix = new String[oldRows][oldCols + 1];

        // Copy old matrix
        for (int i = 0; i < oldRows; i++) {
            System.arraycopy(oldMatrix[i], 0, newMatrix[i], 0, oldCols);
        }

        newMatrix[0][oldCols] = doctorID;

        // Set all time slots for the new doctor as "Available"
        for (int i = 1; i < oldRows; i++) {
            newMatrix[i][oldCols] = "available";
        }

        // Update the schedule's matrix
        schedule.setMatrix(newMatrix);

        // Save the updated schedule back to the storage service
        storageServiceInterface.writeScheduleToCSV(schedule, date);
    }

    /**
     * Updates all scheduled appointments with a new doctor.
     *
     * @param doctorID the unique identifier of the new doctor to be added to all schedules.
     */
    public void updateAllSchedulesWithNewDoctor(String doctorID) {

        File[] csvFiles = storageServiceInterface.getAllDateFile();

        for (File csvFile : csvFiles) {
            try {
                String fileName = csvFile.getName();
                String date = fileName.substring(0, fileName.indexOf(".csv"));

                addNewDoctorToSchedule(doctorID, date);

                System.out.println("Updated schedule for date: " + date);
            } catch (Exception e) {

                System.err.println("Failed to update schedule for file: " + csvFile.getName());
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if a schedule exists for the given date.
     *
     * @param date The date in "yyyyMMdd" format for which the schedule existence needs to be checked.
     * @return true if the schedule exists for the given date, false otherwise.
     */
    public boolean checkExistingSchedule(String date) {
        if (!date.matches("\\d{8}")) {
            System.out.println("Invalid date format. Please use yyyyMMdd.");
            return false;
        }

        return storageServiceInterface.checkScheduleExist(date);

    }

    /**
     * Initializes a new schedule for the given date if it does not already exist.
     *
     * @param date The date for which the schedule is to be created, formatted as YYYYMMDD.
     */
    public void createNewSchedule(String date) {
        storageServiceInterface.initializeSchedule(date);

    }
}


