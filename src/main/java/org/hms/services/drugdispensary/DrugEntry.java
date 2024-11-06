package org.hms.services.drugdispensary;

import java.io.Serializable;

public class DrugEntry implements Serializable {
    private final String name;
    private int quantity;
    private int lowStockAlertThreshold;

    public DrugEntry(String name, int quantity, int lowStockAlertThreshold) {
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
}
