package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

public class DrugInventoryTable extends AbstractTable<DrugEntry> {
    private static final String[] HEADERS = {"ID, Name, Qty, lowStockQty"};

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected DrugEntry createEntry() {
        return new DrugEntry();
    }
}
