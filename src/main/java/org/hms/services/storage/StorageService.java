package org.hms.services.storage;

import org.hms.services.AbstractService;
import org.hms.services.appointment.IAppointmentDataAccess;
import org.hms.services.medicalrecord.IMedicalDataAccess;

public class StorageService
        extends AbstractService<IStorageAccess>
        implements IMedicalDataAccess, IAppointmentDataAccess {
    StorageService() {
        storageService = this;
    }
}
