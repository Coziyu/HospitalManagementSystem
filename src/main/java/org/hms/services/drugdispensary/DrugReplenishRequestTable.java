package org.hms.services.drugdispensary;

import org.hms.entities.AbstractTable;

/**
 * Represents a table for managing and storing drug replenish requests.
 * Provides functionality to handle entries, headers, and file operations
 * associated with drug replenish requests.
 */
public class DrugReplenishRequestTable extends AbstractTable<DrugReplenishRequest> {
    /**
     * Represents the headers for the Drug Replenish Request table.
     * These headers correspond to the fields of the drug replenish request entries.
     */
    private static final String[] HEADERS = {"ID", "Name", "Qty", "Notes"};
    /**
     * Format string used to define the structure of the ASCII table rows.
     * The format follows the pattern:
     * "│ %-4s│ %-15s│ %-5s│ %-60s│\n"
     * The placeholders represent:
     * - ID (4 characters wide)
     * - Drug Name (15 characters wide)
     * - Quantity to Add (5 characters wide)
     * - Notes (60 characters wide)
     */
    private static final String formatString = "│ %-4s│ %-15s│ %-5s│ %-60s│\n";

    /**
     * This constructor creates a new DrugReplenishRequestTable without linking to a file.
     * Calls to saveToFile() will have no effect.
     * Calling saveToFile(String filePath) will NOT link the table to a file too.
     * To link to a file, either use the constructor that takes a filePath parameter.
     * Or call setFilePath(String filePath).
     */
    public DrugReplenishRequestTable() {
        super();
    }

    /**
     * Creates a new DrugReplenishRequestTable and links it to a specified file path.
     *
     * @param filePath the path of the file to link with this table.
     */
    public DrugReplenishRequestTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    /**
     * Retrieves headers for the drug replenish request tables
     *
     * @return an array of header names
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * Creates a template entry for a valid DrugReplenishRequest
     * Entry has an unused ID and placeholder values
     *
     * @return a new DrugReplenishRequest template
     */
    @Override
    public DrugReplenishRequest createValidEntryTemplate() {
        return new DrugReplenishRequest(getUnusedID(), null, -1, null);
    }

    /**
     * Creates an empty instance of DrugReplenishRequestTable.
     *
     * @return an empty DrugReplenishRequestTable instance.
     */
    @Override
    protected DrugReplenishRequestTable createEmpty() {
        return new DrugReplenishRequestTable();
    }


    /**
     * This method will call each entry's toPrintString method to generate table contents.
     * It will then add box drawing characters to create an ASCII table.
     *
     * @return A string representation of the table in an ASCII table format.
     * This string is meant to be printed to the console.
     */
    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌─────┬────────────────┬──────┬─────────────────────────────────────────────────────────────┐\n");
//        printStringBuilder.append(String.format(formatString, "ID", "Name", "Qty", "Notes"));
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├─────┼────────────────┼──────┼─────────────────────────────────────────────────────────────┤\n");
        for (DrugReplenishRequest entry : getEntries()) {
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└─────┴────────────────┴──────┴─────────────────────────────────────────────────────────────┘\n");
        return printStringBuilder.toString();
    }

}
