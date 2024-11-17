package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTableEntry;

/**
 * Represents a request to replenish a specific drug with a specified quantity and optional notes.
 */
public class DrugReplenishRequest extends AbstractTableEntry {
    /**
     * The name of the drug to be replenished.
     */
    String drugName;
    /**
     * Represents the quantity of a drug to be added in a drug replenish request.
     */
    int addQuantity;
    /**
     * Additional notes for the drug replenish request.
     */
    String notes;

    /**
     * Default constructor intializing DrugReplenishRequest with an Invalid ID
     */
    public DrugReplenishRequest() {
        super(-1);
    }

    /**
     * Initializes a DrugReplenishRequest with specified ID, drug name, quantity to add, and notes.
     *
     * @param id          the unique ID of the replenish request.
     * @param drugName    the name of the drug to replenish.
     * @param addQuantity the quantity of the drug to add.
     * @param notes       additional notes for the request.
     */
    public DrugReplenishRequest(int id, String drugName, int addQuantity, String notes) {
        super(id);
        this.drugName = drugName;
        this.addQuantity = addQuantity;
        this.notes = notes;
    }

    /**
     * Retrieves the name of the drug to be replenished.
     *
     * @return the name of the drug.
     */
    public String getDrugName() {
        return drugName;
    }

    /**
     * Sets the name of the drug to be replenished.
     *
     * @param drugName the name of the drug.
     */
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    /**
     * Retrieves the quantity of the drug to be added.
     *
     * @return the quantity to add.
     */
    public int getAddQuantity() {
        return addQuantity;
    }

    /**
     * Sets the quantity of the drug to be added.
     *
     * @param addQuantity the quantity to add.
     */
    public void setAddQuantity(int addQuantity) {
        this.addQuantity = addQuantity;
    }

    /**
     * Retrieves any additional notes for the replenish request.
     *
     * @return the notes for the request.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes for the replenish request.
     *
     * @param notes the notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Converts the drug replenish request to a CSV formatted string.
     *
     * @return a CSV string representing the request details
     */
    @Override
    public String toCSVString() {
        return String.format("%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(drugName),
                addQuantity,
                preprocessCSVString(notes));
    }

    /**
     * Loads the drug replenish request details from a CSV formatted string.
     *
     * @param csvLine comma seperated entry values.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        drugName = parts[1];
        addQuantity = Integer.parseInt(parts[2]);
        notes = parts[3];
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
                drugName,
                addQuantity,
                notes
        );
        return printString;
    }
}
