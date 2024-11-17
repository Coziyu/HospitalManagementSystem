package org.hms.services.appointment;

import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.staffmanagement.Staff;
import org.hms.services.storage.IDataInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface IAppointmentDataInterface extends IDataInterface {
    /**
     * Creates a new pending DrugDispenseRequest.
     * @param drugName
     * @param addQuantity
     * @return
     */
    public DrugDispenseRequest createNewDrugDispenseRequest(String drugName, int addQuantity);

    public ArrayList<AppointmentOutcome> readAppointmentOutcomesFromCSV();

    public void writeAppointmentsToCsv(List<AppointmentInformation> appointments);

    public void writeAppointmentOutcomeToCSV(AppointmentOutcome outcome);

    public void writeAllAppointmentOutcomesToCSV(ArrayList<AppointmentOutcome> appointmentOutcomes);

    public List<AppointmentInformation> readAppointments();


    public AppointmentSchedule loadSchedule(String date) ;

    public void writeScheduleToCSV(AppointmentSchedule schedule, String date);

    public Staff getStaffForSchedule(String staffId);

    public String getStaffNameByID(String userId);

    public File[] getAllDateFile();

    public void initializeSchedule(String date);

    public boolean checkScheduleExist(String date);

}
