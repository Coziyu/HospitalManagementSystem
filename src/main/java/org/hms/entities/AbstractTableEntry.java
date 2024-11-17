package org.hms.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for table entries in the Hospital Management System (HMS).
 * <p>
 * This class provides common functionality for handling CSV data and managing
 * unique identifiers for table entries.
 */
public abstract class AbstractTableEntry implements Serializable {

    /**
     * Represents the unique identifier for an entry in a table.
     * This variable is protected to allow access within subclasses.
     */
    protected int tableEntryID;

    /**
     * Initializes an instance of AbstractTableEntry with the specified table entry ID.
     *
     * @param tableEntryID the unique identifier for the table entry
     */
    public AbstractTableEntry(int tableEntryID) {
        this.tableEntryID = tableEntryID;
    }

    /**
     * Preprocesses a string into expect CSV String entry format.
     *
     * @param input
     * @return Formatted CSV String
     */
    public static String preprocessCSVString(String input) {
        // Step 1: Double any instances of " in the input string
        String doubledQuotes = input.replaceAll("\"", "\"\"");

        // Step 2: If the string contains a comma, wrap the entire string in quotes
        if (doubledQuotes.contains(",")) {
            return "\"" + doubledQuotes + "\"";
        } else {
            return doubledQuotes;
        }
    }

    /**
     * Parses a properly formatted CSVLine into an Array of Strings
     *
     * @param csvLine
     * @return Array of CSVLine Entries, casted to String
     */
    public static String[] parseCSVLine(String csvLine) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < csvLine.length(); i++) {
            char c = csvLine.charAt(i);

            if (c == '"') {
                // If the quote is escaped, append it to the current field
                if (i + 1 < csvLine.length() && csvLine.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++;
                } else {
                    // Toggle the inQuotes flag
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Comma outside of quotes signals the end of a field
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                // Append the character to the current field
                currentField.append(c);
            }
        }

        // Add the last field
        result.add(currentField.toString());

        return result.toArray(new String[0]);
    }

    /**
     * Retrieves the unique identifier for the table entry.
     *
     * @return the unique identifier for the table entry
     */
    public int getTableEntryID() {
        return tableEntryID;
    }

    /**
     * Sets the unique identifier for the table entry.
     *
     * @param tableEntryID the unique identifier for the table entry
     */
    public void setTableEntryID(int tableEntryID) {
        this.tableEntryID = tableEntryID;
    }

    /**
     * Concrete classes must define how to save CSV.
     *
     * @return a csvLine representation of the data.
     */
    public abstract String toCSVString();

    /**
     * Concrete classes must define how to load CSV.
     *
     * @param csvLine comma seperated entry values.
     */
    public abstract void loadFromCSVString(String csvLine);
}
