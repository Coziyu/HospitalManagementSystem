package org.hms.services.appointment;



import org.hms.services.drugdispensary.DrugDispenseRequest;
import org.hms.services.storage.StorageService;

import java.util.ArrayList;
import java.util.Scanner;

public class AppointmentOutcomeTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StorageService storageService = new StorageService();
        AppointmentService appointmentService = new AppointmentService(storageService);

        // Collect data for AppointmentOutcome fields
        // Step 1: Create an ArrayList<DrugDispenseRequest>
        ArrayList<DrugDispenseRequest> prescribedMedication = appointmentService.createNewArrayOfDrugDispenseRequest();


        // Step 2: Add DrugDispenseRequest objects to the list
        System.out.print("Enter number of medications to prescribe: ");
        int medicationCount = Integer.parseInt(scanner.nextLine());

        for (int i = 1; i <= medicationCount; i++) {
            System.out.print("Enter name of drug " + i + ": ");
            String drugName = scanner.nextLine();

            System.out.print("Enter quantity for " + drugName + ": ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Use the addDrugDispenseRequest method to add each DrugDispenseRequest to the list
            appointmentService.addDrugDispenseRequest(prescribedMedication, drugName, quantity);
        }

        // Step 3: Collect data for the AppointmentOutcome fields
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine();

        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();

        System.out.print("Enter Type of Appointment: ");
        String typeOfAppointment = scanner.nextLine();

        System.out.print("Enter Consultation Notes (use ',' and '/' if needed): ");
        String consultationNotes = scanner.nextLine();

        // Use createNewAppointmentOutcome to create an AppointmentOutcome object
        AppointmentOutcome newOutcome = appointmentService.createNewAppointmentOutcome(appointmentID, patientID, typeOfAppointment, consultationNotes, prescribedMedication);

        // Step 4: Write AppointmentOutcome to CSV
        storageService.writeAppointmentOutcomeToCSV(newOutcome);

        System.out.println("AppointmentOutcome has been written to the CSV file.");
        scanner.close();

        //ArrayList<AppointmentOutcome> outcomes = storageService.readAppointmentOutcomesFromCSV();
        appointmentService.displayPendingPrescriptions();

        appointmentService.updatePrescriptionStatus("A333");

        appointmentService.displayPendingPrescriptions();
        appointmentService.updateAppointmentOutcometoCSV();


        /*for (AppointmentOutcome outcome : outcomes) {
            System.out.println("Appointment ID: " + outcome.getAppointmentID());
            System.out.println("Patient ID: " + outcome.getPatientID());
            System.out.println("Type of Appointment: " + outcome.getTypeOfAppointment());
            System.out.println("Consultation Notes: " + outcome.getConsultationNotes());

            // Display each drug and its details in the prescribed medication list
            System.out.println("Prescribed Medication:");
            for (DrugDispenseRequest request : outcome.getPrescribedMedication()) {
                System.out.println("    Drug Name: " + request.getDrugName());
                System.out.println("    Quantity: " + request.getQuantity());
                System.out.println("    Status: " + request.getStatus());
            }
            System.out.println("------------------------------------------------------");
        }*/
    }
}