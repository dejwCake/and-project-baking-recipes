package sk.dejw.android.bakingrecipes.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.utils.RecipeJsonUtils;
import sk.dejw.android.bakingrecipes.utils.RecipeNetworkUtils;

public class FetchRecipesTask extends AsyncTask<String, Void, Recipe[]> {
    private static final String TAG = FetchRecipesTask.class.getSimpleName();

    private Context mContext;
    private AsyncTaskCompleteListener<Recipe[]> listener;

    public FetchRecipesTask(Context context, AsyncTaskCompleteListener<Recipe[]> listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Recipe[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        URL requestUrl = RecipeNetworkUtils.buildUrl(mContext);

        try {
            String jsonResponse = RecipeNetworkUtils
                    .getResponseFromHttpUrl(requestUrl);

            return RecipeJsonUtils.getRecipesFromJson(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Recipe[] recipes) {
        super.onPostExecute(recipes);
        listener.onTaskComplete(recipes);
    }
}
