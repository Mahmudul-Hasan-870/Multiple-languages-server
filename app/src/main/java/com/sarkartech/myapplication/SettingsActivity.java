package com.sarkartech.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup languageRadioGroup;
    private RadioButton banglaRadioButton;
    private RadioButton hindiRadioButton;
    private RadioButton arabicRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        banglaRadioButton = findViewById(R.id.banglaRadioButton);
        hindiRadioButton = findViewById(R.id.hindiRadioButton);
        arabicRadioButton = findViewById(R.id.arabicRadioButton);

        // Load the saved language preference
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedLanguage = preferences.getString("selectedLanguage", "Bangla");
        if (selectedLanguage.equals("Bangla")) {
            banglaRadioButton.setChecked(true);
        } else if (selectedLanguage.equals("Hindi")) {
            hindiRadioButton.setChecked(true);
        } else if (selectedLanguage.equals("Arabic")) {
            arabicRadioButton.setChecked(true);
        }

        // Listen for radio button changes and save the preference
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selected;
            if (banglaRadioButton.isChecked()) {
                selected = "Bangla";
            } else if (hindiRadioButton.isChecked()) {
                selected = "Hindi";
            } else if (arabicRadioButton.isChecked()) {
                selected = "Arabic";
            } else {
                // Handle the case when neither radio button is checked, possibly set a default language.
                selected = "DefaultLanguage"; // Replace with your default language.
            }

            // Save the selected language preference
            preferences.edit().putString("selectedLanguage", selected).apply();
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
        });
    }
}

