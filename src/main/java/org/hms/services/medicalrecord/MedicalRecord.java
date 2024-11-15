
package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;
import org.hms.entities.BloodType;
import org.hms.services.appointment.AppointmentOutcome;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord extends AbstractTable<MedicalEntry> {
    private static final String[] HEADERS = {"EntryID","PatientID","DoctorID", "Date","Diagnosis","Treatment Plan", "ConsultationNotes"};


    public MedicalRecord() {
        super();
    }

    public MedicalRecord(String filePath){
        super();
        this.filePath = filePath;
    }

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public MedicalEntry createValidEntryTemplate(){
        return new MedicalEntry(getUnusedID(), "", "", "", "", "");
    }

    @Override
    protected MedicalRecord createEmpty(){
        return new MedicalRecord();
    }
}
