package hms.model;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    private String specialization;
    private String qualification;
    private String availability;
    private double consultationFee;
    private List<String> availableDays;
    private List<String> availableTimeSlots;

    // Constructor with basic fields
    public Doctor(String id, String name, int age, String contact, String specialization, String availability) {
        super(id, name, age, contact);
        this.specialization = specialization;
        this.availability = availability;
        this.availableDays = new ArrayList<>();
        this.availableTimeSlots = new ArrayList<>();
    }

    // Full constructor
    public Doctor(String id, String name, int age, String contact, String email, String address,
                  String gender, String specialization, String qualification, String availability, double consultationFee) {
        super(id, name, age, contact, email, address, gender);
        this.specialization = specialization;
        this.qualification = qualification;
        this.availability = availability;
        this.consultationFee = consultationFee;
        this.availableDays = new ArrayList<>();
        this.availableTimeSlots = new ArrayList<>();
    }

    // Getters and setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        if (consultationFee < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative");
        }
        this.consultationFee = consultationFee;
    }

    public List<String> getAvailableDays() {
        return new ArrayList<>(availableDays); // Return a copy to maintain encapsulation
    }

    public void setAvailableDays(List<String> days) {
        this.availableDays = new ArrayList<>(days);
    }

    public void addAvailableDay(String day) {
        this.availableDays.add(day);
    }

    public List<String> getAvailableTimeSlots() {
        return new ArrayList<>(availableTimeSlots); // Return a copy to maintain encapsulation
    }

    public void setAvailableTimeSlots(List<String> timeSlots) {
        this.availableTimeSlots = new ArrayList<>(timeSlots);
    }

    public void addAvailableTimeSlot(String timeSlot) {
        this.availableTimeSlots.add(timeSlot);
    }

    public String toFileString() {
        return (id != null ? id : "") + "," +
                (name != null ? name : "") + "," +
                age + "," +
                (contact != null ? contact : "") + "," +
                (email != null ? email : "") + "," +
                (address != null ? address : "") + "," +
                (gender != null ? gender : "") + "," +
                (specialization != null ? specialization : "") + "," +
                (qualification != null ? qualification : "") + "," +
                (availability != null ? availability : "") + "," +
                consultationFee;
    }

    @Override
    public String toString() {
        return super.toString() + ", Specialization: " + specialization + ", Availability: " + availability;
    }
}
