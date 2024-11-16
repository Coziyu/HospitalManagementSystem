package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;

public class ContactInformationTable extends AbstractTable<ContactInformation> {
    private static final String[] HEADERS = {"Entry", "PatientID", "PhoneNumber", "Email", "Address"};
    private static final String formatString = "│ %-6s│ %-11s│ %-12s│ %-25s│ %-30s│\n";

    public ContactInformationTable() {
        super();
    }

    public ContactInformationTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    @Override
    public ContactInformation createValidEntryTemplate() {
        return new ContactInformation(-1, "", "", "", "");
    }

    @Override
    protected ContactInformationTable createEmpty() {
        return new ContactInformationTable();
    }


    public String toPrintString(){
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌───────┬────────────┬─────────────┬──────────────────────────┬───────────────────────────────┐\n");
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├───────┼────────────┼─────────────┼──────────────────────────┼───────────────────────────────┤\n");
        for (ContactInformation entry : getEntries()){
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└───────┴────────────┴─────────────┴──────────────────────────┴───────────────────────────────┘\n");

        return printStringBuilder.toString();
    }
}
