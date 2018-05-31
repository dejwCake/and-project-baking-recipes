package sk.dejw.android.bakingrecipes.asyncTasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;
import sk.dejw.android.bakingrecipes.provider.RecipeProvider;

public class SaveRecipesTask extends AsyncTask<Recipe[], Void, String> {
    private static final String TAG = SaveRecipesTask.class.getSimpleName();

    public static final String OK_RESULT = "OK";

    private Context mContext;
    private AsyncTaskCompleteListener<String> listener;

    public SaveRecipesTask(Context context, AsyncTaskCompleteListener<String> listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Recipe[]... params) {
        Recipe[] recipes = params[0];
        try {

            Cursor cursor = mContext.getContentResolver().query(
                    RecipeProvider.Recipes.RECIPES_URI,
                    null,
                    null,
                    null,
                    RecipeContract.COLUMN_ID
            );

            for (int i = 0; i < recipes.length; i++) {
                ContentValues newRecipe = new ContentValues();
                newRecipe.put(RecipeContract.COLUMN_ID, recipes[i].getId());
                newRecipe.put(RecipeContract.COLUMN_NAME, recipes[i].getName());
                newRecipe.put(RecipeContract.COLUMN_INGREDIENTS_JSON, recipes[i].getIngredientsInJson());
                newRecipe.put(RecipeContract.COLUMN_RECIPE_STEPS_JSON, recipes[i].getStepsInJson());
                newRecipe.put(RecipeContract.COLUMN_SERVINGS, recipes[i].getServings());
                newRecipe.put(RecipeContract.COLUMN_IMAGE, recipes[i].getImage());
                if ( mContext.getContentResolver().update( RecipeProvider.Recipes.RECIPES_URI, newRecipe, RecipeContract.COLUMN_ID + " = ?", new String[]{ recipes[i].getId().toString() } ) == 0 ) {
                    mContext.getContentResolver().insert(RecipeProvider.Recipes.RECIPES_URI, newRecipe);
                }
            }

            return OK_RESULT;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onTaskComplete(result);
    }
}
