package sk.dejw.android.bakingrecipes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.services.RecipeService;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeStepClickListener {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    public static final String EXTRA_RECIPE = "recipe";
    public static final String BUNDLE_RECIPE = "recipe";

    Recipe mRecipe = null;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            Log.d(TAG, "Intent: " + startingIntent.toString());
            if (startingIntent.hasExtra(EXTRA_RECIPE)) {
                Log.d(TAG, "Extras: " + startingIntent.getExtras().toString());
                mRecipe = startingIntent.getExtras().getParcelable(EXTRA_RECIPE);
            }
        }

        if (mRecipe == null && savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(BUNDLE_RECIPE);
        }
        Log.d(TAG, "Recipe: " + mRecipe.getName());

        setTitle(mRecipe.getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(mRecipe);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }

        if (findViewById(R.id.recipe_step_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setRecipe(mRecipe);
                recipeStepFragment.setRecipeStepPosition(0);

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_container, recipeStepFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        Log.d(TAG, "Calling startActionUpdateRecipeWidgets");
        RecipeService.startActionUpdateRecipeWidgets(this, mRecipe.getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    public void onRecipeStepSelected(int recipeStepPosition) {
        if (mTwoPane) {
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setRecipe(mRecipe);
            recipeStepFragment.setRecipeStepPosition(recipeStepPosition);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepFragment)
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeStepFragment.RECIPE, mRecipe);
            bundle.putInt(RecipeStepFragment.RECIPE_STEP_POSITION, recipeStepPosition);
            bundle.putString(RecipeStepActivity.RECIPE_NAME, mRecipe.getName());

            final Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
