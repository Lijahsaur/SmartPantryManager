package com.richfield.smartpantry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.richfield.smartpantry.models.Recipe;

/**
 * Screen 4: Displays the full formatted ingredient list and preparation method for a selected recipe.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView tvName = findViewById(R.id.tvDetailRecipeName);
        TextView tvBadge = findViewById(R.id.tvDetailStatusBadge);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvSteps = findViewById(R.id.tvDetailSteps);
        Button btnBack = findViewById(R.id.btnBackFromDetail);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("EXTRA_RECIPE");
        if (recipe != null) {
            tvName.setText(recipe.getName());
            tvSteps.setText(recipe.getSteps());

            if (recipe.getMissingIngredientsCount() == 0) {
                tvBadge.setText("✓ 100% Strict Pantry Match — Ready to Cook!");
            } else {
                tvBadge.setText("Almost There — Missing 1 Ingredient");
            }

            // Format ingredients as bullet points
            String[] items = recipe.getIngredientsRaw().split(",");
            StringBuilder bulletList = new StringBuilder();
            for (String item : items) {
                String[] parts = item.trim().split(":");
                if (parts.length >= 3) {
                    bulletList.append("• ").append(capitalize(parts[0]))
                            .append(" — ").append(parts[1]).append(" ").append(parts[2]).append("\n");
                } else {
                    bulletList.append("• ").append(item.trim()).append("\n");
                }
            }
            tvIngredients.setText(bulletList.toString().trim());
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}