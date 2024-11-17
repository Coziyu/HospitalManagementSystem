package org.hms.services.appointment;

import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.staffmanagement.Staff;
import org.hms.services.storage.IDataInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing the data management operations for appointments.
 */
public interface IAppointmentDataInterface extends IDataInterface {
    /**
     * Creates a new drug dispense request with the specified drug name and quantity.
     *
     * @param drugName    the name of the drug to be included in the request.
     * @param addQuantity the quantity of the drug to be requested.
     * @return a new instance of {@link DrugDispenseRequest} initialized with the given parameters.
     */
    public DrugDispenseRequest createNewDrugDispenseRequest(String drugName, int addQuantity);

    /**
     * Reads appointment outcomes from a CSV file and returns them as a list of AppointmentOutcome objects.
     *
     * @return A list of AppointmentOutcome objects representing the outcomes of various appointments.
     */
    public ArrayList<AppointmentOutcome> readAppointmentOutcomesFromCSV();

    /**
     * Writes a list of appointment information to a CSV file.
     *
     * @param appointments the list of {@link AppointmentInformation} objects to be written to the CSV file.
     */
    public void writeAppointmentsToCsv(List<AppointmentInformation> appointments);

    /**
     * Writes the outcome of a specific appointment to a CSV file.
     *
     * @param outcome The outcome of the appointment to be written to the CSV file. This includes
     *                appointment ID, patient ID, type of appointment, consultation notes,
     *                and prescribed medications.
     */
    public void writeAppointmentOutcomeToCSV(AppointmentOutcome outcome);

    /**
     * Writes all appointment outcomes to a CSV file.
     *
     * @param appointmentOutcomes a list of AppointmentOutcome objects to be written to the CSV file
     */
    public void writeAllAppointmentOutcomesToCSV(ArrayList<AppointmentOutcome> appointmentOutcomes);

    /**
     * Reads the list of appointments from the storage system.
     *
     * @return a list of {@link AppointmentInformation} objects representing the appointments.
     */
    public List<AppointmentInformation> readAppointments();


    /**
     * Loads the appointment schedule for the given date.
     *
     * @param date The date for which the appointment schedule is to be loaded.
     * @return The appointment schedule for the specified date.
     */
    public AppointmentSchedule loadSchedule(String date);

    /**
     * Writes the given appointment schedule to a CSV file for the specified date.
     *
     * @param schedule the appointment schedule to be written to the CSV file
     * @param date     the date for which the schedule is to be written
     */
    public void writeScheduleToCSV(AppointmentSchedule schedule, String date);

    /**
     * Retrieves the Staff member associated with a given schedule based on the staff ID.
     *
     * @param staffId the alphanumerical ID of the staff member to retrieve.
     * @return the Staff object that matches the provided staff ID.
     */
    public Staff getStaffForSchedule(String staffId);

    /**
     * Retrieves the name of a staff member based on their unique identifier.
     *
     * @param userId The unique identifier of the staff member.
     * @return The name of the staff member corresponding to the provided userId.
     * If the userId is not found, returns null.
     */
    public String getStaffNameByID(String userId);

    /**
     * Retrieves all files containing appointment scheduling data.
     *
     * @return an array of Files representing all stored appointment schedules.
     */
    public File[] getAllDateFile();

    /**
     * Initializes the appointment schedule for the specified date.
     *
     * @param date The date for which to initialize the schedule, formatted as YYYYMMDD.
     */
    public void initializeSchedule(String date);

    /**
     * Checks if a schedule exists for the given date.
     *
     * @param date The date in "yyyyMMdd" format for which the schedule existence needs to be verified.
     * @return true if a schedule exists for the given date, false otherwise.
     */
    public boolean checkScheduleExist(String date);

}
