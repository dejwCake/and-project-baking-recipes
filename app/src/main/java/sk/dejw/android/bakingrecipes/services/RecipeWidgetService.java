package sk.dejw.android.bakingrecipes.services;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import sk.dejw.android.bakingrecipes.GridRemoteViewsFactory;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.ui.RecipeDetailActivity;

public class RecipeWidgetService extends RemoteViewsService {

    public static final String TAG = RecipeWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "Intent is " + intent.toString());
        final Recipe recipe = intent.getParcelableExtra(RecipeDetailActivity.EXTRA_RECIPE);
        Log.d(TAG, "Recipe on widget is " + recipe.getId());
        return new GridRemoteViewsFactory(this.getApplicationContext(), recipe);
    }
}
