package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTableEntry;


public class DrugDispenseRequest extends AbstractTableEntry {
    String drugName;
    int quantity;
    DrugRequestStatus status;

    public DrugDispenseRequest(int id, String drugName, int quantity, DrugRequestStatus status) {
        super(id);
        this.drugName = drugName;
        this.quantity = quantity;
        this.status = status;
    }

    public DrugRequestStatus getStatus() {
        return status;
    }

    public void setStatus(DrugRequestStatus status) {
        this.status = status;
    }

    public String getDrugName() {
        return drugName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toCSVString() {
        return String.format("%s,%s,%s,%s",
                getTableEntryID(),
                preprocessCSVString(drugName),
                quantity,
                status);
    }

    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = parseCSVLine(csvLine);
        tableEntryID = Integer.parseInt(parts[0]);
        drugName = parts[1];
        quantity = Integer.parseInt(parts[2]);
        status = DrugRequestStatus.valueOf(parts[3]);
    }
}
