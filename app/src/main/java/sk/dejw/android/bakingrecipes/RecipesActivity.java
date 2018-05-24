package sk.dejw.android.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.adapters.RecipeAdapter;
import sk.dejw.android.bakingrecipes.asyncTasks.AsyncTaskCompleteListener;
import sk.dejw.android.bakingrecipes.asyncTasks.FetchRecipesTask;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.utils.GlobalNetworkUtils;

public class RecipesActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

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

        mRecipeListView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new RecipeAdapter(mListOfRecipes, this);
        mRecipeListView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Get data from remote with async task
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_RECIPES, mListOfRecipes);
        super.onSaveInstanceState(outState);
    }

    private void loadData() {
        showDataView();

        if (GlobalNetworkUtils.hasConnection(this)) {
            Log.d(TAG, "Internet working.");
            mLoadingIndicator.setVisibility(View.VISIBLE);
            new FetchRecipesTask(this, new FetchRecipesTaskCompleteListener()).execute();
        } else {
            showErrorMessage();
        }

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

    public class FetchRecipesTaskCompleteListener implements AsyncTaskCompleteListener<Recipe[]>
    {
        @Override
        public void onTaskComplete(Recipe[] recipes)
        {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipes != null) {
                showDataView();
                ArrayList<Recipe> listOfRecipes = new ArrayList<Recipe>(Arrays.asList(recipes));
                mAdapter.swapData(listOfRecipes);
            } else {
                showErrorMessage();
            }
        }
    }
}
