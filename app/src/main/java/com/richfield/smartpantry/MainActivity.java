package com.richfield.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantry.adapters.PantryAdapter;
import com.richfield.smartpantry.database.DatabaseHelper;
import com.richfield.smartpantry.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen 1 (Home): Displays all current pantry ingredients in a RecyclerView
 * and provides navigation to Add/Edit, Suggested Recipes, and Settings screens.
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvPantryList;
    private PantryAdapter pantryAdapter;
    private TextView tvEmptyPantry, tvPantryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        tvPantryCount = findViewById(R.id.tvPantryCount);
        rvPantryList = findViewById(R.id.rvPantryList);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnNavSuggested = findViewById(R.id.btnNavSuggestedRecipes);
        Button btnNavSettings = findViewById(R.id.btnNavSettings);

        rvPantryList.setLayoutManager(new LinearLayoutManager(this));
        pantryAdapter = new PantryAdapter(new ArrayList<>(), new PantryAdapter.OnPantryItemActionListener() {
            @Override
            public void onEdit(PantryItem item) {
                Intent intent = new Intent(MainActivity.this, AddEditPantryActivity.class);
                intent.putExtra("EXTRA_PANTRY_ITEM", item);
                startActivity(intent);
            }

            @Override
            public void onDelete(PantryItem item) {
                confirmDelete(item);
            }
        });
        rvPantryList.setAdapter(pantryAdapter);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditPantryActivity.class);
            startActivity(intent);
        });

        btnNavSuggested.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        btnNavSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload pantry items every time user returns to MainActivity (Lifecycle awareness)
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();
        pantryAdapter.updateData(items);
        tvPantryCount.setText(items.size() + " ingredient(s) currently in your pantry");

        if (items.isEmpty()) {
            tvEmptyPantry.setVisibility(View.VISIBLE);
            rvPantryList.setVisibility(View.GONE);
        } else {
            tvEmptyPantry.setVisibility(View.GONE);
            rvPantryList.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Ingredient")
                .setMessage("Are you sure you want to delete '" + item.getName() + "' from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deletePantryItem(item.getId());
                    Toast.makeText(this, item.getName() + " deleted", Toast.LENGTH_SHORT).show();
                    loadPantryItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}