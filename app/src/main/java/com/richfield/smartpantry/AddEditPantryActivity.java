package com.richfield.smartpantry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.richfield.smartpantry.database.DatabaseHelper;
import com.richfield.smartpantry.models.PantryItem;

/**
 * Screen 2: Form to Create or Update a PantryItem with strict input validation.
 */
public class AddEditPantryActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private DatabaseHelper dbHelper;
    private PantryItem existingItem = null;
    private final String[] units = {"pcs", "g", "kg", "ml", "l", "tsp", "tbsp"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pantry);

        dbHelper = new DatabaseHelper(this);

        TextView tvFormTitle = findViewById(R.id.tvFormTitle);
        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button btnSave = findViewById(R.id.btnSaveIngredient);
        Button btnCancel = findViewById(R.id.btnCancel);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnit.setAdapter(unitAdapter);

        // Check if we are editing an existing item passed via Intent
        if (getIntent().hasExtra("EXTRA_PANTRY_ITEM")) {
            existingItem = (PantryItem) getIntent().getSerializableExtra("EXTRA_PANTRY_ITEM");
            if (existingItem != null) {
                tvFormTitle.setText("Edit Pantry Ingredient");
                btnSave.setText("Update Ingredient");
                etName.setText(existingItem.getName());
                etQuantity.setText(String.valueOf(existingItem.getQuantity()));
                etExpiry.setText(existingItem.getExpiryDate());

                for (int i = 0; i < units.length; i++) {
                    if (units[i].equalsIgnoreCase(existingItem.getUnit())) {
                        spinnerUnit.setSelection(i);
                        break;
                    }
                }
            }
        }

        btnSave.setOnClickListener(v -> saveOrUpdateIngredient());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveOrUpdateIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText().toString().trim();

        // Input Validation (Requirement 3.1)
        if (name.isEmpty()) {
            etName.setError("Ingredient name is required");
            etName.requestFocus();
            return;
        }

        if (qtyStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than zero");
            etQuantity.requestFocus();
            return;
        }

        if (existingItem == null) {
            // CREATE new item in SQLite
            PantryItem newItem = new PantryItem(name, quantity, unit, expiry);
            dbHelper.addPantryItem(newItem);
            Toast.makeText(this, "Ingredient added to pantry!", Toast.LENGTH_SHORT).show();
        } else {
            // UPDATE existing item in SQLite
            existingItem.setName(name);
            existingItem.setQuantity(quantity);
            existingItem.setUnit(unit);
            existingItem.setExpiryDate(expiry);
            dbHelper.updatePantryItem(existingItem);
            Toast.makeText(this, "Ingredient updated!", Toast.LENGTH_SHORT).show();
        }

        finish(); // Return to MainActivity
    }
}