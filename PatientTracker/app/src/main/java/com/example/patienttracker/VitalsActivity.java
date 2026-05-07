package com.example.patienttracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VitalsActivity extends AppCompatActivity {

    private EditText etVitalPatientId, etVisitDate, etHeight, etWeight;
    private TextView tvBmiDisplay;
    private Button btnSubmitVitals;
    private double currentBmi = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

        etVitalPatientId = findViewById(R.id.etVitalPatientId);
        etVisitDate = findViewById(R.id.etVisitDate);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        tvBmiDisplay = findViewById(R.id.tvBmiDisplay);
        btnSubmitVitals = findViewById(R.id.btnSubmitVitals);
        // Auto-hyphenate the Visit Date
        etVisitDate.addTextChangedListener(new DateMaskWatcher(etVisitDate));

        // Auto-calculate BMI as user types
        TextWatcher bmiWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                calculateBmiLocally();
            }
        };
        etHeight.addTextChangedListener(bmiWatcher);
        etWeight.addTextChangedListener(bmiWatcher);

        btnSubmitVitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVitals();
            }
        });
    }

    private void calculateBmiLocally() {
        String heightStr = etHeight.getText().toString();
        String weightStr = etWeight.getText().toString();

        if (!heightStr.isEmpty() && !weightStr.isEmpty()) {
            try {
                double heightCm = Double.parseDouble(heightStr);
                double weightKg = Double.parseDouble(weightStr);
                double heightM = heightCm / 100.0;
                currentBmi = weightKg / (heightM * heightM);
                tvBmiDisplay.setText(String.format("BMI: %.2f", currentBmi));
            } catch (NumberFormatException e) {
                tvBmiDisplay.setText("BMI: Error in numbers");
            }
        }
    }

    private void submitVitals() {
        String patientId = etVitalPatientId.getText().toString().trim();
        String visitDate = etVisitDate.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (patientId.isEmpty() || visitDate.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double heightCm = Double.parseDouble(heightStr);
        double weightKg = Double.parseDouble(weightStr);

        Vital newVital = new Vital(patientId, visitDate, heightCm, weightKg);

        RetrofitClient.getApiService().addVitals(newVital).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(VitalsActivity.this, "Vitals Saved!", Toast.LENGTH_SHORT).show();

                    // Routing logic based on BMI [cite: 45-46]
                    if (currentBmi <= 25) {
                        Intent intent = new Intent(VitalsActivity.this, GeneralAssessmentActivity.class);
                        // Pass the patient ID to the next screen so they don't have to retype it
                        intent.putExtra("PATIENT_ID", patientId);
                        startActivity(intent);
                        Toast.makeText(VitalsActivity.this, "Routing to General Assessment...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(VitalsActivity.this, OverweightAssessmentActivity.class);
                        intent.putExtra("PATIENT_ID", patientId);
                        startActivity(intent);
                        Toast.makeText(VitalsActivity.this, "Routing to Overweight Assessment...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VitalsActivity.this, "Error saving vitals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VitalsActivity.this, "Network Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}