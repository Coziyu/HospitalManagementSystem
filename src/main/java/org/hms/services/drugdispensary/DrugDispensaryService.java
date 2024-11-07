package org.hms.services.drugdispensary;

import org.hms.services.AbstractService;

import java.util.ArrayList;
import java.util.List;

public class DrugDispensaryService extends AbstractService<IDrugStockDataInterface> {

    DrugInventoryTable drugInventory;
    DrugReplenishRequestTable drugReplenishRequestTable;

    public DrugDispensaryService(IDrugStockDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        drugInventory = storageServiceInterface.getDrugInventory();
        drugReplenishRequestTable = storageServiceInterface.getDrugReplenishRequestTable();
    }

    boolean dispenseDrug(DrugDispenseRequest pendingRequest){
        String drugRequested = pendingRequest.getDrugName();

        // Try finding the drug in the table.
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugRequested);

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

        // Update the Inventory with new stock
        pendingRequest.setStatus(DrugRequestStatus.DISPENSED);
        drugStock.setQuantity(quantityAvailable - quantityRequested);

        return true;
    }

    boolean submitReplenishRequest(String drugName, int addQuantity, String notes) {
        DrugReplenishRequest newRequest = drugReplenishRequestTable.createValidEntryTemplate();
        newRequest.setDrugName(drugName);
        newRequest.setAddQuantity(addQuantity);
        newRequest.setNotes(notes);

        try {
            drugReplenishRequestTable.addEntry(newRequest);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    boolean processReplenishRequest(int replenishRequestID, boolean accept){
        // TODO: Decide if we are keeping records of past requests.
        DrugReplenishRequest request = drugReplenishRequestTable.getEntry(replenishRequestID);
        if (accept){
            boolean success = addDrugStockQuantity(request.getDrugName(), request.getAddQuantity());
            if (!success){
                return false;
            }
        }

        try {
            drugReplenishRequestTable.removeEntry(request.getID());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    //TODO: Exceptions of StockQuantity Manipulation
    boolean setDrugStockQuantity(String drugName, int newQuantity){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        if (results.isEmpty()){
            return false;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        drugStock.setQuantity(newQuantity);
        return true;
    }
    boolean addDrugStockQuantity(String drugName, int newQuantity){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        if (results.isEmpty()){
            return false;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        drugStock.setQuantity(drugStock.getQuantity() + newQuantity);
        return true;
    }
    int getDrugStockQuantity(String drugName){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        //TODO: This should throw an error
        if (results.isEmpty()){
            return -1;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        return drugStock.getQuantity();
    }
}
