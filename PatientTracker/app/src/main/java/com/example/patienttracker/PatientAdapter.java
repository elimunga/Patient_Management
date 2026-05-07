package com.example.patienttracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<PatientSummary> patientList;

    public PatientAdapter(List<PatientSummary> patientList) {
        this.patientList = patientList;
    }

    public void updateData(List<PatientSummary> newPatientList) {
        this.patientList = newPatientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_row, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        PatientSummary patient = patientList.get(position);
        holder.tvName.setText(patient.getPatientName());
        holder.tvAge.setText(String.valueOf(patient.getAge()));
        holder.tvBmi.setText(patient.getBmiStatus());

        // Handle null dates (if they haven't done an assessment yet)
        String date = patient.getLastAssessmentDate();
        holder.tvDate.setText(date != null ? date : "N/A");
    }

    @Override
    public int getItemCount() {
        return patientList == null ? 0 : patientList.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvBmi, tvDate;
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRowName);
            tvAge = itemView.findViewById(R.id.tvRowAge);
            tvBmi = itemView.findViewById(R.id.tvRowBmi);
            tvDate = itemView.findViewById(R.id.tvRowDate);
        }
    }
}