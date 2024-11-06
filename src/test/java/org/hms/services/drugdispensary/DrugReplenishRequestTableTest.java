package org.hms.services.drugdispensary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DrugReplenishRequestTableTest {

    private DrugReplenishRequestTable drugReplenishRequestTable;

    @BeforeEach
    void setUp() {
        drugReplenishRequestTable = new DrugReplenishRequestTable();

        try {
        DrugReplenishRequest req1 = drugReplenishRequestTable.createValidEntryTemplate();
        req1.setDrugName("Druggy");
        req1.setAddQuantity(10);
        req1.setNotes("Wowzers");
            drugReplenishRequestTable.addEntry(req1);
        DrugReplenishRequest req2 = drugReplenishRequestTable.createValidEntryTemplate();
        req2.setDrugName("Cruggy");
        req2.setAddQuantity(5);
        req2.setNotes("Momzers");
            drugReplenishRequestTable.addEntry(req2);
        DrugReplenishRequest req3 = drugReplenishRequestTable.createValidEntryTemplate();
        req3.setDrugName("Bruggy");
        req3.setAddQuantity(5);
        req3.setNotes("Lol");
            drugReplenishRequestTable.addEntry(req3);
        DrugReplenishRequest req4 = drugReplenishRequestTable.createValidEntryTemplate();
        req4.setDrugName("Wolfram");
        req4.setAddQuantity(5);
        req4.setNotes("Sup");
            drugReplenishRequestTable.addEntry(req4);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void searchByAttribute() {
        ArrayList<DrugReplenishRequest> results = drugReplenishRequestTable.searchByAttribute(DrugReplenishRequest::getAddQuantity, 5);
        System.out.println(results);
        assertEquals(3, results.size());
    }
}