package sk.dejw.android.bakingrecipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import sk.dejw.android.bakingrecipes.models.Ingredient;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;
import sk.dejw.android.bakingrecipes.provider.RecipeProvider;
import sk.dejw.android.bakingrecipes.ui.RecipeDetailActivity;
import sk.dejw.android.bakingrecipes.utils.RecipeCursorUtils;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = GridRemoteViewsFactory.class.getSimpleName();

    Context mContext;
    Cursor mCursor;
    Recipe mRecipe;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged");
        mRecipe = null;
        if (mCursor != null) mCursor.close();
        try {
            mCursor = mContext.getContentResolver().query(
                    RecipeProvider.Recipes.RECIPES_URI,
                    null,
                    RecipeContract.COLUMN_SHOW_ON_WIDGET + " = ?",
                    new String[]{ "1" },
                    RecipeContract.COLUMN_ID
            );
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "Cursor count " + mCursor.getCount());
        if (mCursor != null && mCursor.getCount() > 0) {
            mRecipe = RecipeCursorUtils.getFirstRecipeFromCursor(mCursor);
            mCursor.close();
        } else {
            mRecipe = Recipe.mockObject();
        }
        Log.d(TAG, "Recipe " + mRecipe.getName());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipe == null || mRecipe.getIngredients() == null) return 0;
        Log.d(TAG, "Recipe ingredients count" + mRecipe.getIngredients().length);
        return mRecipe.getIngredients().length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mRecipe == null || mRecipe.getIngredients().length == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_ingredient_item);

        Ingredient ingredient = mRecipe.getIngredients()[i];
        views.setTextViewText(R.id.tv_ingredient_name, ingredient.getIngredient());
        views.setTextViewText(R.id.tv_ingredient_quantity, String.format("%d %s", ingredient.getQuantity(), ingredient.getMeasure()));

        Log.d(TAG, "i " + i);
        Log.d(TAG, "Ingredient " + ingredient.getIngredient());

        Bundle extras = new Bundle();
        extras.putParcelable(RecipeDetailActivity.EXTRA_RECIPE, mRecipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.tv_ingredient_name, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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
