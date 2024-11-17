package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTableEntry;

/**
 * The DrugInventoryEntry class represents an entry in a drug inventory table.
 * It includes details such as the name of the drug, its current stock quantity,
 * and a threshold value for triggering low stock alerts.
 */
public class DrugInventoryEntry extends AbstractTableEntry {
    /**
     * Represents the name of the drug in the inventory.
     */
    private String name;
    /**
     * Represents the current stock quantity of a drug in the inventory.
     * This value is used to track the number of units available for the drug.
     */
    private int quantity;
    /**
     * The threshold quantity below which a low stock alert is triggered.
     */
    private int lowStockAlertThreshold;

    /**
     * Default constructor initializing DrugInventoryEntry with an invalid ID.
     */
    public DrugInventoryEntry() {
        super(-1);
    }

    /**
     * Initializes a DrugInventoryEntry with specified ID, name, quantity, and low stock alert threshold.
     *
     * @param id                     the unique ID of the drug entry.
     * @param name                   the name of the drug.
     * @param quantity               the current stock quantity of the drug.
     * @param lowStockAlertThreshold the threshold quantity below which a low stock alert is triggered.
     */
    public DrugInventoryEntry(int id, String name, int quantity, int lowStockAlertThreshold) {
        super(id);
        this.name = name;
        this.quantity = quantity;
        this.lowStockAlertThreshold = lowStockAlertThreshold;
    }

    /**
     * Retrieves the low stock alert threshold for this drug entry.
     *
     * @return the low stock alert threshold.
     */
    public int getLowStockAlertThreshold() {
        return lowStockAlertThreshold;
    }

    /**
     * Sets the low stock alert threshold for this drug entry.
     *
     * @param lowStockAlertThreshold the new low stock alert threshold to set.
     */
    public void setLowStockAlertThreshold(int lowStockAlertThreshold) {
        this.lowStockAlertThreshold = lowStockAlertThreshold;
    }

    /**
     * Retrieves the current stock quantity of the drug.
     *
     * @return the stock quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the stock quantity for the drug.
     *
     * @param quantity the new quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the name of the drug.
     *
     * @return the drug name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the drug.
     *
     * @param name the new name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Converts the drug inventory entry to a CSV formatted string
     *
     * @return a CSV string representing the entry details
     */
    @Override
    public String toCSVString() {
        return String.format("%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(name), quantity, lowStockAlertThreshold);
    }

    /**
     * Loads the drug inventory entry details from a CSV formatted string
     *
     * @param csvLine comma seperated entry values.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        name = parts[1];
        quantity = Integer.parseInt(parts[2]);
        lowStockAlertThreshold = Integer.parseInt(parts[3]);
    }

    /**
     * Generate a string representation of the object with the given format string.
     * Meant to be printed to the console.
     *
     * @param formatString The format string to use. Must contain 4 placeholders for the entry's ID, drugName, addQuantity and notes.
     * @return A string representation of the object.
     */
    public String toPrintString(String formatString) {
        String printString = String.format(formatString,
                getTableEntryID(),
                name,
                quantity,
                lowStockAlertThreshold
        );
        return printString;
    }
}
