package com.example.patienttracker;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateMaskWatcher implements TextWatcher {
    private String current = "";
    private final String ddmmyyyy = "YYYYMMDD";
    private final EditText editText;

    public DateMaskWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (!s.toString().equals(current)) {
            // Strip out everything except numbers
            String clean = s.toString().replaceAll("[^\\d]", "");

            // Inject the hyphens for YYYY-MM-DD format
            if (clean.length() > 8) {
                clean = clean.substring(0, 8);
            }
            if (clean.length() > 6) {
                clean = clean.substring(0, 4) + "-" + clean.substring(4, 6) + "-" + clean.substring(6);
            } else if (clean.length() > 4) {
                clean = clean.substring(0, 4) + "-" + clean.substring(4);
            }

            current = clean;
            editText.setText(current);
            // Keep the cursor at the end of the text
            editText.setSelection(current.length());
        }
    }
}