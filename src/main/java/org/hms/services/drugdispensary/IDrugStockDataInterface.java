package org.hms.services.drugdispensary;

import org.hms.services.storage.IDataInterface;

import java.util.ArrayList;

public interface IDrugStockDataInterface extends IDataInterface {
    /**
     * @return ArrayList of appointmentIDs
     */
    ArrayList<String> getAppointmentsPendingDispensary();
    DrugInventoryTable getDrugInventory();
    DrugReplenishRequestTable getDrugReplenishRequestTable();
}
