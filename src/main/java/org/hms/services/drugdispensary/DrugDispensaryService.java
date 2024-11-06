package org.hms.services.drugdispensary;

import org.hms.services.AbstractService;

import java.util.List;

public class DrugDispensaryService extends AbstractService<IDrugStockDataInterface> {

    DrugInventoryTable drugInventory;

    public DrugDispensaryService(IDrugStockDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        // TODO: Load drugInventoryTable from storageService.
    }

    boolean dispenseDrug(DrugRequest pendingRequest){
        String drugRequested = pendingRequest.getDrugName();

        // Try finding the drug in the table.
        List<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugRequested);

        // If empty, it means Drug doesn't exist.
        // print here?
        if (results.isEmpty()){
            return false;
        }

        // Should be 1 entry, but if more we just use the first entry for now.
        DrugInventoryEntry drugStock = results.getFirst();

        int quantityRequested = pendingRequest.getQuantity();
        int quantityAvailable = drugStock.getQuantity();
        // If affectedStock doesnt have enough entries, return false;
        if (quantityAvailable < quantityRequested){
            return false;
        }

        pendingRequest.setStatus(DrugRequestStatus.DISPENSED);
        drugStock.setQuantity(quantityAvailable - quantityRequested);

        // Update the Inventory with new stock

        return true;
    }

    boolean submitReplenishRequest(String drugName, int quantity) {


        //TODO: Send replenishment request to Storage Service.



        return true;
    }
}
