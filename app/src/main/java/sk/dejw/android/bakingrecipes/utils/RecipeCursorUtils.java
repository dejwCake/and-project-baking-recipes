package sk.dejw.android.bakingrecipes.utils;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;

public final class RecipeCursorUtils {

    public static ArrayList<Recipe> getRecipesFromCursor(Cursor data) {
        final Integer RECIPE_ID = data.getColumnIndex(RecipeContract.COLUMN_ID);
        final Integer RECIPE_NAME = data.getColumnIndex(RecipeContract.COLUMN_NAME);
        final Integer RECIPE_INGREDIENTS_JSON = data.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS_JSON);
        final Integer RECIPE_STEPS_JSON = data.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_JSON);
        final Integer RECIPE_SERVINGS = data.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        final Integer RECIPE_IMAGE = data.getColumnIndex(RecipeContract.COLUMN_IMAGE);

        ArrayList<Recipe> listOfRecipes = new ArrayList<>();
        try {
            while (data.moveToNext()) {
                JSONArray ingredientsJsonArray = new JSONArray(data.getString(RECIPE_INGREDIENTS_JSON));
                JSONArray stepsJsonArray = new JSONArray(data.getString(RECIPE_STEPS_JSON));
                Recipe recipe = new Recipe(
                        data.getInt(RECIPE_ID),
                        data.getString(RECIPE_NAME),
                        IngredientJsonUtils.getIngredientsFromJsonArray(ingredientsJsonArray),
                        RecipeStepJsonUtils.getRecipeStepsFromJsonArray(stepsJsonArray),
                        data.getInt(RECIPE_SERVINGS),
                        data.getString(RECIPE_IMAGE)
                );
                listOfRecipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            data.close();
        }
        return listOfRecipes;
    }

    public static Recipe getFirstRecipeFromCursor(Cursor data) {
        final Integer RECIPE_ID = data.getColumnIndex(RecipeContract.COLUMN_ID);
        final Integer RECIPE_NAME = data.getColumnIndex(RecipeContract.COLUMN_NAME);
        final Integer RECIPE_INGREDIENTS_JSON = data.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS_JSON);
        final Integer RECIPE_STEPS_JSON = data.getColumnIndex(RecipeContract.COLUMN_RECIPE_STEPS_JSON);
        final Integer RECIPE_SERVINGS = data.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        final Integer RECIPE_IMAGE = data.getColumnIndex(RecipeContract.COLUMN_IMAGE);

        Recipe recipe = null;
        try {
            data.moveToFirst();
            JSONArray ingredientsJsonArray = new JSONArray(data.getString(RECIPE_INGREDIENTS_JSON));
            JSONArray stepsJsonArray = new JSONArray(data.getString(RECIPE_STEPS_JSON));
            recipe = new Recipe(
                    data.getInt(RECIPE_ID),
                    data.getString(RECIPE_NAME),
                    IngredientJsonUtils.getIngredientsFromJsonArray(ingredientsJsonArray),
                    RecipeStepJsonUtils.getRecipeStepsFromJsonArray(stepsJsonArray),
                    data.getInt(RECIPE_SERVINGS),
                    data.getString(RECIPE_IMAGE)
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            data.close();
        }
        return recipe;
    }
}