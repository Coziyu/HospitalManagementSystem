    package org.hms.services.drugdispensary;

    import org.junit.jupiter.api.Test;

    import static org.junit.jupiter.api.Assertions.*;

    class DrugDispenseRequestTest {

        @Test
        void toCSVString() {
            DrugDispenseRequest request = new DrugDispenseRequest(1, "Aspirin", 10, DrugRequestStatus.PENDING);
            String expectedCSV = "1,Aspirin,10,PENDING";

            assertEquals(expectedCSV, request.toCSVString(), "The CSV output should match the expected format.");
        }

        @Test
        void toCSVStringCommaInName(){
            DrugDispenseRequest request = new DrugDispenseRequest(1, "Asp,irin", 10, DrugRequestStatus.PENDING);
            String expectedCSV = "1,\"Asp,irin\",10,PENDING";

            assertEquals(expectedCSV, request.toCSVString(), "The CSV output should match the expected format.");
        }

        @Test
        void toCSVStringQuoteInName(){
            DrugDispenseRequest request = new DrugDispenseRequest(1, "\"Ass\"pi\"r\"in", 10, DrugRequestStatus.PENDING);
            String expectedCSV = "1,\"\"Ass\"\"pi\"\"r\"\"in,10,PENDING";

            assertEquals(expectedCSV, request.toCSVString(), "The CSV output should match the expected format.");
        }

        @Test
        void toCSVStringCommaAndQuoteInName(){
            DrugDispenseRequest request = new DrugDispenseRequest(1, "\"Ass\"p,i\"rin", 10, DrugRequestStatus.PENDING);
            String expectedCSV = "1,\"\"\"Ass\"\"p,i\"\"rin\",10,PENDING";

            assertEquals(expectedCSV, request.toCSVString(), "The CSV output should match the expected format.");
        }

        @Test
        void loadFromCSVStringCommaAndQuoteInName(){
            String csvString = "1,\"\"\"Ass\"\"p,i\"\"rin\",10,PENDING";
            DrugDispenseRequest request = new DrugDispenseRequest(1, "\"Ass\"p,i\"rin", 10, DrugRequestStatus.PENDING);

            request.loadFromCSVString(csvString);

            assertEquals(1, request.getTableEntryID());
            assertEquals("\"Ass\"p,i\"rin", request.getDrugName());
            assertEquals(10, request.getQuantity());
            assertEquals(DrugRequestStatus.PENDING, request.getStatus());
        }

        @Test
        void loadFromCSVString() {
            String csvString = "2,Ibuprofen,5,DISPENSED";
            DrugDispenseRequest request = new DrugDispenseRequest(0, "", 0, DrugRequestStatus.PENDING);

            request.loadFromCSVString(csvString);

            assertEquals(2, request.getTableEntryID(), "ID should be parsed correctly from CSV.");
            assertEquals("Ibuprofen", request.getDrugName(), "Drug name should be parsed correctly from CSV.");
            assertEquals(5, request.quantity, "Quantity should be parsed correctly from CSV.");
            assertEquals(DrugRequestStatus.DISPENSED, request.getStatus(), "Status should be parsed correctly from CSV.");
        }

        @Test
        void loadFromCSVStringInvalidFormat() {
            String csvString = "Invalid,CSV,Format";

            DrugDispenseRequest request = new DrugDispenseRequest(0, "", 0, DrugRequestStatus.PENDING);

            assertThrows(Exception.class, () -> {
                request.loadFromCSVString(csvString);
            }, "Invalid CSV format should throw an Exception.");
        }

        @Test
        void loadFromCSVStringInvalidStatus() {
            String csvString = "3,Paracetamol,20,INVALID_STATUS";

            DrugDispenseRequest request = new DrugDispenseRequest(0, "", 0, DrugRequestStatus.PENDING);

            assertThrows(IllegalArgumentException.class, () -> {
                request.loadFromCSVString(csvString);
            }, "Invalid status in CSV should throw an IllegalArgumentException.");
        }
    }
