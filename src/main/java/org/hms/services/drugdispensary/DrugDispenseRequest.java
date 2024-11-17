package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTableEntry;
import org.hms.entities.Colour;

public class DrugDispenseRequest extends AbstractTableEntry {
    String drugName;
    int quantity;
    DrugRequestStatus status;

    /**
     * Initializes a DrugDispenseRequest with specified ID, drug name, quantity, and status.
     *
     * @param id       the unique ID of the dispense request.
     * @param drugName the name of the drug requested.
     * @param quantity the quantity of the drug requested.
     * @param status   the current status of the request.
     */
    public DrugDispenseRequest(int id, String drugName, int quantity, DrugRequestStatus status) {
        super(id);
        this.drugName = drugName;
        this.quantity = quantity;
        this.status = status;
    }

    /**
     * Retrieves the current status of the drug dispense request.
     *
     * @return the status of the request.
     */
    public DrugRequestStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the drug dispense request.
     *
     * @param status the new status to be assigned to the request.
     */
    public void setStatus(DrugRequestStatus status) {
        this.status = status;
    }

    /**
     * Retrieves the name of the drug requested.
     *
     * @return the name of the drug.
     */
    public String getDrugName() {
        return drugName;
    }

    /**
     * Retrieves the quantity of the drug requested.
     *
     * @return the quantity requested.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the drug requested.
     *
     * @param quantity the new quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Converts the drug dispense request to a CSV formatted string.
     *
     * @return a CSV string representing the request details.
     */
    @Override
    public String toCSVString() {
        return String.format("%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(drugName),
                quantity,
                status);
    }

    /**
     * Loads the drug dispense request details from a CSV formatted string.
     *
     * @param csvLine the CSV string containing request details.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        drugName = parts[1];
        quantity = Integer.parseInt(parts[2]);
        status = DrugRequestStatus.valueOf(parts[3]);
    }

    public String toPrintString() {
        return (String.format(Colour.YELLOW + "Drug Requested: %s, Quantity: %s,Status: %s" + Colour.RESET,
                preprocessCSVString(drugName),
                quantity,
                status)
        );
    }
}
