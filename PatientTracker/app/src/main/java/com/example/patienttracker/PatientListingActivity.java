package com.example.patienttracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientListingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private EditText etFilterDate;
    private Button btnFilter;
    private List<PatientSummary> allPatients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_listing);

        etFilterDate = findViewById(R.id.etFilterDate);
        btnFilter = findViewById(R.id.btnFilter);
        recyclerView = findViewById(R.id.recyclerViewPatients);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(allPatients);
        recyclerView.setAdapter(adapter);
        // Auto-hyphenate the Filter Date
        etFilterDate.addTextChangedListener(new DateMaskWatcher(etFilterDate));

        fetchPatients();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterByDate(etFilterDate.getText().toString().trim());
            }
        });
    }

    private void fetchPatients() {
        RetrofitClient.getApiService().getPatients().enqueue(new Callback<List<PatientSummary>>() {
            @Override
            public void onResponse(Call<List<PatientSummary>> call, Response<List<PatientSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allPatients = response.body();
                    adapter.updateData(allPatients);
                } else {
                    Toast.makeText(PatientListingActivity.this, "Failed to load patients", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<PatientSummary>> call, Throwable t) {
                Toast.makeText(PatientListingActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByDate(String dateQuery) {
        if (dateQuery.isEmpty()) {
            adapter.updateData(allPatients); // Show all if filter is empty
            return;
        }

        List<PatientSummary> filteredList = new ArrayList<>();
        for (PatientSummary p : allPatients) {
            if (p.getLastAssessmentDate() != null && p.getLastAssessmentDate().equals(dateQuery)) {
                filteredList.add(p);
            }
        }
        adapter.updateData(filteredList);
    }
}