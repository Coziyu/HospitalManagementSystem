package org.hms.services.medicalrecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class PatientTableTest {

    private PatientTable patientTable;
    @BeforeEach
    void setUp() {
        String dataRoot = System.getProperty("user.dir") + "/data/";
        patientTable = new PatientTable();

        // Save the original contents of the files into a backup
        try {
            patientTable.loadFromFile(dataRoot + "PatientList.csv");
            patientTable.saveToFile(dataRoot + "PatientList.csv.testback");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Clear the contents of the files
        patientTable = new PatientTable();
    }
    @AfterEach
    void tearDown() {
        String dataRoot = System.getProperty("user.dir") + "/data/";
        PatientTable patientTable = new PatientTable();

        // Restore the original contents of the files
        try {
            patientTable.loadFromFile(dataRoot + "PatientList.csv.testback");
            patientTable.saveToFile(dataRoot + "PatientList.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Delete the backup file
        try {
            Files.delete(Paths.get(dataRoot + "PatientList.csv.testback"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void toPrintString(){
        PatientTable patientTable = new PatientTable();
        try {
            patientTable.addEntry(new PatientParticulars(0, "PAT001", "Alice Brown", "1980-05-14", "Female", "POSITIVE_A"));
            patientTable.addEntry(new PatientParticulars(1, "PAT002", "Bob Smith", "1985-10-10", "Male", "NEGATIVE_A"));
            patientTable.addEntry(new PatientParticulars(2, "PAT003", "Charlie Davis", "1990-03-25", "Female", "NEGATIVE_AB"));
            patientTable.addEntry(new PatientParticulars(3, "PAT004", "David Johnson", "1988-12-05", "Male", "NEGATIVE_O"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(patientTable.toPrintString());
    }
}