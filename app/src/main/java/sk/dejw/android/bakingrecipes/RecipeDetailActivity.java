package sk.dejw.android.bakingrecipes;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.models.RecipeStep;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeStepClickListener {

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
            if (startingIntent.hasExtra(EXTRA_RECIPE)) {
                mRecipe = getIntent().getExtras().getParcelable(EXTRA_RECIPE);
            }
        }

        if (mRecipe == null && savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(BUNDLE_RECIPE);
        }

        setTitle(mRecipe.getName());

        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();

        if(findViewById(R.id.recipe_step_container) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setRecipeStep(mRecipe.getSteps()[0]);

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_container, recipeStepFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    public void onRecipeStepSelected(RecipeStep recipeStep) {
        if(mTwoPane == true) {

            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

            recipeStepFragment.setRecipeStep(recipeStep);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepFragment)
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeStepFragment.RECIPE_STEP, recipeStep);
            bundle.putString(RecipeStepActivity.RECIPE_NAME, mRecipe.getName());

            final Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
