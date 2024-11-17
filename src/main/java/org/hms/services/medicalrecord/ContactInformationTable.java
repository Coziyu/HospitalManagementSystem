package org.hms.services.medicalrecord;

import org.hms.entities.AbstractTable;

/**
 * This class represents a table of contact information, providing functionality
 * to manage, format, and render contact entries such as phone numbers and emails.
 * It extends the AbstractTable class and operates on ContactInformation objects.
 */
public class ContactInformationTable extends AbstractTable<ContactInformation> {
    /**
     * An array of strings representing the headers for the contact information table.
     * The headers provided are: "Entry", "PatientID", "PhoneNumber", "Email", and "Address".
     * This array is used for defining the column names in tables related
     * to contact information of patients.
     */
    private static final String[] HEADERS = {"Entry", "PatientID", "PhoneNumber", "Email", "Address"};
    /**
     * A string format template used for printing rows of contact information in a tabular format.
     * Each entry in the table is formatted into columns, where:
     * - The first column is 6 characters wide (Entry ID).
     * - The second column is 11 characters wide (Patient ID).
     * - The third column is 12 characters wide (Phone Number).
     * - The fourth column is 25 characters wide (Email).
     * - The fifth column is 30 characters wide (Address).
     */
    private static final String formatString = "│ %-6s│ %-11s│ %-12s│ %-25s│ %-30s│\n";

    /**
     * Constructs an instance of the ContactInformationTable.
     * This constructor invokes the superclass's constructor to initialize
     * the entries list and set the file path to an empty string.
     */
    public ContactInformationTable() {
        super();
    }

    /**
     * Constructs a ContactInformationTable with the specified file path.
     *
     * @param filePath the path to the file containing the contact information data
     */
    public ContactInformationTable(String filePath) {
        super();
        this.filePath = filePath;
    }

    /**
     * Retrieves the headers for the contact information table.
     *
     * @return an array of strings representing the headers of the contact information table,
     * including "Entry", "PatientID", "PhoneNumber", "Email", and "Address".
     */
    @Override
    public String[] getHeaders() {
        return HEADERS;
    }

    /**
     * Creates a valid entry template for contact information.
     *
     * @return a new ContactInformation object with default values.
     */
    @Override
    public ContactInformation createValidEntryTemplate() {
        return new ContactInformation(-1, "", "", "", "");
    }

    /**
     * Creates and returns an empty instance of ContactInformationTable.
     *
     * @return A new and empty instance of ContactInformationTable.
     */
    @Override
    protected ContactInformationTable createEmpty() {
        return new ContactInformationTable();
    }


    /**
     * Generates a formatted string representation of the contact information table,
     * including headers and entries.
     *
     * @return a string containing the table of contact information formatted with
     * borders and headers.
     */
    public String toPrintString() {
        StringBuilder printStringBuilder = new StringBuilder(entries.size() * 100);
        printStringBuilder.append("┌───────┬────────────┬─────────────┬──────────────────────────┬───────────────────────────────┐\n");
        printStringBuilder.append(String.format(formatString, (Object[]) HEADERS));
        printStringBuilder.append("├───────┼────────────┼─────────────┼──────────────────────────┼───────────────────────────────┤\n");
        for (ContactInformation entry : getEntries()) {
            printStringBuilder.append(entry.toPrintString(formatString));
        }
        printStringBuilder.append("└───────┴────────────┴─────────────┴──────────────────────────┴───────────────────────────────┘\n");

        return printStringBuilder.toString();
    }
}
