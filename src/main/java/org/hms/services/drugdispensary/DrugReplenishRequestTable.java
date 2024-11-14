package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

public class DrugReplenishRequestTable extends AbstractTable<DrugReplenishRequest> {
    private static final String[] HEADERS = {"ID", "Name", "Qty", "Notes"};
    private static final String formatString = "│ %-4s│ %-15s│ %-5s│ %-30s│\n";

    /**
     * This constructor creates a new DrugReplenishRequestTable without linking to a file.
     * Calls to saveToFile() will have no effect.
     * Calling saveToFile(String filePath) will NOT link the table to a file too.
     * To link to a file, either use the constructor that takes a filePath parameter.
     * Or call setFilePath(String filePath).
     */
    public DrugReplenishRequestTable(){
        super();
    }

    public DrugReplenishRequestTable(String filePath){
        super();
        this.filePath = filePath;
    }

    /**
     * @return
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * @return
     */
    @Override
    public DrugReplenishRequest createValidEntryTemplate() {
        return new DrugReplenishRequest(getUnusedID(), null, -1, null);
    }

    @Override
    protected AbstractTable<DrugReplenishRequest> createEmpty() {
        return new DrugReplenishRequestTable(filePath);
    }


    /**
     * This method will call each entry's toPrintString method to generate table contents.
     * It will then add box drawing characters to create an ASCII table.
     * @return A string representation of the table in an ASCII table format.
     *         This string is meant to be printed to the console.
     */
    public String toPrintString(){
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌─────┬────────────────┬──────┬───────────────────────────────┐\n");
//        printStringBuilder.append(String.format(formatString, "ID", "Name", "Qty", "Notes"));
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├─────┼────────────────┼──────┼───────────────────────────────┤\n");
        for (DrugReplenishRequest entry : getEntries()){
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└─────┴────────────────┴──────┴───────────────────────────────┘\n");
        return printStringBuilder.toString();
    }

}
