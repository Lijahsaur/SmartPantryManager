package com.richfield.smartpantry.models;

import java.io.Serializable;

/**
 * Represents a recipe pre-loaded in the database.
 * Required ingredients are stored in a structured format: "ingredientName:quantity:unit, ..."
 */
public class Recipe implements Serializable {
    private int id;
    private String name;
    private String ingredientsRaw; // e.g., "egg:2:pcs, butter:10:g, salt:1:tsp"
    private String steps;
    private int missingIngredientsCount; // Used if you want bonus marks for the "Almost There" stretch goal

    public Recipe(int id, String name, String ingredientsRaw, String steps) {
        this.id = id;
        this.name = name;
        this.ingredientsRaw = ingredientsRaw;
        this.steps = steps;
        this.missingIngredientsCount = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIngredientsRaw() { return ingredientsRaw; }
    public void setIngredientsRaw(String ingredientsRaw) { this.ingredientsRaw = ingredientsRaw; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public int getMissingIngredientsCount() { return missingIngredientsCount; }
    public void setMissingIngredientsCount(int missingIngredientsCount) {
        this.missingIngredientsCount = missingIngredientsCount;
    }
}