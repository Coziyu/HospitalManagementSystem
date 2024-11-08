package org.hms.services.appointment;

public class AppointmentSchedule {
    private String[][] matrix;  // Stores the schedule with doctor IDs and time slots included

    // Constructor initializes the matrix with predefined dimensions
    public AppointmentSchedule(int numDoctors, int numTimeSlots) {

        this.matrix = new String[numTimeSlots + 1][numDoctors + 1];
        this.matrix[0][0] = "Time/Doctor";  // Top-left cell as header label
    }


    public void setDoctorID(int col, String doctorID) {
        matrix[0][col + 1] = doctorID;  // +1 to skip header cell
    }


    public void setTimeSlot(int row, String timeSlot) {
        matrix[row + 1][0] = timeSlot;  // +1 to skip header cell
    }


    public void setSlot(int timeSlotRow, int doctorCol, String value) {
        matrix[timeSlotRow + 1][doctorCol + 1] = value;  // Adjust for headers
    }


    public String getDoctorID(int col) {
        return matrix[0][col + 1];  // +1 to skip header cell
    }


    public String getTimeSlot(int row) {
        return matrix[row + 1][0];  // +1 to skip header cell
    }


    public String getSlot(int timeSlotRow, int doctorCol) {
        return matrix[timeSlotRow + 1][doctorCol + 1];  // Adjust for headers
    }


    public String[][] getMatrix() {
        return matrix;
    }
}
