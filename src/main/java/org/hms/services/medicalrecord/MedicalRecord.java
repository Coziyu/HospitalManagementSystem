
package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;
import org.hms.entities.BloodType;
import org.hms.services.appointment.AppointmentOutcome;
import org.hms.services.drugdispensary.DrugInventoryEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a medical record which contains a collection of medical entries.
 * It extends the AbstractTable class parameterized with MedicalEntry.
 */
public class MedicalRecord extends AbstractTable<MedicalEntry> {
    /**
     * An array of strings representing the column headers for the medical records table.
     * The columns include:
     * - "Entry": The entry number in the medical record.
     * - "PatientID": The unique identifier for the patient.
     * - "DoctorID": The unique identifier for the doctor.
     * - "Date": The date of the medical entry.
     * - "Diagnosis": The diagnosis information.
     * - "Treatment Plan": The plan for treatment.
     * - "ConsultationNotes": Notes made during the consultation.
     */
    private static final String[] HEADERS = {"Entry", "PatientID", "DoctorID", "Date", "Diagnosis", "Treatment Plan", "ConsultationNotes"};
    /**
     * The format string used to define the table layout for printing medical records.
     * This string specifies the width and alignment of each column when representing the medical entries.
     * Columns are organized as follows: Entry, PatientID, DoctorID, Date, Diagnosis, Treatment Plan, Consultation Notes.
     */
    private static final String formatString = "│ %-6s│ %-11s│ %-11s│ %-11s│ %-20s│ %-30s│ %-40s│\n";

    /**
     * Default constructor for the MedicalRecord class.
     * Initializes a new instance of MedicalRecord by calling the constructor
     * of its superclass AbstractTable with the parameterized type MedicalEntry.
     */
    public MedicalRecord() {
        super();
    }

    /**
     * Constructs a MedicalRecord instance with a specified file path.
     * The file path is used to locate the CSV file that contains medical records.
     *
     * @param filePath the path to the CSV file containing medical records
     */
    public MedicalRecord(String filePath) {
        super();
        this.filePath = filePath;
    }

    /**
     * Retrieves the headers for the medical record entries.
     *
     * @return an array of strings representing the column headers of the medical record.
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * Creates a valid template for a new medical entry with default values.
     *
     * @return a new instance of MedicalEntry with default values for each field
     */
    @Override
    public MedicalEntry createValidEntryTemplate() {
        return new MedicalEntry(getUnusedID(), "", "", "", "", "");
    }

    /**
     * Creates and returns an empty instance of the MedicalRecord class.
     *
     * @return a new instance of the MedicalRecord class, which is initially empty.
     */
    @Override
    protected MedicalRecord createEmpty() {
        return new MedicalRecord();
    }

    /**
     * Converts the medical record entries into a formatted string representation.
     *
     * @return A string representing the medical record in a formatted table layout.
     */
    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌───────┬────────────┬────────────┬────────────┬─────────────────────┬───────────────────────────────┬─────────────────────────────────────────┐\n");
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├───────┼────────────┼────────────┼────────────┼─────────────────────┼───────────────────────────────┼─────────────────────────────────────────┤\n");
        for (MedicalEntry entry : getEntries()) {
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└───────┴────────────┴────────────┴────────────┴─────────────────────┴───────────────────────────────┴─────────────────────────────────────────┘\n");
        return printStringBuilder.toString();
    }
}
