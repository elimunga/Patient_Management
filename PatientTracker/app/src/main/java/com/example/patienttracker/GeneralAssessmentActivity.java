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

public class GeneralAssessmentActivity extends AppCompatActivity {

    private EditText etGenPatientId, etGenVisitDate, etGenComments;
    private RadioGroup rgGenHealth, rgGenDrugs;
    private Button btnSubmitGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_assessment);

        etGenPatientId = findViewById(R.id.etGenPatientId);
        etGenVisitDate = findViewById(R.id.etGenVisitDate);
        etGenComments = findViewById(R.id.etGenComments);
        rgGenHealth = findViewById(R.id.rgGenHealth);
        rgGenDrugs = findViewById(R.id.rgGenDrugs);
        btnSubmitGen = findViewById(R.id.btnSubmitGen);
        // Auto-hyphenate the Visit Date
        etGenVisitDate.addTextChangedListener(new DateMaskWatcher(etGenVisitDate));

        // Auto-fill Patient ID from the previous screen
        String passedId = getIntent().getStringExtra("PATIENT_ID");
        if (passedId != null) {
            etGenPatientId.setText(passedId);
        }

        btnSubmitGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAssessment();
            }
        });
    }

    private void submitAssessment() {
        String patientId = etGenPatientId.getText().toString().trim();
        String visitDate = etGenVisitDate.getText().toString().trim();
        String comments = etGenComments.getText().toString().trim();

        // Get Health
        int healthId = rgGenHealth.getCheckedRadioButtonId();
        String health = healthId == R.id.rbHealthGood ? "Good" : (healthId == R.id.rbHealthPoor ? "Poor" : "");

        // Get Drugs boolean
        int drugsId = rgGenDrugs.getCheckedRadioButtonId();
        boolean usingDrugs = (drugsId == R.id.rbDrugsYes);

        if (patientId.isEmpty() || visitDate.isEmpty() || health.isEmpty() || drugsId == -1) {
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        GeneralAssessment assessment = new GeneralAssessment(patientId, visitDate, health, usingDrugs, comments);

        RetrofitClient.getApiService().addGeneralAssessment(assessment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GeneralAssessmentActivity.this, "Assessment Saved!", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(GeneralAssessmentActivity.this, PatientListingActivity.class);
                     startActivity(intent);
                } else {
                    Toast.makeText(GeneralAssessmentActivity.this, "Error saving assessment", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GeneralAssessmentActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}