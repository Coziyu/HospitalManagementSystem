package org.hms.services.drugdispensary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrugReplenishRequestTest {

    @Test
    void testToCSVString() {
        // Given
        DrugReplenishRequest request = new DrugReplenishRequest(1, "Aspirin", 50, "Urgent Replenish");

        // When
        String csvString = request.toCSVString();

        // Then
        assertEquals("1,Aspirin,50,Urgent Replenish", csvString);
    }

    @Test
    void testLoadFromCSVString() {
        // Given
        String csvLine = "2,Paracetamol,100,Regular Replenish";

        // When
        DrugReplenishRequest request = new DrugReplenishRequest();
        request.loadFromCSVString(csvLine);

        // Then
        assertEquals(2, request.getTableEntryID());
        assertEquals("Paracetamol", request.getDrugName());
        assertEquals(100, request.getAddQuantity());
        assertEquals("Regular Replenish", request.getNotes());
    }

    @Test
    void testToCSVString_withCommaInNotes() {
        // Given
        DrugReplenishRequest request = new DrugReplenishRequest(5, "Ibuprofen", 75, "Handle with care, keep dry");

        // When
        String csvString = request.toCSVString();

        // Then
        assertEquals("5,Ibuprofen,75,\"Handle with care, keep dry\"", csvString);
    }

    @Test
    void testLoadFromCSVString_withCommaInNotes() {
        // Given
        String csvLine = "6,Ibuprofen,75,\"Handle with care, keep dry\"";

        // When
        DrugReplenishRequest request = new DrugReplenishRequest();
        request.loadFromCSVString(csvLine);

        // Then
        assertEquals(6, request.getTableEntryID());
        assertEquals("Ibuprofen", request.getDrugName());
        assertEquals(75, request.getAddQuantity());
        assertEquals("Handle with care, keep dry", request.getNotes());
    }

    @Test
    void testLoadFromCSVString_withInvalidData() {
        // Given
        String csvLine = "3,,50,";  // Invalid drug name and notes

        // When
        DrugReplenishRequest request = new DrugReplenishRequest();
        request.loadFromCSVString(csvLine);

        // Then
        assertEquals(3, request.getTableEntryID());
        assertEquals("", request.getDrugName()); // Expecting empty string
        assertEquals(50, request.getAddQuantity());
        assertEquals("", request.getNotes()); // Expecting empty string
    }

    @Test
    void testToCSVString_withEmptyFields() {
        // Given
        DrugReplenishRequest request = new DrugReplenishRequest(4, "", 0, "");

        // When
        String csvString = request.toCSVString();

        // Then
        assertEquals("4,,0,", csvString);
    }
}
