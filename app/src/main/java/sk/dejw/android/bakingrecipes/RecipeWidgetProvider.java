package sk.dejw.android.bakingrecipes;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeContract;
import sk.dejw.android.bakingrecipes.services.RecipeService;
import sk.dejw.android.bakingrecipes.services.RecipeWidgetService;
import sk.dejw.android.bakingrecipes.ui.RecipeDetailActivity;
import sk.dejw.android.bakingrecipes.ui.RecipesActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String TAG = RecipeWidgetProvider.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if (width < 300) {
            rv = getRecipeImageRemoteView(context, recipe, appWidgetId);
        } else {
            rv = getRecipeStepsRemoteView(context, recipe, appWidgetId);
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getRecipeImageRemoteView(Context context, Recipe recipe, int appWidgetId) {
        Intent intent;
        if (recipe == null || recipe.getId() == RecipeContract.INVALID_RECIPE_ID) {
            intent = new Intent(context, RecipesActivity.class);
        } else { // Set on click to open the corresponding detail activity
            Log.d(TAG, "getRecipeImageRemoteView recipeId=" + recipe.getId());
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
            intent.setData(Uri.withAppendedPath(Uri.parse("myapp://widget/id/#togetituniqie" + appWidgetId), String.valueOf(appWidgetId)));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_image);

        if (recipe != null && recipe.equals("")) {
            int[] appWidgetIds = {appWidgetId};
            Picasso.with(context)
                    .load(recipe.getImage())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(views, R.id.iv_widget_recipe_image, appWidgetIds);
        }

        views.setTextViewText(R.id.tv_widget_recipe_name, String.valueOf(recipe.getName()));

        Log.d(TAG, "Pending intent" + pendingIntent.toString());

        views.setOnClickPendingIntent(R.id.iv_widget_recipe_image, pendingIntent);
        views.setOnClickPendingIntent(R.id.tv_widget_recipe_name, pendingIntent);

        return views;
    }

    private static RemoteViews getRecipeStepsRemoteView(Context context, Recipe recipe, int appWidgetId) {
        Log.d(TAG, "getRecipeStepsRemoteView recipeId = " + recipe.getId());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_steps_view);

        Intent intent = new Intent(context, RecipeWidgetService.class);
//        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        views.setRemoteAdapter(R.id.gv_widget_recipe_steps_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        appIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        appIntent.setData(Uri.withAppendedPath(Uri.parse("myapp://widget/id/#togetituniqie" + appWidgetId), String.valueOf(appWidgetId)));
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.gv_widget_recipe_steps_view, appPendingIntent);
        views.setEmptyView(R.id.gv_widget_recipe_steps_view, R.id.empty_view);
        return views;
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        RecipeService.startActionUpdateRecipeWidgets(context, RecipeContract.INVALID_RECIPE_ID);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
//        RecipeService.startActionUpdateRecipeWidgets(context, RecipeContract.INVALID_RECIPE_ID);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}
