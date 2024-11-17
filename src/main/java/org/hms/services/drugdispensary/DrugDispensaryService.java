package org.hms.services.drugdispensary;

import org.hms.entities.Colour;
import org.hms.services.AbstractService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class DrugDispensaryService extends AbstractService<IDrugStockDataInterface> {

    DrugInventoryTable drugInventory;
    DrugReplenishRequestTable drugReplenishRequestTable;

    /**
     * Initialises the DrugDispensaryService with the given data interface
     *
     * @param dataInterface the interface for accessing drug stock data.
     */
    public DrugDispensaryService(IDrugStockDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
        drugInventory = storageServiceInterface.getDrugInventory();
        drugReplenishRequestTable = storageServiceInterface.getDrugReplenishRequestTable();
    }

    /**
     * Retrieves the drug replenish requests as a formatted string.
     *
     * @return a string representation of the drug replenish requests.
     */
    public String getDrugReplenishRequestsAsString(){
        return drugReplenishRequestTable.toPrintString();
    }

    /**
     * Retrieves the drug inventory as a string
     *
     * @return a string representation of the drug inventory
     */
    public String getDrugInventoryAsString(){
        return drugInventory.toPrintString();
    }

    /**
     * Looks through drugInventory, and returns a table of drugs with
     * quantity < lowStockAlertThreshold
     * @return READ-ONLY copy of DrugInventoryTable filtered by threshold
     */
    public DrugInventoryTable getLowStockDrugs(){
        BiPredicate<Integer, Integer> smallerThanOrEquals = (i, j) -> (i <= j);

        return (DrugInventoryTable) drugInventory.filterByCondition(
                DrugInventoryEntry::getQuantity,
                DrugInventoryEntry::getLowStockAlertThreshold,
                smallerThanOrEquals);
    }

    /**
     * Processes a drug dispense request and updates the inventory if valid.
     * @param pendingRequest the drug dispense request to process.
     * @return true if the drug was successfully dispensed, false otherwise
     */
    public boolean dispenseDrug(DrugDispenseRequest pendingRequest){
        String drugRequested = pendingRequest.getDrugName();

        // Try finding the drug in the table.
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugRequested);

        // If empty, it means Drug doesn't exist.
        // print here?
        if (results.isEmpty()){
            System.out.println(Colour.RED + "Drug not found: " + drugRequested);
            System.out.println("A new entry for " + drugRequested + " was added to the inventory.");
            System.out.println("Please submit a replenish request for this drug." + Colour.RESET);
            return false;
        }

        // Should be 1 entry, but if more we just use the first entry for now.
        DrugInventoryEntry drugStock = results.getFirst();

        int quantityRequested = pendingRequest.getQuantity();
        int quantityAvailable = drugStock.getQuantity();
        // If affectedStock doesnt have enough entries, return false;
        if (quantityAvailable < quantityRequested){
            System.out.println(Colour.RED + "Not enough stock for " + drugRequested + Colour.RESET);
            return false;
        }

        // Update the Inventory with new stock
        pendingRequest.setStatus(DrugRequestStatus.DISPENSED);
        drugStock.setQuantity(quantityAvailable - quantityRequested);
        try {
            drugInventory.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     *Submits a replenish request for a specified drug by name
     *
     * @param drugName name of drug to replenish
     * @param addQuantity quantity to add
     * @param notes additional notes for the request
     * @return true if request was successfully submitted, false otherwise
     */
    public boolean submitReplenishRequest(String drugName, int addQuantity, String notes) {
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

    /**
     * Submits a replenish request for a specific drug using its inventory entry ID.
     *
     * @param inventoryEntryID the ID of the inventory entry to replenish.
     * @param addQuantity      the quantity to add.
     * @param notes            additional notes for the request.
     * @return true if the request was successfully submitted, false otherwise.
     */
    public boolean submitReplenishRequest(int inventoryEntryID, int addQuantity, String notes) {
        DrugReplenishRequest newRequest = drugReplenishRequestTable.createValidEntryTemplate();

        // Get drug name from inventoryEntryID
        String drugName = drugInventory.getEntry(inventoryEntryID).getName();

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

    /**
     * Processes a replenish and accepts or reject it
     *
     * @param replenishRequestID the ID of the replenish request
     * @param accept        True if accepted, false if rejected
     * @return          True if request was successfully processed, false otherwise
     */
    public boolean processReplenishRequest(int replenishRequestID, boolean accept){
        DrugReplenishRequest request = drugReplenishRequestTable.getEntry(replenishRequestID);
        if (accept){
            boolean success = addDrugStockQuantity(request.getDrugName(), request.getAddQuantity());
            if (!success){
                return false;
            }
        }

        try {
            drugReplenishRequestTable.removeEntry(request.getTableEntryID());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Processes all pending replenish requests and accepts them.
     *
     * @return true if all requests were successfully processed, false otherwise
     */
    public boolean approveAllReplenishRequests(){
        List<DrugReplenishRequest> requests = drugReplenishRequestTable.getEntries();
        for (DrugReplenishRequest request : requests){
            processReplenishRequest(request.getTableEntryID(), true);
        }
        return true;
    }


    /**
     * Updates the stock quantity for a specific drug by name
     * @param drugName  name of the drug
     * @param newQuantity   the new stock quantity
     * @return  true if stock quantity successfully updated, false otherwise
     */
    public boolean setDrugStockQuantity(String drugName, int newQuantity){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        if (results.isEmpty()){
            return false;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        drugStock.setQuantity(newQuantity);
        try {
            drugInventory.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public boolean setDrugLowStockAlertThreshold(String drugName, int newLowStockAlertThreshold){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        if (results.isEmpty()){
            return false;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        drugStock.setLowStockAlertThreshold(newLowStockAlertThreshold);
        try {
            drugInventory.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Adds to the stock quantity for a specific drug by name.
     *
     * @param drugName    the name of the drug.
     * @param newQuantity the quantity to add.
     * @return true if the stock quantity was successfully updated, false otherwise.
     */
    public boolean addDrugStockQuantity(String drugName, int newQuantity){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        if (results.isEmpty()){
            return false;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        drugStock.setQuantity(drugStock.getQuantity() + newQuantity);
        try {
            drugInventory.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Retrieves the current stock quantity for a specific drug by name.
     *
     * @param drugName the name of the drug.
     * @return the current stock quantity, or -1 if the drug does not exist.
     */
    public int getDrugStockQuantity(String drugName){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName);
        if (results.isEmpty()){
            return -1;
        }
        DrugInventoryEntry drugStock = results.getFirst();
        return drugStock.getQuantity();
    }

    /**
     * Checks if the entryID is valid
     * @param entryID the inventory to validate
     * @return  true if valid, false otherwise
     */
    public boolean isValidDrugEntryID(int entryID){
         return !drugInventory.searchByAttribute(DrugInventoryEntry::getTableEntryID, entryID).isEmpty();
    }

    public boolean isValidReplenishRequestID(int entryID){
        return !drugReplenishRequestTable.searchByAttribute(DrugReplenishRequest::getTableEntryID, entryID).isEmpty();
    }

    /**
     * Checks if a drug exists in the inventory.
     *
     * @param drugName the name of the drug to search for.
     * @return true if the drug exists, false otherwise.
     */
    public boolean doesDrugExist(String drugName){
        return !drugInventory.searchByAttribute(DrugInventoryEntry::getName, drugName).isEmpty();
    }

    /**
     * Adds a new drug to the inventory.
     *
     * @param drugName               the name of the drug.
     * @param quantity               the initial quantity of the drug.
     * @param lowStockAlertThreshold the low stock alert threshold for the drug.
     * @return true if the drug was successfully added, false otherwise.
     */
    public boolean addNewDrug(String drugName, int quantity, int lowStockAlertThreshold){
        DrugInventoryEntry newDrug = drugInventory.createValidEntryTemplate();

        newDrug.setName(drugName);
        newDrug.setQuantity(quantity);
        newDrug.setLowStockAlertThreshold(lowStockAlertThreshold);

        try {
            drugInventory.addEntry(newDrug);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Removes a drug from the inventory.
     * @param entryID the ID of the drug to remove.
     * @return true if the drug was successfully removed, false otherwise.
     */
    public boolean removeDrugFromInventory(int entryID){
        // Check if the entryID is valid
        if (!isValidDrugEntryID(entryID)){
            return false;
        }
        try {
            drugInventory.removeEntry(entryID);
    /**
     * Retrieves the name of the drug specified by its entry ID.
     *
     * @param entryID the unique identifier of the drug in the inventory.
     * @return the name of the drug if found, otherwise null.
     */
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Retrieves the name of the drug specified by its entry ID.
     * @param entryID the unique identifier of the drug in the inventory.
     * @return the name of the drug if found, otherwise null.
     */
    public String getDrugName(int entryID){
        ArrayList<DrugInventoryEntry> results = drugInventory.searchByAttribute(DrugInventoryEntry::getTableEntryID, entryID);
        if (results.isEmpty()){
            return null;
        }
        return results.getFirst().getName();
    }
}
