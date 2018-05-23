package sk.dejw.android.bakingrecipes.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.dejw.android.bakingrecipes.models.Ingredient;

public final class IngredientJsonUtils {

    public static Ingredient[] getIngredientsFromJsonArray(JSONArray jsonArray)
            throws JSONException {

        final String INGREDIENT_QUANTITY = "quantity";
        final String INGREDIENT_MEASURE = "measure";
        final String INGREDIENT_INGREDIENT = "ingredient";

        //TODO handle error
        Ingredient[] ingredients = new Ingredient[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            ingredients[i] = new Ingredient(
                jsonObject.getInt(INGREDIENT_QUANTITY),
                jsonObject.getString(INGREDIENT_MEASURE),
                jsonObject.getString(INGREDIENT_INGREDIENT)
            );
        }
        return ingredients;
    }
}