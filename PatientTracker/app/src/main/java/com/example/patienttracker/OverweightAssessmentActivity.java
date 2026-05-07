package com.example.patienttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverweightAssessmentActivity extends AppCompatActivity {

    private EditText etOwPatientId, etOwVisitDate, etOwComments;
    private RadioGroup rgOwHealth, rgOwDiet;
    private Button btnSubmitOw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overweight_assessment);

        etOwPatientId = findViewById(R.id.etOwPatientId);
        etOwVisitDate = findViewById(R.id.etOwVisitDate);
        etOwComments = findViewById(R.id.etOwComments);
        rgOwHealth = findViewById(R.id.rgOwHealth);
        rgOwDiet = findViewById(R.id.rgOwDiet);
        btnSubmitOw = findViewById(R.id.btnSubmitOw);
        // Auto-hyphenate the Visit Date
        etOwVisitDate.addTextChangedListener(new DateMaskWatcher(etOwVisitDate));

        String passedId = getIntent().getStringExtra("PATIENT_ID");
        if (passedId != null) {
            etOwPatientId.setText(passedId);
        }

        btnSubmitOw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAssessment();
            }
        });
    }

    private void submitAssessment() {
        String patientId = etOwPatientId.getText().toString().trim();
        String visitDate = etOwVisitDate.getText().toString().trim();
        String comments = etOwComments.getText().toString().trim();

        int healthId = rgOwHealth.getCheckedRadioButtonId();
        String health = healthId == R.id.rbOwHealthGood ? "Good" : (healthId == R.id.rbOwHealthPoor ? "Poor" : "");

        int dietId = rgOwDiet.getCheckedRadioButtonId();
        boolean dietHistory = (dietId == R.id.rbDietYes);

        if (patientId.isEmpty() || visitDate.isEmpty() || health.isEmpty() || dietId == -1) {
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        OverweightAssessment assessment = new OverweightAssessment(patientId, visitDate, health, dietHistory, comments);

        RetrofitClient.getApiService().addOverweightAssessment(assessment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OverweightAssessmentActivity.this, "Assessment Saved!", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(OverweightAssessmentActivity.this, PatientListingActivity.class);
                     startActivity(intent);
                } else {
                    Toast.makeText(OverweightAssessmentActivity.this, "Error saving assessment", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OverweightAssessmentActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}