package com.richfield.smartpantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantry.R;
import com.richfield.smartpantry.models.PantryItem;

import java.util.List;

/**
 * Custom RecyclerView Adapter to bind PantryItem objects from SQLite to item_pantry.xml cards.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnPantryItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private List<PantryItem> pantryList;
    private final OnPantryItemActionListener listener;

    public PantryAdapter(List<PantryItem> pantryList, OnPantryItemActionListener listener) {
        this.pantryList = pantryList;
        this.listener = listener;
    }

    public void updateData(List<PantryItem> newList) {
        this.pantryList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryList.get(position);
        holder.tvName.setText(item.getName());

        // Format whole numbers cleanly (e.g. "2" instead of "2.0")
        String qtyFormatted = (item.getQuantity() == (long) item.getQuantity())
                ? String.format("%d", (long) item.getQuantity())
                : String.valueOf(item.getQuantity());

        holder.tvQuantityUnit.setText("Available: " + qtyFormatted + " " + item.getUnit());

        if (item.getExpiryDate() == null || item.getExpiryDate().trim().isEmpty()) {
            holder.tvExpiry.setText("Expiry: No date set");
        } else {
            holder.tvExpiry.setText("Expires: " + item.getExpiryDate());
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return pantryList == null ? 0 : pantryList.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantityUnit, tvExpiry;
        Button btnEdit, btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantityUnit = itemView.findViewById(R.id.tvQuantityUnit);
            tvExpiry = itemView.findViewById(R.id.tvExpiryDate);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}