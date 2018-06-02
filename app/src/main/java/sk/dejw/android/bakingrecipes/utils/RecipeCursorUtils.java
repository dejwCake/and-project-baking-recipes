package sk.dejw.android.bakingrecipes.utils;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;

public final class RecipeCursorUtils {

    public static ArrayList<Recipe> getRecipesFromCursor(Cursor cursor) {
        final Integer RECIPE_ID = cursor.getColumnIndex(RecipeContract.COLUMN_ID);
        final Integer RECIPE_NAME = cursor.getColumnIndex(RecipeContract.COLUMN_NAME);
        final Integer RECIPE_INGREDIENTS_JSON = cursor.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS_JSON);
        final Integer RECIPE_STEPS_JSON = cursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_JSON);
        final Integer RECIPE_SERVINGS = cursor.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        final Integer RECIPE_IMAGE = cursor.getColumnIndex(RecipeContract.COLUMN_IMAGE);

        ArrayList<Recipe> listOfRecipes = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                JSONArray ingredientsJsonArray = new JSONArray(cursor.getString(RECIPE_INGREDIENTS_JSON));
                JSONArray stepsJsonArray = new JSONArray(cursor.getString(RECIPE_STEPS_JSON));
                Recipe recipe = new Recipe(
                        cursor.getInt(RECIPE_ID),
                        cursor.getString(RECIPE_NAME),
                        IngredientJsonUtils.getIngredientsFromJsonArray(ingredientsJsonArray),
                        RecipeStepJsonUtils.getRecipeStepsFromJsonArray(stepsJsonArray),
                        cursor.getInt(RECIPE_SERVINGS),
                        cursor.getString(RECIPE_IMAGE)
                );
                listOfRecipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfRecipes;
    }

    public static Recipe getFirstRecipeFromCursor(Cursor cursor) {
        final Integer RECIPE_ID = cursor.getColumnIndex(RecipeContract.COLUMN_ID);
        final Integer RECIPE_NAME = cursor.getColumnIndex(RecipeContract.COLUMN_NAME);
        final Integer RECIPE_INGREDIENTS_JSON = cursor.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS_JSON);
        final Integer RECIPE_STEPS_JSON = cursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_JSON);
        final Integer RECIPE_SERVINGS = cursor.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        final Integer RECIPE_IMAGE = cursor.getColumnIndex(RecipeContract.COLUMN_IMAGE);

        Recipe recipe = null;
        try {
            cursor.moveToFirst();
            JSONArray ingredientsJsonArray = new JSONArray(cursor.getString(RECIPE_INGREDIENTS_JSON));
            JSONArray stepsJsonArray = new JSONArray(cursor.getString(RECIPE_STEPS_JSON));
            recipe = new Recipe(
                    cursor.getInt(RECIPE_ID),
                    cursor.getString(RECIPE_NAME),
                    IngredientJsonUtils.getIngredientsFromJsonArray(ingredientsJsonArray),
                    RecipeStepJsonUtils.getRecipeStepsFromJsonArray(stepsJsonArray),
                    cursor.getInt(RECIPE_SERVINGS),
                    cursor.getString(RECIPE_IMAGE)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipe;
    }
}