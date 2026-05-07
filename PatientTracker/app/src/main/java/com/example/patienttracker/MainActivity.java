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

public class MainActivity extends AppCompatActivity {

    private EditText etPatientId, etFirstName, etLastName, etDob, etRegDate;
    private RadioGroup rgGender;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Connects to the XML we just wrote

        // 1. Link Java variables to XML elements
        etPatientId = findViewById(R.id.etPatientId);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDob = findViewById(R.id.etDob);
        etRegDate = findViewById(R.id.etRegDate);
        rgGender = findViewById(R.id.rgGender);
        btnSubmit = findViewById(R.id.btnSubmit);
        // Attach the auto-hyphen formatter to the date fields
        etDob.addTextChangedListener(new DateMaskWatcher(etDob));
        etRegDate.addTextChangedListener(new DateMaskWatcher(etRegDate));

        // 2. Listen for the Submit Button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPatient();
            }
        });
    }

    private void registerPatient() {
        // Grab the text from the inputs
        String id = etPatientId.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String regDate = etRegDate.getText().toString().trim();

        // Find which gender is checked
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = "";
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            gender = selectedRadioButton.getText().toString();
        }

        // Basic validation
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || regDate.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Patient object
        Patient newPatient = new Patient(id, firstName, lastName, dob, gender, regDate);

        // Send to Python Backend
        RetrofitClient.getApiService().registerPatient(newPatient).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Patient Registered!", Toast.LENGTH_SHORT).show();
                    // UPON SUCCESS: Load the Vitals Page (We will create this next)
                     Intent intent = new Intent(MainActivity.this, VitalsActivity.class);
                     startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Error: Patient ID might already exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}