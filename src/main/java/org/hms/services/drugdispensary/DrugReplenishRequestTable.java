package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

public class DrugReplenishRequestTable extends AbstractTable<DrugReplenishRequest> {
    private static final String[] HEADERS = {"ID", "Name", "Qty", "Notes"};
    /**
     * @return
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * @return
     */
    @Override
    public DrugReplenishRequest createValidEntryTemplate() {
        return new DrugReplenishRequest(getUnusedID(), null, -1, null);
    }

    @Override
    protected DrugReplenishRequestTable createEmpty() {
        return new DrugReplenishRequestTable();
    }


}
