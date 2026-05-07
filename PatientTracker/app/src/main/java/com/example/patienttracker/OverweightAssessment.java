package com.example.patienttracker;

public class OverweightAssessment {
    private String patient_id;
    private String visit_date;
    private String general_health;
    private boolean diet_history;
    private String comments;

    public OverweightAssessment(String patient_id, String visit_date, String general_health, boolean diet_history, String comments) {
        this.patient_id = patient_id;
        this.visit_date = visit_date;
        this.general_health = general_health;
        this.diet_history = diet_history;
        this.comments = comments;
    }
}