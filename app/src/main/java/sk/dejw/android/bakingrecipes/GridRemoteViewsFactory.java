package sk.dejw.android.bakingrecipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;
import sk.dejw.android.bakingrecipes.provider.RecipeProvider;
import sk.dejw.android.bakingrecipes.ui.RecipeDetailActivity;
import sk.dejw.android.bakingrecipes.utils.RecipeCursorUtils;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = GridRemoteViewsFactory.class.getSimpleName();

    Context mContext;
    Recipe mRecipe;

    public GridRemoteViewsFactory(Context applicationContext, Recipe recipe) {
        mContext = applicationContext;
        mRecipe = recipe;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Cursor cursor = mContext.getContentResolver().query(
                RecipeProvider.Recipes.withId(mRecipe.getId()),
                null,
                null,
                null,
                RecipeContract.COLUMN_ID
        );
        if (cursor != null && cursor.getCount() > 0) {
            mRecipe = RecipeCursorUtils.getFirstRecipeFromCursor(cursor);
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipe.getSteps() == null) return 0;
        Log.d(TAG, "Recipe steps count" + mRecipe.getSteps().length);
        return mRecipe.getSteps().length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mRecipe == null || mRecipe.getSteps().length == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_step_list_item);

        views.setTextViewText(R.id.tv_recipe_step_description, mRecipe.getSteps()[i].getDescription());

        Bundle extras = new Bundle();
        extras.putParcelable(RecipeDetailActivity.EXTRA_RECIPE, mRecipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.tv_recipe_step_description, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
