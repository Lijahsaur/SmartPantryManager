package com.richfield.smartpantry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantry.adapters.RecipeAdapter;
import com.richfield.smartpantry.database.DatabaseHelper;
import com.richfield.smartpantry.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen 3: Runs the strict-matching algorithm against the user's current SQLite pantry
 * and displays only recipes where 100% of required ingredients are available in sufficient quantity.
 */
public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView rvSuggestedRecipes;
    private RecipeAdapter recipeAdapter;
    private TextView tvZeroRecipesFeedback, tvMatchSummary;
    private Button btnStrictFilter, btnAlmostThereFilter;
    private boolean showingStrictMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);

        tvMatchSummary = findViewById(R.id.tvMatchSummary);
        tvZeroRecipesFeedback = findViewById(R.id.tvZeroRecipesFeedback);
        rvSuggestedRecipes = findViewById(R.id.rvSuggestedRecipes);
        btnStrictFilter = findViewById(R.id.btnStrictFilter);
        btnAlmostThereFilter = findViewById(R.id.btnAlmostThereFilter);
        Button btnBack = findViewById(R.id.btnBackToPantry);

        rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(new ArrayList<>(), recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("EXTRA_RECIPE", recipe);
            startActivity(intent);
        });
        rvSuggestedRecipes.setAdapter(recipeAdapter);

        btnStrictFilter.setOnClickListener(v -> {
            showingStrictMode = true;
            updateFilterButtonsUI();
            loadRecipes();
        });

        btnAlmostThereFilter.setOnClickListener(v -> {
            showingStrictMode = false;
            updateFilterButtonsUI();
            loadRecipes();
        });

        btnBack.setOnClickListener(v -> finish());

        loadRecipes();
    }

    private void updateFilterButtonsUI() {
        if (showingStrictMode) {
            btnStrictFilter.setBackgroundColor(Color.WHITE);
            btnStrictFilter.setTextColor(Color.parseColor("#2E7D32"));
            btnAlmostThereFilter.setBackgroundColor(Color.parseColor("#1B5E20"));
            btnAlmostThereFilter.setTextColor(Color.WHITE);
        } else {
            btnAlmostThereFilter.setBackgroundColor(Color.WHITE);
            btnAlmostThereFilter.setTextColor(Color.parseColor("#2E7D32"));
            btnStrictFilter.setBackgroundColor(Color.parseColor("#1B5E20"));
            btnStrictFilter.setTextColor(Color.WHITE);
        }
    }

    private void loadRecipes() {
        List<Recipe> matchedRecipes;
        if (showingStrictMode) {
            matchedRecipes = dbHelper.getStrictMatchingRecipes();
            tvMatchSummary.setText(matchedRecipes.size() + " recipe(s) match 100% of your pantry right now");
            tvZeroRecipesFeedback.setText(
                    "No recipes match your pantry yet - add more ingredients!\n\n" +
                            "(Tip: Add 'Egg' [2 pcs], 'Butter' [10 g], and 'Salt' [1 tsp] to unlock Classic Scrambled Eggs!)");
        } else {
            matchedRecipes = dbHelper.getAlmostThereRecipes();
            tvMatchSummary.setText(matchedRecipes.size() + " recipe(s) missing only 1 ingredient");
            tvZeroRecipesFeedback.setText("No 'Almost There' recipes right now.");
        }

        recipeAdapter.updateData(matchedRecipes);

        if (matchedRecipes.isEmpty()) {
            tvZeroRecipesFeedback.setVisibility(View.VISIBLE);
            rvSuggestedRecipes.setVisibility(View.GONE);
        } else {
            tvZeroRecipesFeedback.setVisibility(View.GONE);
            rvSuggestedRecipes.setVisibility(View.VISIBLE);
        }
    }
}