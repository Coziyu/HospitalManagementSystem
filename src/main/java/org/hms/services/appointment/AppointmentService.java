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

public class AppointmentService extends AbstractService<IAppointmentDataInterface> {

    private List<AppointmentInformation> appointments;
    private AppointmentSchedule appointmentSchedule;
    private ArrayList<AppointmentOutcome> appointmentOutcomes;


    public AppointmentService(IAppointmentDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        StorageService storageService = new StorageService();
        appointments = storageService.readAppointments();
        appointmentOutcomes = storageService.readAppointmentOutcomesFromCSV();
    }

    public void displayOneAppointment(AppointmentInformation appointment) {
        System.out.println("Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Patient ID: " + appointment.getPatientID());
        System.out.println("Doctor ID: " + appointment.getDoctorID());
        System.out.println("Appointment Time Slot: " + appointment.getAppointmentTimeSlot());
        System.out.println("Appointment Status: " + appointment.getAppointmentStatus());
        System.out.println("--------------------------------------------------");
    }

    public AppointmentSchedule getAppointmentSchedule(String date) {
        return storageServiceInterface.loadSchedule(date);
    }

    //For patient
    public String getDoctorID(String patientID) {
        for (AppointmentInformation appointment : appointments) {
            // Check if the patientID matches
            if (appointment.getPatientID().equals(patientID)) {
                if((appointment.getAppointmentStatus() == AppointmentStatus.PENDING) || (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED)){
                // Return the doctor's ID for the matching appointment
                return appointment.getDoctorID();}
            }
        }
        // If no appointment is found for the given patientID
        System.out.println("No appointment found for patient ID: " + patientID);
        return null; // or throw an exception if preferred
    }

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
            // DrugDispenseRequest drugRequest = new DrugDispenseRequest(drugName, addQuantity, notes);
            // TODO: Consider adding a drugNotes field to the DrugDispenseRequest
            DrugDispenseRequest drugRequest = storageServiceInterface.createNewDrugDispenseRequest(drugName, addQuantity);

            prescribedMedication.add(drugRequest);
        }

        // Create and return the AppointmentOutcome object
        return new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
    }*/

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

    public AppointmentOutcome keyInOutcome(String appointmentID, String patientID, String typeOfAppointment, String consultationNotes, ArrayList<DrugDispenseRequest> prescribedMedication) {

        int dummyID = 1000;
        // Create and return the AppointmentOutcome object
        return new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
    }

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



    public void addAppointmentOutcome(AppointmentOutcome outcome) {
        appointmentOutcomes.add(outcome);
        //Need to add a function to write the new outcome to last row of CSV
    }

    //For admin
    public void displayAllAppointments() {
        for (AppointmentInformation appointment : appointments) {
            displayOneAppointment(appointment);
        }
    }


    //For pharmacist
    //These three methods will be used together to create a new appointment outcome
    public AppointmentOutcome createNewAppointmentOutcome(String appointmentID, String patientID, String typeOfAppointment, String consultationNotes, ArrayList<DrugDispenseRequest> prescribedMedication) {
        AppointmentOutcome outcome = new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
        appointmentOutcomes.add(outcome);
        storageServiceInterface.writeAppointmentOutcomeToCSV(outcome);

        return outcome;
    }

    public ArrayList<DrugDispenseRequest> createNewArrayOfDrugDispenseRequest() {
        ArrayList<DrugDispenseRequest> newList = new ArrayList<>();
        return newList;
    }

    public void addDrugDispenseRequest(ArrayList<DrugDispenseRequest> list, String drugName, int addQuantity) {
        // Create a new DrugDispenseRequest instance
        DrugDispenseRequest drugRequest = storageServiceInterface.createNewDrugDispenseRequest(drugName, addQuantity);
        list.add(drugRequest);
    }
    //


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

    //TODO: For nich to call this method
    public boolean updateAppointmentOutcometoCSV() {
        boolean updated = false;
        storageServiceInterface.writeAllAppointmentOutcomesToCSV(appointmentOutcomes);
        updated = true;
        return updated;

    }

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

    //TODO: For yingjie: I want a function that returns a list of patientIDs that
    // have pending prescriptions.
    // Returns: List<String>
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
     * A function that returns a list of appointment outcomes that have pending prescriptions
     * for a given patient.
     * @param patientID
     * @return List<AppointmentOutcome> of AppointmentOutcomes with pending prescriptions
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

    //TODO: For yingjie: I want a function that returns takes in a patientID
    // and returns a list of pending prescriptions for that patient.
    // Takesin: String patientID, Returns: List<DrugDispenseRequest>
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

    public boolean checkExistingSchedule(String date) {
        if (!date.matches("\\d{8}")) {
            System.out.println("Invalid date format. Please use yyyyMMdd.");
            return false;
        }

        return storageServiceInterface.checkScheduleExist(date);

    }

    public void createNewSchedule(String date) {
        storageServiceInterface.initializeSchedule(date);

    }
}


