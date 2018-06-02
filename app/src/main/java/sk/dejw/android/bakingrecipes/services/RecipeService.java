package sk.dejw.android.bakingrecipes.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.RecipeWidgetProvider;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;
import sk.dejw.android.bakingrecipes.provider.RecipeProvider;
import sk.dejw.android.bakingrecipes.utils.RecipeCursorUtils;

public class RecipeService extends IntentService {

    public static final String TAG = RecipeService.class.getSimpleName();
    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "sk.dejw.android.bakingrecipes.action.update_recipe_widgets";
    public static final String EXTRA_RECIPE_ID = "sk.dejw.android.bakingrecipes.extra.RECIPE_ID";

    public RecipeService() {
        super("RecipeService");
    }

    public static void startActionUpdateRecipeWidgets(Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                final long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID,
                        RecipeContract.INVALID_RECIPE_ID);
                handleActionUpdateRecipeWidgets(recipeId);
            }
        }
    }

    private void handleActionUpdateRecipeWidgets(long recipeId) {
        Cursor cursor = null;
        if(recipeId == RecipeContract.INVALID_RECIPE_ID) {
            cursor = getContentResolver().query(
                    RecipeProvider.Recipes.RECIPES_URI,
                    null,
                    null,
                    null,
                    RecipeContract.COLUMN_ID
            );
        } else {
            cursor = getContentResolver().query(
                    RecipeProvider.Recipes.withId(recipeId),
                    null,
                    null,
                    null,
                    RecipeContract.COLUMN_ID
            );
            Log.d(TAG, "Cursor count: " + cursor.getCount());
        }

        Recipe recipe = null;
        if (cursor != null && cursor.getCount() > 0) {
            recipe = RecipeCursorUtils.getFirstRecipeFromCursor(cursor);
            cursor.close();
        }

        ContentValues dontShowInWidget = new ContentValues();
        dontShowInWidget.put(RecipeContract.COLUMN_SHOW_ON_WIDGET, 0);
        getContentResolver().update(
            RecipeProvider.Recipes.RECIPES_URI,
                dontShowInWidget,
            RecipeContract.COLUMN_ID + " != ?",
            new String[]{ recipe.getId().toString() }
        );

        ContentValues showInWidget = new ContentValues();
        showInWidget.put(RecipeContract.COLUMN_SHOW_ON_WIDGET, 1);
        getContentResolver().update(
                RecipeProvider.Recipes.RECIPES_URI,
                showInWidget,
                RecipeContract.COLUMN_ID + " = ?",
                new String[]{ recipe.getId().toString() }
        );

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_widget_recipe_ingredients_view);
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipe);
    }
}
