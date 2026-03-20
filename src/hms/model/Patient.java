package hms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient extends Person {
    private String bloodGroup;
    private String allergies;
    private String disease;
    private Date registrationDate;
    private List<MedicalRecord> medicalRecords;

    // Constructor
    public Patient(String id, String name, int age, String contact, String disease) {
        super(id, name, age, contact);
        this.disease = disease;
        this.registrationDate = new Date();
        this.medicalRecords = new ArrayList<>();
    }

    // Full constructor
    public Patient(String id, String name, int age, String contact, String email, String address,
                   String gender, String bloodGroup, String allergies, String disease) {
        super(id, name, age, contact, email, address, gender);
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
        this.disease = disease;
        this.registrationDate = new Date();
        this.medicalRecords = new ArrayList<>();
    }

    // Getters and setters
    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return new ArrayList<>(medicalRecords); // Return a copy to maintain encapsulation
    }

    public void addMedicalRecord(MedicalRecord record) {
        if (record != null) {
            this.medicalRecords.add(record);
        }
    }

    public String toFileString() {
        return (id != null ? id : "") + "," +
                (name != null ? name : "") + "," +
                age + "," +
                (contact != null ? contact : "") + "," +
                (email != null ? email : "") + "," +
                (address != null ? address : "") + "," +
                (gender != null ? gender : "") + "," +
                (bloodGroup != null ? bloodGroup : "") + "," +
                (allergies != null ? allergies : "") + "," +
                (disease != null ? disease : "");
    }

    @Override
    public String toString() {
        return super.toString() + ", Disease: " + disease + ", Blood Group: " + bloodGroup;
    }
}
