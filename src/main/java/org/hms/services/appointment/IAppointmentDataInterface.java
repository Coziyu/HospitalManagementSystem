package org.hms.services.appointment;

import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.drugdispensary.DrugRequestStatus;
import org.hms.services.storage.IDataInterface;

public interface IAppointmentDataInterface extends IDataInterface {
    /**
     * Creates a new pending DrugDispenseRequest.
     * @param drugName
     * @param addQuantity
     * @return
     */
    public DrugDispenseRequest createNewDrugDispenseRequest(String drugName, int addQuantity);

}
