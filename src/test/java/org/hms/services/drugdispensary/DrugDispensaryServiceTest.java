package org.hms.services.drugdispensary;

import org.hms.services.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrugDispensaryServiceTest {

    private DrugDispensaryService drugDispensaryService;
    private DrugInventoryTable drugInventory;

    @Nested
    class TestDispense {
        @BeforeEach
        void setUp() {
            drugInventory = new DrugInventoryTable();
            try {
                drugInventory.addEntry(new DrugInventoryEntry(1, "Aspirin", 50, 10));
                drugInventory.addEntry(new DrugInventoryEntry(2, "Ibuprofen", 20, 5));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            StorageService storageService = new StorageService();

            drugDispensaryService = new DrugDispensaryService(storageService);
            drugDispensaryService.drugInventory = drugInventory; // Set inventory directly
        }

        @Test
        void dispenseDrug_Success() {
            DrugDispenseRequest request = new DrugDispenseRequest(2, "Aspirin", 10, DrugRequestStatus.PENDING);

            boolean result = drugDispensaryService.dispenseDrug(request);

            assertTrue(result);
            assertEquals(DrugRequestStatus.DISPENSED, request.getStatus());
            assertEquals(40, drugInventory.searchByAttribute(DrugInventoryEntry::getName, "Aspirin").get(0).getQuantity());
        }

        @Test
        void dispenseDrug_InsufficientQuantity() {
            DrugDispenseRequest request = new DrugDispenseRequest(5, "Ibuprofen", 25, DrugRequestStatus.PENDING);

            boolean result = drugDispensaryService.dispenseDrug(request);

            assertFalse(result);
            assertEquals(DrugRequestStatus.PENDING, request.getStatus());
            assertEquals(20, drugInventory.searchByAttribute(DrugInventoryEntry::getName, "Ibuprofen").get(0).getQuantity());
        }

        @Test
        void dispenseDrug_DrugNotFound() {
            DrugDispenseRequest request = new DrugDispenseRequest(43,"Paracetamol", 5, DrugRequestStatus.PENDING);

            boolean result = drugDispensaryService.dispenseDrug(request);

            assertFalse(result);
            assertEquals(DrugRequestStatus.PENDING, request.getStatus());
        }
    }

    @Nested
    class TestReplenish {
        @BeforeEach
        void setUp() {

        }

        @Test
        void submitReplenishRequest() {

        }
    }
}
