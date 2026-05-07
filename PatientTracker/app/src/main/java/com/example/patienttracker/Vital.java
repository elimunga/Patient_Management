package com.example.patienttracker;

public class Vital {
    private String patient_id;
    private String visit_date;
    private double height_cm;
    private double weight_kg;

    public Vital(String patient_id, String visit_date, double height_cm, double weight_kg) {
        this.patient_id = patient_id;
        this.visit_date = visit_date;
        this.height_cm = height_cm;
        this.weight_kg = weight_kg;
    }
}