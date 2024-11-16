package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;
import org.hms.entities.BloodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PatientTable extends AbstractTable<PatientParticulars> {
    private static final String[] HEADERS = {"Entry", "PatientID", "Name", "BirthDate", "Gender", "BloodType"};
    private static final String formatString = "│ %-6s│ %-11s│ %-15s│ %-11s│ %-8s│ %-10s│\n";

    public PatientTable() {
        super();
    }

    public PatientTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public PatientParticulars createValidEntryTemplate() {
        try {
            return new PatientParticulars(getUnusedID(), "", "", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"), "", BloodType.NULL);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PatientTable createEmpty() {
        return new PatientTable();
    }

    /**
     * This method will call each entry's toPrintString() method to generate a string representation of the table.
     * It will then add box drawing characters to create an ASCII table.
     * @return A string representation of the table in an ASCII table format.
     *         This string is meant to be printed to the console.
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
