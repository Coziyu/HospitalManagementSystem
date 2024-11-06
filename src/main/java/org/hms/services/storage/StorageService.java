package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.IAppointmentDataInterface;
import org.hms.services.drugdispensary.DrugInventoryTable;
import org.hms.services.drugdispensary.DrugReplenishRequestTable;
import org.hms.services.drugdispensary.IDrugStockDataInterface;
import org.hms.services.medicalrecord.IMedicalDataInterface;

import java.util.ArrayList;

public class StorageService
        extends AbstractService<IDataInterface>
        implements IMedicalDataInterface, IAppointmentDataInterface, IDrugStockDataInterface {
    public StorageService() {
        storageServiceInterface = this;
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
        // TODO: Implement loading from file
        return new DrugInventoryTable();
    }

    @Override
    public DrugReplenishRequestTable getDrugReplenishRequestTable() {
        // TODO: Implement loading from file
        return new DrugReplenishRequestTable();
    }
}
