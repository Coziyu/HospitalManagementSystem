package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.IAppointmentDataInterface;
import org.hms.services.drugdispensary.DrugInventoryTable;
import org.hms.services.drugdispensary.DrugReplenishRequestTable;
import org.hms.services.drugdispensary.IDrugStockDataInterface;
import org.hms.services.medicalrecord.IMedicalDataInterface;

import java.io.IOException;
import java.util.ArrayList;

public class StorageService
        extends AbstractService<IDataInterface>
        implements IMedicalDataInterface, IAppointmentDataInterface, IDrugStockDataInterface {

    private static final String dataRoot = System.getProperty("user.dir") + "/data/";
    private DrugInventoryTable drugInventoryTable;
    private DrugReplenishRequestTable drugReplenishRequestTable;
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
}
