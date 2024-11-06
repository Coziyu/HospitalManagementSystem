    package org.hms.services.drugdispensary;

    import org.junit.jupiter.api.Test;

    import static org.junit.jupiter.api.Assertions.*;

    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;

    class DrugRequestTest {

        @Test
        void toCSVString() {
            DrugRequest request = new DrugRequest(1, "Aspirin", 10, DrugRequestStatus.PENDING);
            String expectedCSV = "1,Aspirin,10,PENDING";

            assertEquals(expectedCSV, request.toCSVString(), "The CSV output should match the expected format.");
        }

        @Test
        void loadFromCSVString() {
            String csvString = "2,Ibuprofen,5,DISPENSED";
            DrugRequest request = new DrugRequest(0, "", 0, DrugRequestStatus.PENDING);

            request.loadFromCSVString(csvString);

            assertEquals(2, request.getID(), "ID should be parsed correctly from CSV.");
            assertEquals("Ibuprofen", request.getDrugName(), "Drug name should be parsed correctly from CSV.");
            assertEquals(5, request.quantity, "Quantity should be parsed correctly from CSV.");
            assertEquals(DrugRequestStatus.DISPENSED, request.getStatus(), "Status should be parsed correctly from CSV.");
        }

        @Test
        void loadFromCSVStringInvalidFormat() {
            String csvString = "Invalid,CSV,Format";

            DrugRequest request = new DrugRequest(0, "", 0, DrugRequestStatus.PENDING);

            assertThrows(Exception.class, () -> {
                request.loadFromCSVString(csvString);
            }, "Invalid CSV format should throw an Exception.");
        }

        @Test
        void loadFromCSVStringInvalidStatus() {
            String csvString = "3,Paracetamol,20,INVALID_STATUS";

            DrugRequest request = new DrugRequest(0, "", 0, DrugRequestStatus.PENDING);

            assertThrows(IllegalArgumentException.class, () -> {
                request.loadFromCSVString(csvString);
            }, "Invalid status in CSV should throw an IllegalArgumentException.");
        }
    }
