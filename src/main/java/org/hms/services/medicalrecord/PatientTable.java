package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;
import org.hms.entities.BloodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The PatientTable class extends AbstractTable to provide a specialized table for patient particulars.
 * It includes methods to return table headers, create valid entry templates for patient particulars,
 * create an empty PatientTable, and generate a string representation of the table in an ASCII table format.
 */
public class PatientTable extends AbstractTable<PatientParticulars> {
    /**
     * HEADERS is an array of strings representing the column headers for the patient table.
     * It includes the headers for Entry, PatientID, Name, BirthDate, Gender, and BloodType.
     */
    private static final String[] HEADERS = {"Entry", "PatientID", "Name", "BirthDate", "Gender", "BloodType"};
    /**
     * The format string used for generating each row of the patient table in an ASCII table format.
     * It specifies the width and alignment of each column to match the headers and the corresponding data fields.
     * <p>
     * The format is as follows:
     * - Entry: Width 6, Left-aligned
     * - PatientID: Width 11, Left-aligned
     * - Name: Width 15, Left-aligned
     * - BirthDate: Width 11, Left-aligned
     * - Gender: Width 8, Left-aligned
     * - BloodType: Width 10, Left-aligned
     */
    private static final String formatString = "│ %-6s│ %-11s│ %-15s│ %-11s│ %-8s│ %-10s│\n";

    /**
     * Default constructor for the PatientTable class.
     * Initializes the PatientTable by calling the superclass constructor.
     */
    public PatientTable() {
        super();
    }

    /**
     * Constructs a new PatientTable with the provided file path.
     *
     * @param filePath the path to the CSV file containing patient particulars data
     */
    public PatientTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    /**
     * Returns the headers used for the patient table.
     *
     * @return an array of strings representing the headers.
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * Creates a valid entry template for a patient's particulars with default values.
     * This includes an unused ID, empty strings for unspecified fields, a default birth date of January 1, 2000,
     * and a null blood type.
     *
     * @return A new instance of PatientParticulars with default values.
     * @throws RuntimeException if the parsing of the default birth date fails.
     */
    @Override
    public PatientParticulars createValidEntryTemplate() {
        try {
            return new PatientParticulars(getUnusedID(), "", "", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), "", BloodType.NULL);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an empty instance of the PatientTable.
     * This method overrides the parent class to return a new empty PatientTable specific to patient details.
     *
     * @return A new, empty PatientTable instance.
     */
    @Override
    protected PatientTable createEmpty() {
        return new PatientTable();
    }

    /**
     * This method will call each entry's toPrintString() method to generate a string representation of the table.
     * It will then add box drawing characters to create an ASCII table.
     *
     * @return A string representation of the table in an ASCII table format.
     * This string is meant to be printed to the console.
     */
    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌───────┬────────────┬────────────────┬────────────┬─────────┬───────────┐\n");
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├───────┼────────────┼────────────────┼────────────┼─────────┼───────────┤\n");
        for (PatientParticulars entry : getEntries()) {
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└───────┴────────────┴────────────────┴────────────┴─────────┴───────────┘\n");

        return printStringBuilder.toString();
    }
}
