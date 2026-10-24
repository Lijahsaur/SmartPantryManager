package com.richfield.smartpantry.models;

import java.io.Serializable;

/**
 * Represents a single ingredient stored in the user's home pantry.
 * Implements Serializable so it can be easily passed between Activities via Intents.
 */
public class PantryItem implements Serializable {
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    // Constructor for creating a new item before it has a database ID
    public PantryItem(String name, double quantity, String unit, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Constructor for retrieving an existing item from SQLite
    public PantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}