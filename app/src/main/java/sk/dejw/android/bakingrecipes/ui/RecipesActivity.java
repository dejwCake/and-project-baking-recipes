package sk.dejw.android.bakingrecipes.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.adapters.RecipeAdapter;
import sk.dejw.android.bakingrecipes.asyncTasks.AsyncTaskCompleteListener;
import sk.dejw.android.bakingrecipes.asyncTasks.FetchRecipesTask;
import sk.dejw.android.bakingrecipes.asyncTasks.SaveRecipesTask;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.provider.RecipeProvider;
import sk.dejw.android.bakingrecipes.utils.GlobalNetworkUtils;
import sk.dejw.android.bakingrecipes.utils.RecipeCursorUtils;

public class RecipesActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RecipesActivity.class.getSimpleName();

    @BindView(R.id.recipe_list_view)
    RecyclerView mRecipeListView;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    RecipeAdapter mAdapter;

    private ArrayList<Recipe> mListOfRecipes;

    public static final String BUNDLE_RECIPES = "recipes";

    private static final int RECIPE_LOADER_ID = 156;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        if (savedInstanceState == null || !savedInstanceState.containsKey(BUNDLE_RECIPES)) {
            mListOfRecipes = new ArrayList<Recipe>();
        } else {
            mListOfRecipes = savedInstanceState.getParcelableArrayList(BUNDLE_RECIPES);
        }
        ButterKnife.bind(this);

        if(findViewById(R.id.phone_frame_layout) != null) {
            mRecipeListView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            mRecipeListView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        mAdapter = new RecipeAdapter(mListOfRecipes, this);
        mRecipeListView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Get data from remote with async task
        loadDataFromInternet();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_RECIPES, mListOfRecipes);
        super.onSaveInstanceState(outState);
    }

    private void loadDataFromInternet() {
        showDataView();

        if (GlobalNetworkUtils.hasConnection(this)) {
            Log.d(TAG, "Internet working.");
            mLoadingIndicator.setVisibility(View.VISIBLE);
            new FetchRecipesTask(this, new FetchRecipesTaskCompleteListener()).execute();
        } else {
            showErrorMessage();
        }

    }

    private void saveDataFromInternet(Recipe[] recipes) {
        new SaveRecipesTask(this, new SaveRecipesTaskCompleteListener()).execute(recipes);
    }

    private void loadData() {
        getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, this);
    }

    private void showDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecipeListView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecipeListView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                RecipeProvider.Recipes.RECIPES_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Recipes loaded: ".concat(String.valueOf(data.getCount())));
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data.getCount() != 0) {
            showDataView();
            ArrayList<Recipe> listOfRecipes = RecipeCursorUtils.getRecipesFromCursor(data);
            mAdapter.swapData(listOfRecipes);
            mAdapter.notifyDataSetChanged();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class FetchRecipesTaskCompleteListener implements AsyncTaskCompleteListener<Recipe[]>
    {
        @Override
        public void onTaskComplete(Recipe[] recipes)
        {
            Log.d(TAG, "Recipes downloaded: ".concat(String.valueOf(recipes.length)));
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipes != null) {
                saveDataFromInternet(recipes);
            } else {
                showErrorMessage();
            }
        }
    }

    public class SaveRecipesTaskCompleteListener implements AsyncTaskCompleteListener<String>
    {
        @Override
        public void onTaskComplete(String result)
        {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result.equals(SaveRecipesTask.OK_RESULT)) {
                loadData();
            } else {
                showErrorMessage();
            }
        }
    }
}
