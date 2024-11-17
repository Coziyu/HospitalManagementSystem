package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.*;
import org.hms.services.drugdispensary.DrugInventoryTable;
import org.hms.services.drugdispensary.DrugReplenishRequestTable;
import org.hms.services.drugdispensary.IDrugStockDataInterface;
import org.hms.services.drugdispensary.*;

import org.hms.services.medicalrecord.*;
import org.hms.services.staffmanagement.Staff;
import org.hms.services.staffmanagement.StaffTable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The StorageService class manages the storage and retrieval of various healthcare data tables.
 * It extends AbstractService and implements IMedicalDataInterface, IAppointmentDataInterface,
 * and IDrugStockDataInterface to provide a comprehensive storage solution for medical records,
 * appointments, drug inventory, and other related data.
 */
public class StorageService
        extends AbstractService<IDataInterface>
        implements IMedicalDataInterface, IAppointmentDataInterface, IDrugStockDataInterface {

    /**
     * The root directory for storing data files.
     * This directory is constructed based on the user's current working directory.
     */
    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    /**
     * Counter to track the number of drug dispense requests made.
     * This field is used to generate unique identifiers for each drug dispense request.
     * Note: This implementation is a temporary solution and should be refactored.
     */
    // TODO: THIS IS A DIRTY HACK! REFACTOR IT ASAP
    private static int drugDispenseRequestCounter = 0;
    /**
     * Represents the table that manages the drug inventory.
     * This field holds an instance of the DrugInventoryTable providing methods
     * to manipulate and access drug inventory data.
     */
    private DrugInventoryTable drugInventoryTable;
    /**
     * A private field in the StorageService that manages drug replenish requests.
     * It utilizes the DrugReplenishRequestTable class to store and handle operations related to drug replenishments.
     */
    private DrugReplenishRequestTable drugReplenishRequestTable;
    /**
     * Represents a table of medical records within the storage service.
     * This variable stores an instance of the MedicalRecord class,
     * which contains a collection of medical entries.
     */
    private MedicalRecord medicalRecordTable;
    /**
     * Represents the table containing patient particulars.
     * This table is responsible for managing and maintaining
     * entries related to patient data, including patient ID,
     * name, birthdate, gender, and blood type.
     */
    private PatientTable patientParticularsTable;
    /**
     * Holds the table of contact information, which includes entries
     * such as phone numbers, emails, and addresses associated with patients.
     * This table provides the functionality to manage, format, and
     * render contact information entries.
     */
    private ContactInformationTable contactInformationTable;
    /**
     * Represents the table containing all staff-related information.
     * This table is used to store and manage data pertaining to staff members,
     * including their details, roles, and other pertinent information required
     * for hospital management and scheduling.
     */
    private StaffTable staffTable;

    /**
     * Constructs a StorageService instance and initializes various data tables necessary for the application.
     * <p>
     * The constructor is responsible for initializing the following tables:
     * - Drug Inventory Table
     * - Drug Replenish Request Table
     * - Medical Record Table
     * - Patient Particulars Table
     * - Contact Information Table
     * - Staff Table
     * <p>
     * Each table is initialized by invoking its respective initialization method that loads data from a CSV file.
     */
    public StorageService() {
        storageServiceInterface = this;
        initializeDrugInventoryTable();
        initializeDrugReplenishRequestTable();
        initializeMedicalRecordTable();
        initializePatientParticularsTable();
        initializeContactInformationTable();
        initializeStaffTable();
    }

    /**
     * Initializes the staff table by loading data from the staff CSV file.
     * The CSV file is located at the path specified by the dataRoot field.
     * If an IOException occurs during the loading process, it is wrapped
     * in a RuntimeException and thrown.
     */
    private void initializeStaffTable() {
        staffTable = new StaffTable(dataRoot + "staff.csv");
        try {
            staffTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initializes the contact information table by loading data from a CSV file.
     * It constructs a new ContactInformationTable instance using a file path
     * provided by the `dataRoot` field and attempts to load the contact data.
     *
     * @throws RuntimeException if an IOException occurs while loading the file
     */
    private void initializeContactInformationTable() {
        contactInformationTable = new ContactInformationTable(dataRoot + "contact_information.csv");
        try {
            contactInformationTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the patient particulars table by loading patient data from a CSV file.
     * <p>
     * This method sets up the `patientParticularsTable` field by creating a new instance
     * of `PatientTable` using the path to the `PatientList.csv` file. It then attempts
     * to load the data from the specified file. If an I/O error occurs during this process,
     * a `RuntimeException` is thrown.
     * </p>
     * <p>
     * The `PatientTable` class is designed to handle the storage and management of patient
     * particulars. It reads from the CSV file and populates the table accordingly.
     * </p>
     *
     * @throws RuntimeException if there is an issue with loading the patient data from the file.
     */
    private void initializePatientParticularsTable() {
        patientParticularsTable = new PatientTable(dataRoot + "PatientList.csv");
        try {
            patientParticularsTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the DrugReplenishRequestTable by setting its file path to the specified CSV file
     * and loading its contents from the file.
     * If an I/O error occurs while loading the data, a RuntimeException is thrown.
     */
    private void initializeDrugReplenishRequestTable() {
        drugReplenishRequestTable = new DrugReplenishRequestTable(dataRoot + "drugReplenishRequests.csv");
        try {
            drugReplenishRequestTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the drug inventory table by linking it to a CSV file and loading its contents.
     * This method sets up the `drugInventoryTable` using the path provided by the `dataRoot`,
     * and calls `loadFromFile` to populate it with data from the specified file.
     * If an IOException occurs during the file loading process, a RuntimeException is thrown.
     */
    private void initializeDrugInventoryTable() {
        drugInventoryTable = new DrugInventoryTable(dataRoot + "drugInventory.csv");
        try {
            drugInventoryTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the medical records table for the application.
     * This method sets up the medicalRecordTable by loading data from the specified CSV file.
     * If an I/O error occurs while loading the file, it throws a RuntimeException.
     */
    private void initializeMedicalRecordTable() {
        medicalRecordTable = new MedicalRecord(dataRoot + "medical_records.csv");
        try {
            medicalRecordTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Retrieves a list of appointment IDs that are pending for dispensary.
     *
     * @return ArrayList of appointment IDs that are pending dispensary.
     */
    @Override
    public ArrayList<String> getAppointmentsPendingDispensary() {
        // TODO: Implement fetching from AppointmentService
        ArrayList<String> appointmentsPendingDispensary = new ArrayList<>();
        appointmentsPendingDispensary.add("A0001");
        appointmentsPendingDispensary.add("A0002");
        appointmentsPendingDispensary.add("A0003");
        return appointmentsPendingDispensary;
    }

    /**
     * Retrieves the current drug inventory table managed by the storage service.
     *
     * @return An instance of DrugInventoryTable representing the current drug inventory.
     */
    @Override
    public DrugInventoryTable getDrugInventory() {
        return drugInventoryTable;
    }

    /**
     * Retrieves the table used for managing and storing drug replenish requests.
     *
     * @return an instance of DrugReplenishRequestTable representing the drug replenish requests table.
     */
    @Override
    public DrugReplenishRequestTable getDrugReplenishRequestTable() {
        return drugReplenishRequestTable;
    }

    /**
     * Retrieves the staff table.
     *
     * @return the current instance of StaffTable containing staff information.
     */
    @Override
    public StaffTable getStaffTable() {
        return staffTable;
    }

    /**
     * Retrieves the patient particulars table.
     *
     * @return an instance of PatientTable containing patient-related data.
     */
    @Override
    public PatientTable getPatientTable() {
        return patientParticularsTable;
    }

    /**
     * Retrieve the table containing medical records.
     *
     * @return MedicalRecord table containing medical entries.
     */
    @Override
    public MedicalRecord getMedicalRecordTable() {
        return medicalRecordTable;
    }

    /**
     * Retrieves the ContactInformationTable, which contains and manages contact information entries
     * such as phone numbers, emails, and addresses associated with patients.
     *
     * @return an instance of ContactInformationTable providing access to contact information entries.
     */
    @Override
    public ContactInformationTable getContactInformationTable() {
        return contactInformationTable;
    }

    /**
     * Creates a new DrugDispenseRequest with the specified drug name and quantity, and assigns it a unique ID.
     *
     * @param drugName    The name of the drug to be dispensed.
     * @param addQuantity The quantity of the drug to be added to the dispense request.
     * @return A newly created DrugDispenseRequest object with the given drug name and quantity, set to pending status.
     */
    public DrugDispenseRequest createNewDrugDispenseRequest(String drugName, int addQuantity) {
        //TODO: THIS IS A DIRTY HACK! REFACTOR IT ASAP
        // Requires cooperation with Yingjie for this
        DrugDispenseRequest newDispenseRequest = new DrugDispenseRequest(drugDispenseRequestCounter, drugName, addQuantity, DrugRequestStatus.PENDING);
        drugDispenseRequestCounter += 1;
        return newDispenseRequest;
    }

    /**
     * Reads appointment data from a CSV file and returns a list of appointment information.
     * The method parses the CSV file line by line, extracting details for each appointment
     * and creating corresponding AppointmentInformation objects.
     *
     * @return List of AppointmentInformation objects representing the appointments read from the CSV file.
     */

    public List<AppointmentInformation> readAppointments() {
        List<AppointmentInformation> appointments = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(dataRoot + "Appointment/" + "Appointments.csv"))) {
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assumes comma as delimiter
                // Parse values and create an AppointmentInformation object
                int appointmentID = Integer.parseInt(values[0]);
                String patientID = values[1];
                String doctorID = values[2];
                Date appointmentTimeSlot = new SimpleDateFormat("yyyy-MM-dd HH:mm-HH:mm").parse(values[3]);
                AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(values[4]);

                appointments.add(new AppointmentInformation(appointmentID, patientID, doctorID, appointmentTimeSlot, appointmentStatus));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Writes a list of appointments to a CSV file at the designated location.
     *
     * @param appointments List of {@link AppointmentInformation} objects representing the appointments to be written to the CSV file.
     */
    public void writeAppointmentsToCsv(List<AppointmentInformation> appointments) {
        String filePath = dataRoot + "Appointment/" + "Appointments.csv";
        SimpleDateFormat timeSlotFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm-HH:mm");

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the header row
            writer.write("appointmentID,patientID,doctorID,appointmentTimeSlot,appointmentStatus\n");

            // Write each appointment's details
            for (AppointmentInformation appointment : appointments) {
                // Format the appointment time slot correctly
                String formattedTimeSlot = timeSlotFormat.format(appointment.getAppointmentTimeSlot());

                writer.write(appointment.getAppointmentID() + "," +
                        appointment.getPatientID() + "," +
                        appointment.getDoctorID() + "," +
                        formattedTimeSlot + "," +
                        appointment.getAppointmentStatus() + "\n");
            }

//            System.out.println("Appointments successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Loads the appointment schedule for a specific date from a CSV file.
     *
     * @param date The date for which the schedule should be loaded, in the format "YYYY-MM-DD".
     * @return An AppointmentSchedule object containing the loaded schedule data.
     */
    public AppointmentSchedule loadSchedule(String date) {
        int numRows = 0;
        int numCols = 0;

        String filePath = dataRoot + "Appointment/schedules/" + date + ".csv";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                numRows++;
                String[] values = line.split(",");
                numCols = Math.max(values.length, numCols);
            }
            br.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        AppointmentSchedule schedule = new AppointmentSchedule(numCols - 1, numRows - 1);

        try {
            BufferedReader br = new BufferedReader(new FileReader(dataRoot + "Appointment/schedules/" + date + ".csv"));

            String line;

            for (int row = 0; (line = br.readLine()) != null; ++row) {
                String[] values = line.split(",");

                for (int col = 0; col < values.length; ++col) {
                    schedule.getMatrix()[row][col] = values[col];
                }
            }

            br.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return schedule;
    }

    /**
     * Writes the scheduling matrix of an AppointmentSchedule object to a CSV file.
     *
     * @param schedule The AppointmentSchedule object containing the scheduling matrix to be written.
     * @param date     The date used to name the CSV file, indicating the schedule's date.
     */
    public void writeScheduleToCSV(AppointmentSchedule schedule, String date) {
        String[][] matrix = schedule.getMatrix();  // Retrieve the matrix from the AppointmentSchedule object
        //String fileName = date + ".csv";  // Use the date to create the file name
        String filePath = dataRoot + "Appointment/schedules/" + date + ".csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : matrix) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != null) {
                        writer.write(row[i]);
                    }
                    if (i < row.length - 1) {
                        writer.write(",");  // Add a comma between columns
                    }
                }
                writer.write("\n");  // Newline after each row
            }
//            System.out.println("Matrix written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the outcome of an appointment to a CSV file. The method captures details such as
     * appointment ID, patient ID, type of appointment, consultation notes, and prescribed medications.
     *
     * @param outcome The outcome of the appointment, encapsulating appointment ID, patient ID,
     *                type of appointment, consultation notes, and prescribed medications.
     */
    public void writeAppointmentOutcomeToCSV(AppointmentOutcome outcome) {
        try (FileWriter writer = new FileWriter(dataRoot + "Appointment/" + "AppointmentOutcome.csv", true)) { // Append mode

            // Prepare data fields for the AppointmentOutcome
            String appointmentID = outcome.getAppointmentID();
            String patientID = outcome.getPatientID();
            String typeOfAppointment = outcome.getTypeOfAppointment();

            // Handle consultation notes by escaping commas and slashes
            String sanitizedNotes = outcome.getConsultationNotes().replace(",", ";").replace("/", " or ");

            // Begin constructing the row for the CSV
            StringBuilder rowData = new StringBuilder();
            rowData.append(appointmentID).append(",")
                    .append(patientID).append(",")
                    .append(typeOfAppointment).append(",")
                    .append(sanitizedNotes);

            // Add each drug name and quantity to separate slots in the CSV row
            for (DrugDispenseRequest request : outcome.getPrescribedMedication()) {
                rowData.append(",").append(request.getDrugName()) // Drug name slot
                        .append(",").append(request.getQuantity())
                        .append(",").append("PENDING");
            }

            // Write the row to the CSV and add a new line
            writer.write(rowData.toString());
            writer.write("\n"); // New line for each record

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads appointment outcomes from a CSV file and returns a list of AppointmentOutcome objects.
     * Each line in the CSV represents an appointment outcome with various fields such as
     * appointment ID, patient ID, type of appointment, consultation notes, and prescribed medications.
     * <p>
     * The method handles potential IOExceptions and formats errors gracefully.
     *
     * @return an ArrayList of AppointmentOutcome objects parsed from the CSV file.
     */
    public ArrayList<AppointmentOutcome> readAppointmentOutcomesFromCSV() {
        ArrayList<AppointmentOutcome> appointmentOutcomes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(dataRoot + "Appointment/" + "AppointmentOutcome.csv"))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Split the line by comma
                String[] fields = line.split(",");

                // Parse the basic AppointmentOutcome fields
                String appointmentID = fields[0].trim();
                String patientID = fields[1].trim();
                String typeOfAppointment = fields[2].trim();
                String consultationNotes = fields[3].replace(";", ",").replace(" or ", "/").trim();

                // Parse the prescribed medications, starting from index 4
                ArrayList<DrugDispenseRequest> prescribedMedication = new ArrayList<>();
                for (int i = 4; i < fields.length; i += 3) {
                    if (i + 2 >= fields.length) break; // Ensure there are enough fields for drug data

                    String drugName = fields[i].trim();
                    int quantity = Integer.parseInt(fields[i + 1].trim());
                    DrugRequestStatus status = DrugRequestStatus.valueOf(fields[i + 2].trim());

                    DrugDispenseRequest drugRequest = new DrugDispenseRequest(i / 3, drugName, quantity, status);
                    prescribedMedication.add(drugRequest);
                }

                // Create an AppointmentOutcome object and add it to the list
                AppointmentOutcome outcome = new AppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);
                appointmentOutcomes.add(outcome);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Error parsing the CSV file: " + e.getMessage());
        }

        return appointmentOutcomes;
    }

    /**
     * Writes a list of {@code AppointmentOutcome} objects to a CSV file.
     *
     * @param appointmentOutcomes an {@code ArrayList} of {@code AppointmentOutcome} objects representing
     *                            the outcomes of various appointments.
     */
    public void writeAllAppointmentOutcomesToCSV(ArrayList<AppointmentOutcome> appointmentOutcomes) {
        try (FileWriter writer = new FileWriter(dataRoot + "Appointment/" + "AppointmentOutcome.csv", false)) { // Overwrite mode

            // Step 1: Write the header line
            writer.write("AppointmentID,PatientID,TypeOfAppointment,ConsultationNotes,DrugName,Quantity,Status\n");

            // Step 2: Write each AppointmentOutcome to the CSV file
            for (AppointmentOutcome outcome : appointmentOutcomes) {
                StringBuilder rowData = new StringBuilder();
                rowData.append(outcome.getAppointmentID()).append(",")
                        .append(outcome.getPatientID()).append(",")
                        .append(outcome.getTypeOfAppointment()).append(",")
                        .append(outcome.getConsultationNotes().replace(",", ";").replace("/", " or "));

                // Add each drug name, quantity, and status as separate columns
                for (DrugDispenseRequest request : outcome.getPrescribedMedication()) {
                    rowData.append(",").append(request.getDrugName())
                            .append(",").append(request.getQuantity())
                            .append(",").append(request.getStatus().toString());
                }

                // Write the row to the CSV and add a new line
                writer.write(rowData.toString());
                writer.write("\n");
            }

//            System.out.println("All AppointmentOutcomes have been updated in CSV file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all CSV files containing appointment scheduling data from the specified directory.
     * The method fetches files from the directory path constructed using the root data directory
     * and the "Appointment/schedules/" subpath.
     *
     * @return An array of File objects representing all CSV files in the specified directory.
     * If no CSV files are found or the directory is invalid, the method returns null.
     * @throws IllegalArgumentException if the directory does not exist or is not a valid directory.
     */
    public File[] getAllDateFile() {
        String directoryPath = dataRoot + "Appointment/schedules/";
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        File[] csvFiles = directory.listFiles((dir, name) -> name.endsWith(".csv"));

        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No CSV files found in the directory: " + directoryPath);
        }
        return csvFiles;
    }

    /**
     * Retrieves the Staff object associated with the given staff ID.
     *
     * @param staffId The ID of the staff member whose information is to be fetched.
     * @return The Staff object corresponding to the given staff ID, or null if no such staff member exists.
     */
    public Staff getStaffForSchedule(String staffId) {
        StaffTable staffTable = new StaffTable();
        return staffTable.getEntries().stream()
                .filter(staff -> staff.getStaffId().equals(staffId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the name of the staff member corresponding to the given user ID.
     *
     * @param userId the ID of the staff member whose name is to be retrieved
     * @return the name of the staff member with the provided ID, or null if no such staff member is found
     */
    @Override
    public String getStaffNameByID(String userId) {
        ArrayList<Staff> target = staffTable.searchByAttribute(Staff::getStaffId, userId);
        if (target.isEmpty()) {
            return null;
        }
        return target.getFirst().getName();
    }

    /**
     * Checks if a schedule file exists for the given date.
     *
     * @param date The date in "yyyyMMdd" format for which the schedule existence needs to be verified.
     * @return true if a schedule file exists for the given date, false otherwise.
     */
    public boolean checkScheduleExist(String date) {

        String folderPath = dataRoot + "Appointment/schedules/";
        String fileName = date + ".csv";
        File file = new File(folderPath + fileName);

        return file.exists();
    }

    /**
     * Initializes the appointment schedule for the specified date by copying a template schedule file.
     *
     * @param date The date for which to initialize the schedule, formatted as YYYYMMDD.
     */
    public void initializeSchedule(String date) {
        String folderPath = dataRoot + "Appointment/schedules/";
        String templateFilepath = folderPath + "templateSchedule.csv";
        String newFilePath = folderPath + date + ".csv";

        File templateFile = new File(templateFilepath);
        File newFile = new File(newFilePath);

        try {
            // Ensure the folder exists
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // Create directories if they don't exist
            }

            // Check if the new file already exists
            if (newFile.exists()) {
                System.out.println("Schedule for " + date + " already exists.");
                return;
            }

            // Copy templateSchedule.csv to date.csv
            Files.copy(templateFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Initialized schedule for " + date + " successfully at: " + newFilePath);
        } catch (IOException e) {
            System.out.println("Error initializing schedule for " + date + ": " + e.getMessage());
        }
    }

}

