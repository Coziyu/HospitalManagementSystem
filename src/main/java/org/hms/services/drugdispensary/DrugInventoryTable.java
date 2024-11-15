package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

public class DrugInventoryTable extends AbstractTable<DrugInventoryEntry> {
    private static final String[] HEADERS = {"ID", "Name", "Qty", "lowStockQty"};

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public DrugInventoryEntry createValidEntryTemplate() {
        return new DrugInventoryEntry(getUnusedID(), null, -1, -1);
    }

    @Override
    protected DrugInventoryTable createEmpty() {
        return new DrugInventoryTable();
    }
}
