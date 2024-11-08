package org.hms.services.appointment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AppointmentCsvTool {

    private String filePath;

    // Constructor to initialize file path
    public AppointmentCsvTool(String filePath) {
        this.filePath = filePath;
    }

    // Method to read appointments from CSV
    public List<AppointmentInformation> readAppointments() {
        List<AppointmentInformation> appointments = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assumes comma as delimiter
                // Parse values and create an AppointmentInformation object
                int appointmentID = Integer.parseInt(values[0]);
                String patientID = values[1];
                String doctorID = values[2];
                Date appointmentTimeSlot = new SimpleDateFormat("yyyy-MM-dd HH:mm-HH:mm").parse(values[3]);
                AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(values[4]);

                appointments.add(new AppointmentInformation(appointmentID, patientID, doctorID, appointmentTimeSlot, appointmentStatus));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return appointments;
    }


}
