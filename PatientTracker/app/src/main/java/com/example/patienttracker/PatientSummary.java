package com.example.patienttracker;

public class PatientSummary {
    private String patient_name;
    private int age;
    private String bmi_status;
    private String last_assessment_date;

    public String getPatientName() { return patient_name; }
    public int getAge() { return age; }
    public String getBmiStatus() { return bmi_status; }
    public String getLastAssessmentDate() { return last_assessment_date; }
}