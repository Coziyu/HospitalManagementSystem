package org.hms.services.appointment;

/**
 * The AppointmentSchedule class is responsible for storing and managing a scheduling matrix
 * which includes information about doctor IDs and their assigned time slots.
 */
public class AppointmentSchedule {
    /**
     * A 2D String array representing the scheduling matrix.
     * This matrix holds information about doctor IDs and the corresponding time slots.
     * The top row and leftmost column serve as headers for doctor IDs and time slots respectively.
     */
    private String[][] matrix;  // Stores the schedule with doctor IDs and time slots included

    /**
     * Constructs an AppointmentSchedule object with a specified number of doctors
     * and time slots. The scheduling matrix is initialized with these dimensions,
     * and the top-left cell is set as the header label.
     *
     * @param numDoctors   the number of doctors to be included in the schedule
     * @param numTimeSlots the number of time slots to be included in the schedule
     */
    // Constructor initializes the matrix with predefined dimensions
    public AppointmentSchedule(int numDoctors, int numTimeSlots) {

        this.matrix = new String[numTimeSlots + 1][numDoctors + 1];
        this.matrix[0][0] = "Time/Doctor";  // Top-left cell as header label
    }


    /**
     * Sets the ID of a doctor in the scheduling matrix.
     *
     * @param col      An integer representing the column index where the doctor ID should be placed.
     * @param doctorID A String representing the ID of the doctor to be set in the specified column.
     */
    public void setDoctorID(int col, String doctorID) {
        matrix[0][col + 1] = doctorID;  // +1 to skip header cell
    }


    /**
     * Sets the time slot string at the specified row in the schedule matrix.
     *
     * @param row      the index of the row where the time slot should be set (0-based, excluding header)
     * @param timeSlot the time slot string to be inserted into the matrix
     */
    public void setTimeSlot(int row, String timeSlot) {
        matrix[row + 1][0] = timeSlot;  // +1 to skip header cell
    }


    /**
     * Sets a value for a specific time slot and doctor in the scheduling matrix.
     *
     * @param timeSlotRow the row index of the time slot (0-based)
     * @param doctorCol   the column index of the doctor (0-based)
     * @param value       the value to set in the slot
     */
    public void setSlot(int timeSlotRow, int doctorCol, String value) {
        matrix[timeSlotRow + 1][doctorCol + 1] = value;  // Adjust for headers
    }


    /**
     * Retrieves the doctor ID at the specified column index in the scheduling matrix.
     *
     * @param col The index of the column to retrieve the doctor ID from, excluding the header cell.
     * @return The doctor ID located at the specified column index.
     */
    public String getDoctorID(int col) {
        return matrix[0][col + 1];  // +1 to skip header cell
    }


    /**
     * Retrieves the time slot string at the specified row in the schedule matrix.
     *
     * @param row the row index from which to retrieve the time slot, 0-based
     * @return the time slot string associated with the specified row
     */
    public String getTimeSlot(int row) {
        return matrix[row + 1][0];  // +1 to skip header cell
    }


    /**
     * Retrieves the value of a specific slot in the scheduling matrix.
     *
     * @param timeSlotRow The row index corresponding to the desired time slot (0-based).
     * @param doctorCol   The column index corresponding to the desired doctor (0-based).
     * @return The value stored in the specified slot of the scheduling matrix.
     */
    public String getSlot(int timeSlotRow, int doctorCol) {
        return matrix[timeSlotRow + 1][doctorCol + 1];  // Adjust for headers
    }


    /**
     * Retrieves the scheduling matrix containing doctor IDs and their assigned time slots.
     *
     * @return A 2D String array representing the schedule with doctor IDs and time slots.
     */
    public String[][] getMatrix() {
        return matrix;
    }

    /**
     * Updates the matrix with a new scheduling matrix.
     *
     * @param newMatrix The new scheduling matrix containing updated
     *                  doctor IDs and time slot information.
     */
    public void setMatrix(String[][] newMatrix) {
        this.matrix = newMatrix;
    }
}
