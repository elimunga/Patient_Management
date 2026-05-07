package com.example.patienttracker;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/patients/register")
    Call<Void> registerPatient(@Body Patient patient);

    @POST("api/vitals")
    Call<Void> addVitals(@Body Vital vital);

    @POST("api/assessments/general")
    Call<Void> addGeneralAssessment(@Body GeneralAssessment assessment);

    @POST("api/assessments/overweight")
    Call<Void> addOverweightAssessment(@Body OverweightAssessment assessment);

    @GET("api/patients")
    Call<List<PatientSummary>> getPatients();

}
