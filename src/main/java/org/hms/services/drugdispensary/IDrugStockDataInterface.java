package org.hms.services.drugdispensary;

import org.hms.services.staffmanagement.StaffTable;
import org.hms.services.storage.IDataInterface;

import java.util.ArrayList;

public interface IDrugStockDataInterface extends IDataInterface {
    /**
     * Retrieves list of appointment IDs pending dispensary.
     *
     * @return ArrayList of appointmentIDs pending dispensary.
     */
    ArrayList<String> getAppointmentsPendingDispensary();

    /**
     * Retrieves list of current drug inventory.
     *
     * @return Drug Inventory Table (current drug inventory)
     */
    DrugInventoryTable getDrugInventory();

    /**
     * Retrieves the table of drug replenish requests.
     *
     * @return the DrugReplenishRequestTable containing replenish requests.
     */
    DrugReplenishRequestTable getDrugReplenishRequestTable();

    StaffTable getStaffTable();
}
