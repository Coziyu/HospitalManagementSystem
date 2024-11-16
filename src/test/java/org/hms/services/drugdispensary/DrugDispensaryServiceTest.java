package org.hms.services.drugdispensary;

import org.hms.services.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrugDispensaryServiceTest {

    private DrugDispensaryService drugDispensaryService;
    private DrugInventoryTable drugInventory;
    private DrugReplenishRequestTable drugReplenishRequestTable;


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
    class TestToString{
        @BeforeEach
        void setUp() {
            drugReplenishRequestTable = new DrugReplenishRequestTable();
            drugInventory = new DrugInventoryTable();
            try {
                drugReplenishRequestTable.addEntry(new DrugReplenishRequest(0, "Ibuprofen", 5, "Out of stock"));
                drugReplenishRequestTable.addEntry(new DrugReplenishRequest(1, "Aspirin", 10, "Out of stock?"));
                drugReplenishRequestTable.addEntry(new DrugReplenishRequest(2, "Paracetamol", 15, "Out ,of, \"stock\""));
                drugInventory.addEntry(new DrugInventoryEntry(1, "Aspirin", 50, 10));
                drugInventory.addEntry(new DrugInventoryEntry(2, "Ibuprofen", 20, 5));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            drugDispensaryService = new DrugDispensaryService(new StorageService());
            drugDispensaryService.drugReplenishRequestTable = drugReplenishRequestTable;
            drugDispensaryService.drugInventory = drugInventory;
        }
        @Test
        void getDrugReplenishRequestsAsString() {
            System.out.println(drugDispensaryService.getDrugReplenishRequestsAsString());
            String expected = """
                    ┌─────┬────────────────┬──────┬─────────────────────────────────────────────────────────────┐
                    │ ID  │ Name           │ Qty  │ Notes                                                       │
                    ├─────┼────────────────┼──────┼─────────────────────────────────────────────────────────────┤
                    │ 0   │ Ibuprofen      │ 5    │ Out of stock                                                │
                    │ 1   │ Aspirin        │ 10   │ Out of stock?                                               │
                    │ 2   │ Paracetamol    │ 15   │ Out ,of, "stock"                                            │
                    └─────┴────────────────┴──────┴─────────────────────────────────────────────────────────────┘
                    """;
            assertEquals(expected, drugDispensaryService.getDrugReplenishRequestsAsString());
        }
        @Test
        void getDrugInventoryTableAsString(){
            System.out.println(drugDispensaryService.getDrugInventoryAsString());
            String expected = """
                    ┌─────┬────────────────┬──────┬─────────────┐
                    │ ID  │ Name           │ Qty  │ lowStockQty │
                    ├─────┼────────────────┼──────┼─────────────┤
                    │ 1   │ Aspirin        │ 50   │ 10          │
                    │ 2   │ Ibuprofen      │ 20   │ 5           │
                    └─────┴────────────────┴──────┴─────────────┘
                    """;
            assertEquals(expected, drugDispensaryService.getDrugInventoryAsString());
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

    @Nested
    class TestLowStockAlert {
        @BeforeEach
        void setUp() {
            drugInventory = new DrugInventoryTable();
            try {
                drugInventory.addEntry(new DrugInventoryEntry(0, "Paracetamol", 15, 16));
                drugInventory.addEntry(new DrugInventoryEntry(1, "Aspirin", 50, 49));
                drugInventory.addEntry(new DrugInventoryEntry(2, "Ibuprofen", 20, 20));
                drugInventory.addEntry(new DrugInventoryEntry(3, "Metformin", 0, 5));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            drugDispensaryService = new DrugDispensaryService(new StorageService());
            drugDispensaryService.drugInventory = drugInventory;
        }

        @Test
        void getLowStockDrugs() {
            DrugInventoryTable lowStockDrugsTable = drugDispensaryService.getLowStockDrugs();
            List<DrugInventoryEntry> lowStockDrugs = lowStockDrugsTable.getEntries();
            System.out.println(lowStockDrugsTable.toPrintString());
            assertEquals(3, lowStockDrugs.size());
        }
    }
}
