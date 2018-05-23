package sk.dejw.android.bakingrecipes.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.dejw.android.bakingrecipes.models.RecipeStep;

public final class RecipeStepJsonUtils {

    public static RecipeStep[] getRecipeStepsFromJsonArray(JSONArray jsonArray)
            throws JSONException {

        final String RECIPE_STEP_ID = "id";
        final String RECIPE_STEP_SHORT_DESCRIPTION = "shortDescription";
        final String RECIPE_STEP_DESCRIPTION = "description";
        final String RECIPE_STEP_VIDEO_URL = "videoURL";
        final String RECIPE_STEP_THUMBNAIL_URL = "thumbnailURL";

        //TODO handle error

        RecipeStep[] recipeSteps = new RecipeStep[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            /* Get the JSON object representing the day */
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            recipeSteps[i] = new RecipeStep(
                jsonObject.getInt(RECIPE_STEP_ID),
                jsonObject.getString(RECIPE_STEP_SHORT_DESCRIPTION),
                jsonObject.getString(RECIPE_STEP_DESCRIPTION),
                jsonObject.getString(RECIPE_STEP_VIDEO_URL),
                jsonObject.getString(RECIPE_STEP_THUMBNAIL_URL)
            );
        }
        return recipeSteps;
    }
}