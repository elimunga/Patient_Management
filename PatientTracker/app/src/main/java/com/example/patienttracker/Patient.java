package com.example.patienttracker;
public class Patient {
    private String patient_id;
    private String first_name;
    private String last_name;
    private String dob;
    private String gender;
    private String registration_date;

    public Patient(String patient_id, String first_name, String last_name, String dob, String gender, String registration_date) {
        this.patient_id = patient_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.gender = gender;
        this.registration_date = registration_date;
    }

    // You can generate getters and setters by right-clicking inside
    // Android Studio -> Generate -> Getter and Setter
}