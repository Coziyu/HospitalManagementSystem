package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTableEntry;


public class DrugReplenishRequest extends AbstractTableEntry {
    String drugName;
    int addQuantity;
    String notes;

    public DrugReplenishRequest(){
        super(-1);
    }

    public DrugReplenishRequest(int id, String drugName, int addQuantity, String notes) {
        super(id);
        this.drugName = drugName;
        this.addQuantity = addQuantity;
        this.notes = notes;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public int getAddQuantity() {
        return addQuantity;
    }

    public void setAddQuantity(int addQuantity) {
        this.addQuantity = addQuantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return
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
}
