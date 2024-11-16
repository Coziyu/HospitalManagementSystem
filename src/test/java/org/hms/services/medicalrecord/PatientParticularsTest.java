package org.hms.services.medicalrecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class PatientParticularsTest {
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
    void loadFromCSVString() {
        // Test loading and saving
        String dataRoot = System.getProperty("user.dir") + "/data/";
        try {
            patientTable.loadFromFile(dataRoot + "PatientList.csv");
            patientTable.setFilePath(dataRoot + "TestPatientList.csv");
            patientTable.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}