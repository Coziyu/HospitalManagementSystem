package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTableEntry;


public class DrugEntry extends AbstractTableEntry {
    private String name;
    private int quantity;
    private int lowStockAlertThreshold;

    public DrugEntry () {
        super();
    }

    public DrugEntry(String name, int quantity, int lowStockAlertThreshold) {
        super();
        this.name = name;
        this.quantity = quantity;
        this.lowStockAlertThreshold = lowStockAlertThreshold;
    }

    public int getLowStockAlertThreshold() {
        return lowStockAlertThreshold;
    }

    public void setLowStockAlertThreshold(int lowStockAlertThreshold) {
        this.lowStockAlertThreshold = lowStockAlertThreshold;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public String toCSVString() {
        return String.format("%s,%s,%s,%s",
                getId(),name,quantity,lowStockAlertThreshold);
    }

    /**
     * @param csvLine comma seperated entry values.
     */
    @Override
    public void loadFromCSVString(String csvLine) {
        String[] parts = csvLine.split(",");
        id = Integer.parseInt(parts[0]);
        this.name = parts[1];
        quantity = Integer.parseInt(parts[2]);
        lowStockAlertThreshold = Integer.parseInt(parts[3]);
    }
}
