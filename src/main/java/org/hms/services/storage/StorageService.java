package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.*;
import org.hms.services.drugdispensary.DrugInventoryTable;
import org.hms.services.drugdispensary.DrugReplenishRequestTable;
import org.hms.services.drugdispensary.IDrugStockDataInterface;
import org.hms.services.drugdispensary.*;

import org.hms.services.medicalrecord.ContactInformation;
import org.hms.services.medicalrecord.IMedicalDataInterface;
import org.hms.services.medicalrecord.MedicalEntry;
import org.hms.services.medicalrecord.MedicalRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;
import java.util.List;

//TODO: Rename var from var to something meaningful

public class StorageService
        extends AbstractService<IDataInterface>
        implements IMedicalDataInterface, IAppointmentDataInterface, IDrugStockDataInterface {

    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    private DrugInventoryTable drugInventoryTable;
    private DrugReplenishRequestTable drugReplenishRequestTable;
    private MedicalRecord medicalRecordTable;

    // TODO: THIS IS A DIRTY HACK! REFACTOR IT ASAP
    private static int drugDispenseRequestCounter = 0;

    public StorageService() {
        storageServiceInterface = this;
        initializeDrugInventoryTable();
        initializeDrugReplenishRequestTable();
        initializeMedicalRecordTable();
    }

    /**
     * Initializes the drug replenish request table by loading data from a CSV file.
     * The CSV file is located at the dataRoot directory.
     * If an IOException occurs during loading, a RuntimeException is thrown.
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
     * Initializes the drug inventory table by loading data from a CSV file.
     * The CSV file is located at the dataRoot directory.
     * If an IOException occurs during loading, a RuntimeException is thrown.
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
     * Initialzes the medical record table by loading data from a CSV file.
     * The CSV file is located at the dataRoot directory.
     * If an IOException occurs during loading, a RuntimeException is thrown.
     * @return
     */
    private void initializeMedicalRecordTable() {
        medicalRecordTable = new MedicalRecord(dataRoot + "medical_records.csv");
        try {
            medicalRecordTable.loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ArrayList<String> getAppointmentsPendingDispensary() {
        // TODO: Implement fetching from AppointmentService
        ArrayList<String> appointmentsPendingDispensary = new ArrayList<>();
        appointmentsPendingDispensary.add("A0001");
        appointmentsPendingDispensary.add("A0002");
        appointmentsPendingDispensary.add("A0003");
        return appointmentsPendingDispensary;
    }

    @Override
    public DrugInventoryTable getDrugInventory() {
        return drugInventoryTable;
    }

    @Override
    public DrugReplenishRequestTable getDrugReplenishRequestTable() {
        return drugReplenishRequestTable;
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecord(String patientID) {
        return Optional.empty();
    }

    @Override
    public boolean saveMedicalRecord(MedicalRecord record) {
        return false;
    }

    @Override
    public boolean updateContactInformation(String patientID, ContactInformation contact) {
        return false;
    }

    @Override
    public boolean addMedicalEntry(String patientID, MedicalEntry entry) {
        return false;
    }

    @Override
    public MedicalRecord getMedicalRecordTable() {
        return medicalRecordTable;
    }

    public DrugDispenseRequest createNewDrugDispenseRequest(String drugName, int addQuantity){
        //TODO: THIS IS A DIRTY HACK! REFACTOR IT ASAP
        // Requires cooperation with Yingjie for this
        DrugDispenseRequest newDispenseRequest = new DrugDispenseRequest(drugDispenseRequestCounter, drugName, addQuantity, DrugRequestStatus.PENDING);
        drugDispenseRequestCounter += 1;
        return newDispenseRequest;
    }

    /**
     * Initializes the Array of AppointmentInfo by loading data from a CSV file.
     * The CSV file path is absolute address for now
     * If an IOException occurs during loading, a RuntimeException is thrown.
     */

    public List<AppointmentInformation> readAppointments() {
        List<AppointmentInformation> appointments = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(dataRoot + "Appointments.csv"))) {
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

    public void writeAppointmentsToCsv(List<AppointmentInformation> appointments) {
        String filePath = dataRoot + "Appointments.csv";
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

            System.out.println("Appointments successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public AppointmentSchedule loadSchedule(String date) {
        int numRows = 0;
        int numCols = 0;

        String filePath = dataRoot + date + ".csv";

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
            BufferedReader br = new BufferedReader(new FileReader(dataRoot + date + ".csv"));

            String line;

            for(int row = 0; (line = br.readLine()) != null; ++row) {
                String[] values = line.split(",");

                for(int col = 0; col < values.length; ++col) {
                    schedule.getMatrix()[row][col] = values[col];
                }
            }

            br.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return schedule;
    }

    public void writeScheduleToCSV(AppointmentSchedule schedule, String date) {
        String[][] matrix = schedule.getMatrix();  // Retrieve the matrix from the AppointmentSchedule object
        //String fileName = date + ".csv";  // Use the date to create the file name
        String filePath = dataRoot + date + ".csv";

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
            System.out.println("Matrix written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

            System.out.println("All AppointmentOutcomes have been updated in CSV file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
