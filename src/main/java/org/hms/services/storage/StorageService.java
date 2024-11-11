package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.AppointmentInformation;
import org.hms.services.appointment.AppointmentSchedule;
import org.hms.services.appointment.AppointmentStatus;
import org.hms.services.appointment.IAppointmentDataInterface;
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

public class StorageService
        extends AbstractService<IDataInterface>
        implements IMedicalDataInterface, IAppointmentDataInterface, IDrugStockDataInterface {

    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    private DrugInventoryTable drugInventoryTable;
    private DrugReplenishRequestTable drugReplenishRequestTable;

    // TODO: THIS IS A DIRTY HACK! REFACTOR IT ASAP
    private static int drugDispenseRequestCounter = 0;

    public StorageService() {
        storageServiceInterface = this;
        initializeDrugInventoryTable();
        initializeDrugReplenishRequestTable();
    }

    /**
     * Initializes the drug replenish request table by loading data from a CSV file.
     * The CSV file is located at the dataRoot directory.
     * If an IOException occurs during loading, a RuntimeException is thrown.
     */
    private void initializeDrugReplenishRequestTable() {
        drugReplenishRequestTable = new DrugReplenishRequestTable();
        try {
            drugReplenishRequestTable.loadFromFile(dataRoot + "drugReplenishRequests.csv");
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
        drugInventoryTable = new DrugInventoryTable();
        try {
            drugInventoryTable.loadFromFile(dataRoot + "drugInventory.csv");
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

    public DrugDispenseRequest createNewDrugDispenseRequest(String drugName, int addQuantity){
        // TODO: THIS IS A DIRTY HACK! REFACTOR IT ASAP
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

    public AppointmentSchedule loadschedule(String date) {
        int numRows = 0;
        int numCols = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(dataRoot + date + ".csv"));

            String line;
            try {
                while((line = br.readLine()) != null) {
                    ++numRows;
                    String[] values = line.split(",");
                    if (numCols < values.length) {
                        numCols = values.length;
                    }
                }
            } catch (Throwable var14) {
                try {
                    br.close();
                } catch (Throwable var11) {
                    var14.addSuppressed(var11);
                }

                throw var14;
            }

            br.close();
        } catch (IOException var15) {
            IOException e = var15;
            e.printStackTrace();
        }

        AppointmentSchedule schedule = new AppointmentSchedule(numCols - 1, numRows - 1);

        try {
            BufferedReader br = new BufferedReader(new FileReader(dataRoot + date + ".csv"));

            String line;
            try {
                for(int row = 0; (line = br.readLine()) != null; ++row) {
                    String[] values = line.split(",");

                    for(int col = 0; col < values.length; ++col) {
                        schedule.getMatrix()[row][col] = values[col];
                    }
                }
            } catch (Throwable var12) {
                try {
                    br.close();
                } catch (Throwable var10) {
                    var12.addSuppressed(var10);
                }

                throw var12;
            }

            br.close();
        } catch (IOException var13) {
            var13.printStackTrace();
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
}
