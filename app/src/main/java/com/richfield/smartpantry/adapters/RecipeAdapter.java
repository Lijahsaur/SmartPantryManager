package com.richfield.smartpantry.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantry.R;
import com.richfield.smartpantry.models.Recipe;

import java.util.List;

/**
 * Custom RecyclerView Adapter to display matched recipes and pass click events to RecipeDetailActivity.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> recipeList;
    private final OnRecipeClickListener listener;

    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    public void updateData(List<Recipe> newList) {
        this.recipeList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.tvRecipeName.setText(recipe.getName());

        // Format raw ingredients ("egg:2:pcs, butter:10:g") into readable text ("egg (2 pcs), butter (10 g)")
        String formattedIng = formatIngredientsPreview(recipe.getIngredientsRaw());
        holder.tvIngredientsPreview.setText("Needs: " + formattedIng);

        if (recipe.getMissingIngredientsCount() == 0) {
            holder.tvMatchBadge.setText("✓ 100% Strict Match — Ready to Cook!");
            holder.tvMatchBadge.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvMatchBadge.setText("Almost There — Missing 1 Ingredient");
            holder.tvMatchBadge.setTextColor(Color.parseColor("#E65100"));
        }

        holder.btnViewRecipe.setOnClickListener(v -> listener.onRecipeClick(recipe));
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    private String formatIngredientsPreview(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        String[] items = raw.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            String[] parts = items[i].trim().split(":");
            if (parts.length >= 3) {
                sb.append(parts[0]).append(" (").append(parts[1]).append(" ").append(parts[2]).append(")");
            } else if (parts.length == 2) {
                sb.append(parts[0]).append(" (").append(parts[1]).append(")");
            } else {
                sb.append(items[i].trim());
            }
            if (i < items.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName, tvMatchBadge, tvIngredientsPreview;
        Button btnViewRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvMatchBadge = itemView.findViewById(R.id.tvMatchBadge);
            tvIngredientsPreview = itemView.findViewById(R.id.tvIngredientsPreview);
            btnViewRecipe = itemView.findViewById(R.id.btnViewRecipe);
        }
    }
}