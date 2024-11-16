package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;

public class ContactInfomationTable extends AbstractTable<ContactInformation> {
    private static final String[] HEADERS = {"Entry", "PatientID", "PhoneNumber", "Email", "Address"};
    private static final String formatString = "│ %-6s│ %-11s│ %-12s│ %-20s│ %-30s│\n";

    public ContactInfomationTable() {
        super();
    }

    public ContactInfomationTable(String filePath) {
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
    protected ContactInfomationTable createEmpty() {
        return new ContactInfomationTable();
    }


    public String toPrintString(){
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌───────┬────────────┬─────────────┬─────────────────────┬───────────────────────────────┐\n");
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├───────┼────────────┼─────────────┼─────────────────────┼───────────────────────────────┤\n");
        for (ContactInformation entry : getEntries()){
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└───────┴────────────┴─────────────┴─────────────────────┴───────────────────────────────┘\n");

        return printStringBuilder.toString();
    }
}
