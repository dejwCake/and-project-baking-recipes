package sk.dejw.android.bakingrecipes.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.dejw.android.bakingrecipes.models.Recipe;

public final class RecipeJsonUtils {

    public static Recipe[] getRecipesFromJson(String jsonString)
            throws JSONException {

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        /* String array to hold each day's weather String */
        Recipe[] recipes;

        JSONArray jsonArray = new JSONArray(jsonString);

        //TODO handle error

        recipes = new Recipe[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            recipes[i] = new Recipe(
                jsonObject.getInt(RECIPE_ID),
                jsonObject.getString(RECIPE_NAME),
                IngredientJsonUtils.getIngredientsFromJsonArray(jsonObject.getJSONArray(RECIPE_INGREDIENTS)),
                RecipeStepJsonUtils.getRecipeStepsFromJsonArray(jsonObject.getJSONArray(RECIPE_STEPS)),
                jsonObject.getInt(RECIPE_SERVINGS),
                jsonObject.getString(RECIPE_IMAGE)
            );
        }
        return recipes;
    }
}