package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

public class DrugInventoryTable extends AbstractTable<DrugInventoryEntry> {
    private static final String[] HEADERS = {"ID, Name, Qty, lowStockQty"};

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected DrugInventoryEntry createValidEntryTemplate() {
        return new DrugInventoryEntry(getUnusedID(), null, -1, -1);
    }
}
