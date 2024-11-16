
package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;
import org.hms.entities.BloodType;
import org.hms.services.appointment.AppointmentOutcome;
import org.hms.services.drugdispensary.DrugInventoryEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord extends AbstractTable<MedicalEntry> {
    private static final String[] HEADERS = {"Entry","PatientID","DoctorID", "Date","Diagnosis","Treatment Plan", "ConsultationNotes"};
    private static final String formatString = "│ %-6s│ %-11s│ %-11s│ %-11s│ %-20s│ %-30s│ %-40s│\n";

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

    /**
     * This method will call each entry's toPrintString() method to generate a string representation of the table.
     * It will then add box drawing characters to create an ASCII table.
     * @return A string representation of the table in an ASCII table format.
     *         This string is meant to be printed to the console.
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
