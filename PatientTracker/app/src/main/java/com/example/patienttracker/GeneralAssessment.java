package com.example.patienttracker;

public class GeneralAssessment {
    private String patient_id;
    private String visit_date;
    private String general_health;
    private boolean using_drugs;
    private String comments;

    public GeneralAssessment(String patient_id, String visit_date, String general_health, boolean using_drugs, String comments) {
        this.patient_id = patient_id;
        this.visit_date = visit_date;
        this.general_health = general_health;
        this.using_drugs = using_drugs;
        this.comments = comments;
    }
}