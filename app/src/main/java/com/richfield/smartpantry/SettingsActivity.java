package com.richfield.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.richfield.smartpantry.database.DatabaseHelper;
import com.richfield.smartpantry.models.PantryItem;

/**
 * Screen 5: Persists user preferences using SharedPreferences and includes a quick-seed
 * helper button for live testing.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantryPrefs";
    private Switch switchExpiry, switchMetric;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        switchExpiry = findViewById(R.id.switchExpiryAlerts);
        switchMetric = findViewById(R.id.switchMetricUnits);
        Button btnLoadDemo = findViewById(R.id.btnLoadDemoPantry);
        Button btnSave = findViewById(R.id.btnSaveSettings);

        switchExpiry.setChecked(prefs.getBoolean("EXPIRY_ALERTS", true));
        switchMetric.setChecked(prefs.getBoolean("METRIC_UNITS", true));

        btnLoadDemo.setOnClickListener(v -> {
            dbHelper.addPantryItem(new PantryItem("Eggs", 6, "pcs", "2026-11-20"));
            dbHelper.addPantryItem(new PantryItem("Butter", 100, "g", "2026-12-01"));
            dbHelper.addPantryItem(new PantryItem("Salt", 50, "tsp", "2027-06-01"));
            dbHelper.addPantryItem(new PantryItem("Bread", 4, "pcs", "2026-11-05"));
            dbHelper.addPantryItem(new PantryItem("Cheese", 100, "g", "2026-11-25"));
            Toast.makeText(this, "5 sample ingredients added to pantry!", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("EXPIRY_ALERTS", switchExpiry.isChecked());
            editor.putBoolean("METRIC_UNITS", switchMetric.isChecked());
            editor.apply();
            Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}