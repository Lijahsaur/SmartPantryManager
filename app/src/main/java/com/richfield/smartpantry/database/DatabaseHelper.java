package com.richfield.smartpantry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.richfield.smartpantry.models.PantryItem;
import com.richfield.smartpantry.models.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages local SQLite database creation, pre-seeded recipes, Pantry CRUD operations,
 * and the core strict-matching recipe algorithm.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry Table & Columns
    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipes Table & Columns
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "recipe_name";
    public static final String COL_RECIPE_INGREDIENTS = "ingredients"; // Format: "item:qty:unit, item:qty:unit"
    public static final String COL_RECIPE_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Create Pantry Table
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QTY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT NOT NULL, " +
                COL_PANTRY_EXPIRY + " TEXT);";
        db.execSQL(createPantryTable);

        // 2. Create Recipes Table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT NOT NULL);";
        db.execSQL(createRecipesTable);

        // 3. Pre-load 16 recipes on first run (Requirement 2.2: 15-20 recipes)
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // =========================================================================
    // PRE-SEEDED RECIPES (16 Recipes)
    // Format for ingredients: "name:quantity:unit, name:quantity:unit"
    // =========================================================================
    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Classic Scrambled Eggs",
                "egg:2:pcs, butter:10:g, salt:1:tsp",
                "1. Crack 2 eggs into a bowl and whisk well.\n2. Melt butter in a non-stick pan over low heat.\n3. Pour in eggs and stir gently until soft curds form.\n4. Season with salt and serve warm.");

        insertRecipe(db, "Garlic Butter Pasta",
                "pasta:200:g, garlic:2:pcs, butter:30:g, salt:1:tsp",
                "1. Boil pasta in salted water until al dente, then drain.\n2. Mince garlic cloves finely.\n3. Melt butter in a pan and sauté garlic for 1 minute.\n4. Toss pasta in the garlic butter and serve.");

        insertRecipe(db, "Fresh Tomato Bruschetta",
                "bread:2:pcs, tomato:2:pcs, garlic:1:pcs, olive oil:15:ml, salt:1:tsp",
                "1. Toast the bread slices until golden and crisp.\n2. Rub raw garlic lightly over the warm toast.\n3. Dice tomatoes, mix with olive oil and salt, and spoon onto the toast.");

        insertRecipe(db, "Cheese Omelette",
                "egg:3:pcs, cheese:50:g, butter:10:g, salt:1:tsp",
                "1. Beat eggs with salt in a bowl.\n2. Melt butter in a frying pan over medium heat.\n3. Pour in eggs, cook until edges set, sprinkle grated cheese on top, fold, and serve.");

        insertRecipe(db, "Simple Tomato Pasta",
                "pasta:200:g, tomato:3:pcs, garlic:2:pcs, olive oil:20:ml, salt:1:tsp",
                "1. Cook pasta until tender and drain.\n2. Dice tomatoes and mince garlic.\n3. Simmer garlic and tomatoes in olive oil for 10 minutes with salt.\n4. Toss pasta in the fresh sauce.");

        insertRecipe(db, "Grilled Cheese Sandwich",
                "bread:2:pcs, cheese:60:g, butter:15:g",
                "1. Butter the outside of both bread slices.\n2. Place sliced cheese between the unbuttered sides.\n3. Grill in a skillet over medium heat for 3 minutes per side until golden and melted.");

        insertRecipe(db, "Egg Fried Rice",
                "rice:200:g, egg:2:pcs, onion:1:pcs, oil:15:ml, soy sauce:15:ml",
                "1. Heat oil in a wok or pan and sauté chopped onion.\n2. Push onion aside, crack in eggs, and scramble.\n3. Add cooked rice and soy sauce, tossing on high heat for 3 minutes.");

        insertRecipe(db, "Mashed Potatoes",
                "potato:4:pcs, butter:30:g, milk:50:ml, salt:1:tsp",
                "1. Peel and cube potatoes, then boil in salted water for 15 minutes until soft.\n2. Drain water completely.\n3. Mash potatoes thoroughly with butter, warm milk, and salt.");

        insertRecipe(db, "Crispy Home Fries",
                "potato:3:pcs, onion:1:pcs, oil:20:ml, salt:1:tsp",
                "1. Dice potatoes into small cubes and chop the onion.\n2. Heat oil in a wide skillet over medium-high heat.\n3. Fry potatoes and onion for 15 minutes, turning occasionally until crispy. Season with salt.");

        insertRecipe(db, "French Toast",
                "bread:2:pcs, egg:2:pcs, milk:50:ml, butter:15:g, sugar:10:g",
                "1. Whisk eggs, milk, and sugar in a shallow bowl.\n2. Dip bread slices into the mixture to coat both sides.\n3. Fry in melted butter for 2-3 minutes per side until golden brown.");

        insertRecipe(db, "Fluffy Pancakes",
                "flour:150:g, milk:200:ml, egg:1:pcs, sugar:20:g, butter:15:g",
                "1. Whisk flour, sugar, egg, and milk into a smooth batter.\n2. Melt a little butter in a pan over medium heat.\n3. Pour ladlefuls of batter and flip when bubbles appear on the surface.");

        insertRecipe(db, "Honey Glazed Carrots",
                "carrot:4:pcs, butter:20:g, honey:15:ml, salt:1:tsp",
                "1. Peel and slice carrots into coins, then boil for 8 minutes until tender.\n2. Drain and return to the pan with butter, honey, and salt.\n3. Toss over medium heat for 2 minutes until glazed.");

        insertRecipe(db, "Lemon Garlic Chicken",
                "chicken:300:g, lemon:1:pcs, garlic:2:pcs, olive oil:15:ml, salt:1:tsp",
                "1. Season chicken breast with salt, minced garlic, and juice of 1 lemon.\n2. Heat olive oil in a skillet over medium-high heat.\n3. Cook chicken for 6-7 minutes per side until cooked through.");

        insertRecipe(db, "Tuna Mayo Sandwich",
                "bread:2:pcs, tuna:100:g, mayonnaise:30:g, onion:1:pcs",
                "1. Drain canned tuna and mix in a bowl with mayonnaise and finely diced onion.\n2. Spread evenly between two slices of bread and cut in half.");

        insertRecipe(db, "Cinnamon Oatmeal Bowl",
                "oats:80:g, milk:200:ml, honey:15:ml, cinnamon:1:tsp",
                "1. Combine oats and milk in a small pot over medium heat.\n2. Simmer for 5 minutes while stirring until creamy.\n3. Pour into a bowl and top with honey and cinnamon.");

        insertRecipe(db, "Banana Milkshake",
                "banana:2:pcs, milk:250:ml, honey:15:ml",
                "1. Peel ripe bananas and break into chunks.\n2. Place banana, cold milk, and honey into a blender.\n3. Blend on high for 30 seconds until smooth and frothy.");
    }

    private void insertRecipe(SQLiteDatabase db, String name, String ingredients, String steps) {
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name);
        values.put(COL_RECIPE_INGREDIENTS, ingredients);
        values.put(COL_RECIPE_STEPS, steps);
        db.insert(TABLE_RECIPES, null, values);
    }

    // =========================================================================
    // PANTRY CRUD OPERATIONS (Section 3.2)
    // =========================================================================

    // CREATE
    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName().trim());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit().trim());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate().trim());
        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    // READ ALL PANTRY ITEMS
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COL_PANTRY_NAME + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PANTRY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_NAME));
                double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PANTRY_QTY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY));
                list.add(new PantryItem(id, name, qty, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // UPDATE
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName().trim());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit().trim());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate().trim());
        int rows = db.update(TABLE_PANTRY, values, COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    // DELETE
    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COL_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // READ ALL RECIPES
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_INGREDIENTS));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_STEPS));
                recipes.add(new Recipe(id, name, ingredients, steps));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipes;
    }

    // =========================================================================
    // CORE BUSINESS LOGIC: THE STRICT-MATCHING RULE (Section 2.3)
    // =========================================================================

    /**
     * Normalizes ingredient names so "Tomatoes", "tomato ", and "Potatoes" match "tomato" and "potato".
     */
    public static String normalizeName(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toLowerCase();
        if (s.endsWith("ies") && s.length() > 4) {
            return s.substring(0, s.length() - 3) + "y"; // e.g., berries -> berry
        } else if (s.endsWith("oes") && s.length() > 4) {
            return s.substring(0, s.length() - 2);       // e.g., tomatoes -> tomato, potatoes -> potato
        } else if (s.endsWith("s") && !s.endsWith("ss") && !s.equals("oats")) {
            return s.substring(0, s.length() - 1);       // e.g., eggs -> egg, carrots -> carrot
        }
        return s;
    }

    /**
     * Converts units to a base value (kg -> g, l -> ml) so 1 kg of flour satisfies 150 g of flour.
     */
    private double toBaseQuantity(double qty, String unit) {
        if (unit == null) return qty;
        String u = unit.trim().toLowerCase();
        if (u.equals("kg")) return qty * 1000.0;
        if (u.equals("l")) return qty * 1000.0;
        return qty;
    }

    /**
     * Calculates how many ingredients a user is missing for a given recipe.
     * Returns 0 ONLY if EVERY required ingredient is present in at least the required quantity.
     */
    public int countMissingIngredients(Recipe recipe, List<PantryItem> userPantry) {
        String[] requiredItems = recipe.getIngredientsRaw().split(",");
        int missingCount = 0;

        for (String req : requiredItems) {
            String[] parts = req.trim().split(":");
            if (parts.length < 2) continue;

            String reqName = normalizeName(parts[0]);
            double reqQty = Double.parseDouble(parts[1].trim());
            String reqUnit = parts.length >= 3 ? parts[2].trim() : "pcs";
            double reqBaseQty = toBaseQuantity(reqQty, reqUnit);

            boolean hasEnough = false;
            for (PantryItem pItem : userPantry) {
                String pantryName = normalizeName(pItem.getName());
                if (pantryName.equals(reqName) || pantryName.contains(reqName) || reqName.contains(pantryName)) {
                    double userBaseQty = toBaseQuantity(pItem.getQuantity(), pItem.getUnit());
                    if (userBaseQty >= reqBaseQty) {
                        hasEnough = true;
                        break;
                    }
                }
            }

            if (!hasEnough) {
                missingCount++;
            }
        }
        return missingCount;
    }

    /**
     * Returns ONLY recipes where missingCount == 0 (100% strict match).
     */
    public List<Recipe> getStrictMatchingRecipes() {
        List<PantryItem> pantry = getAllPantryItems();
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> strictMatches = new ArrayList<>();

        for (Recipe r : allRecipes) {
            int missing = countMissingIngredients(r, pantry);
            if (missing == 0) {
                r.setMissingIngredientsCount(0);
                strictMatches.add(r);
            }
        }
        return strictMatches;
    }

    /**
     * Bonus Stretch Feature (Section 2.3): Returns recipes missing ONLY 1 ingredient.
     */
    public List<Recipe> getAlmostThereRecipes() {
        List<PantryItem> pantry = getAllPantryItems();
        List<Recipe> allRecipes = getAllRecipes();
        List<Recipe> almostMatches = new ArrayList<>();

        for (Recipe r : allRecipes) {
            int missing = countMissingIngredients(r, pantry);
            if (missing == 1) {
                r.setMissingIngredientsCount(1);
                almostMatches.add(r);
            }
        }
        return almostMatches;
    }
}