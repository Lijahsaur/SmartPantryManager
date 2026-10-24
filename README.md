# Smart Pantry Manager

## 1. App Description
**Smart Pantry Manager** is a native Java Android application built to help households reduce food waste. Users track the exact leftover ingredients they currently have at home (name, quantity, unit, and optional expiry date), and the application executes a **strict-matching algorithm** against a pre-seeded database of 16 recipes—suggesting only recipes where 100% of required ingredients are present in the user's pantry in sufficient quantities.

### Key Features Implemented:
- **5 Distinct Screens:** Pantry List (`MainActivity`), Add/Edit Ingredient (`AddEditPantryActivity`), Suggested Recipes (`SuggestedRecipesActivity`), Recipe Detail (`RecipeDetailActivity`), and User Preferences (`SettingsActivity`).
- **Strict-Matching Engine:** Filters out any recipe missing even a single ingredient or lacking the required quantity, with string normalization for singular/plural names (e.g., "tomato" vs "tomatoes") and unit conversion (`kg` to `g`, `l` to `ml`).
- **Bonus Stretch Feature ("Almost There"):** Includes a dedicated toggle to view recipes missing strictly 1 ingredient, kept completely separate from the main strict-match list.
- **Input Validation & Empty States:** Prevents blank or invalid numbers on forms and displays clear user feedback when zero recipes match.

## 2. Database Choice & Justification (SQLite)
This application uses **SQLite (`SQLiteOpenHelper`)** for local on-device persistence (`smart_pantry.db`):
- **Offline Availability:** A kitchen and pantry utility must function reliably offline without requiring an active internet connection.
- **Zero Latency for Strict Matching:** Evaluating 16 pre-seeded recipes and comparing ingredient quantities executes instantaneously on local SQLite tables.
- **Full CRUD Persistence:** Supports Create, Read, Update, and Delete operations on the `pantry` table and pre-seeds 16 recipes into the `recipes` table on initial database creation.

## 3. Setup & Run Instructions
1. Clone this repository using Git:
   `git clone [https://github.com/Lijahsaur/SmartPantryManager.git](https://github.com/Lijahsaur/SmartPantryManager.git)`
2. Open **Android Studio** and select **File -> Open**, then choose the cloned `SmartPantryManager` project folder.
3. Wait for Gradle to finish syncing dependencies automatically.
4. Select an Android Emulator (API 24 or higher) in Device Manager, or connect a physical Android device with USB Debugging enabled.
5. Click the green **Run 'app'** play button (`Shift + F10`) in the top toolbar.