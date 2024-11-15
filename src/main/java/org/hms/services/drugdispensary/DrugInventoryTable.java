package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

public class DrugInventoryTable extends AbstractTable<DrugInventoryEntry> {
    private static final String[] HEADERS = {"ID", "Name", "Qty", "lowStockQty"};
    private static final String formatString = "│ %-4s│ %-15s│ %-5s│ %-12s│\n";

    /**
     * This constructor creates a new DrugInventoryTable without linking to a file.
     * Calls to saveToFile() will have no effect.
     * Calling saveToFile(String filePath) will NOT link the table to a file too.
     * To link to a file, either use the constructor that takes a filePath parameter.
     * Or call setFilePath(String filePath).
     */
    public DrugInventoryTable() {
        super();
    }

    /**
     * Creates a new DrugInventoryTable and links it to a specified file path.
     *
     * @param filePath the path of the file to link with this table.
     */
    public DrugInventoryTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    /**
     * Retrieves the headers for the drug inventory table.
     *
     * @return an array of header names.
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * Creates a template entry for a valid DrugInventoryEntry.
     * This entry has an unused ID and placeholder values.
     *
     * @return a new DrugInventoryEntry template.
     */
    @Override
    public DrugInventoryEntry createValidEntryTemplate() {
        return new DrugInventoryEntry(getUnusedID(), null, -1, -1);
    }

    /**
     * Creates an empty instance of DrugInventoryTable.
     *
     * @return an empty DrugInventoryTable instance.
     */
    @Override
    protected DrugInventoryTable createEmpty() {
        return new DrugInventoryTable();
    }

    /**
     * This method will call each entry's toPrintString() method to generate a string representation of the table.
     * It will then add box drawing characters to create an ASCII table.
     * @return A string representation of the table in an ASCII table format.
     *         This string is meant to be printed to the console.
     */
    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌─────┬────────────────┬──────┬─────────────┐\n");
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├─────┼────────────────┼──────┼─────────────┤\n");
        for (DrugInventoryEntry entry : getEntries()) {
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└─────┴────────────────┴──────┴─────────────┘\n");
        return printStringBuilder.toString();
    }
}
