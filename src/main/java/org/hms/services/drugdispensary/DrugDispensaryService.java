package org.hms.services.drugdispensary;

import org.hms.services.AbstractService;

public class DrugDispensaryService
        extends AbstractService<IDrugStockDataInterface> {
    public DrugDispensaryService(IDrugStockDataInterface dataInterface) {
        this.storageServiceInterface = dataInterface;
    }


}
