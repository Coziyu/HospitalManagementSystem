package org.hms.services.storage;

import org.hms.services.drugdispensary.DrugInventoryEntry;
import org.hms.services.drugdispensary.DrugInventoryTable;
import org.hms.services.drugdispensary.DrugReplenishRequestTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    private DrugInventoryTable drugInventoryTable;
    private DrugReplenishRequestTable drugReplenishRequestTable;

    @BeforeEach
    void setUp() {
        drugInventoryTable = new DrugInventoryTable();
        drugReplenishRequestTable = new DrugReplenishRequestTable();

        // Save the original contents of the files into a backup
        try {
            drugInventoryTable.loadFromFile("data/drugInventory.csv");
            drugReplenishRequestTable.loadFromFile("data/drugReplenishRequests.csv");

            drugInventoryTable.saveToFile("data/drugInventory.csv.testback");
            drugReplenishRequestTable.saveToFile("data/drugReplenishRequests.csv.testback");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Clears the entries
        drugInventoryTable = new DrugInventoryTable();
        drugReplenishRequestTable = new DrugReplenishRequestTable();
    }
    @AfterEach
    void tearDown() {
        // Restore the original contents of the files
        try {
            drugInventoryTable.loadFromFile("data/drugInventory.csv.testback");
            drugReplenishRequestTable.loadFromFile("data/drugReplenishRequests.csv.testback");

            drugInventoryTable.saveToFile("data/drugInventory.csv");
            drugReplenishRequestTable.saveToFile("data/drugReplenishRequests.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Delete the backup files
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("data/drugInventory.csv.testback"));
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("data/drugReplenishRequests.csv.testback"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAppointmentsPendingDispensary() {
        // TODO: Write this when implemented
    }

    @Test
    void getDrugInventory() {
        assertEquals(0, drugInventoryTable.getEntries().size());
    }
    @Test
    void getDrugInventorWithEntries() {
        DrugInventoryEntry entry = drugInventoryTable.createValidEntryTemplate();
        entry.setName("Myelin");
        entry.setQuantity(5);
        entry.setLowStockAlertThreshold(7);

        try {
            drugInventoryTable.addEntry(entry);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        assertEquals(1, drugInventoryTable.getEntries().size());

        try {
            drugInventoryTable.saveToFile("data/drugInventory.csv");

            drugInventoryTable.removeEntry(entry.getID());

            drugInventoryTable.saveToFile("data/drugInventory.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    void getDrugReplenishRequestTable() {
        
    }
}