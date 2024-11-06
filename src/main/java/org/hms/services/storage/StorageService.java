package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.IAppointmentDataInterface;
import org.hms.services.drugdispensary.IDrugStockDataInterface;
import org.hms.services.medicalrecord.IMedicalDataInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StorageService
        extends AbstractService<IDataInterface>
        implements IMedicalDataInterface, IAppointmentDataInterface, IDrugStockDataInterface {
    public StorageService() {
        storageServiceInterface = this;
    }

    public ArrayList<String> getAppointmentsPendingDispensary() {
        // TODO: Implement fetching from
        ArrayList<String> appointmentsPendingDispensary = new ArrayList<>();
        appointmentsPendingDispensary.add("A0001");
        appointmentsPendingDispensary.add("A0002");
        appointmentsPendingDispensary.add("A0003");
        return appointmentsPendingDispensary;
    }
}
