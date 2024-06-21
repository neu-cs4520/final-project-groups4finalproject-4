package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Questionnaire extends AppCompatActivity {

    RadioGroup dietRadioGroup;
    RadioButton selectedDietButton;
    Button saveButton;
    private static final String TAG = "Questionnaire";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questionnaire);

        dietRadioGroup = findViewById(R.id.diet_radio_group);

        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedDietId = dietRadioGroup.getCheckedRadioButtonId();

                selectedDietButton = findViewById(selectedDietId);

                if (selectedDietButton != null) {
                    String dietPreference = selectedDietButton.getText().toString();

                    savePreferences(dietPreference);
                    Toast.makeText(Questionnaire.this, "Preferences Saved", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(Questionnaire.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePreferences(String dietPreference) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dietPref", dietPreference);
        editor.apply();
    }
}