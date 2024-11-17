package org.hms.services.appointment;

/**
 * The AppointmentStatus enum represents the various states an appointment can be in.
 * It is used to track and manage the status of an appointment in a system.
 * <p>
 * The possible statuses are:
 * - CONFIRMED: The appointment has been confirmed.
 * - CANCELLED: The appointment has been cancelled.
 * - COMPLETED: The appointment has been completed.
 * - PENDING: The appointment is pending and awaiting confirmation.
 */
public enum AppointmentStatus {
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    PENDING
}
