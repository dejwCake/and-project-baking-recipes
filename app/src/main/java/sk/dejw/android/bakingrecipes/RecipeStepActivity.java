package sk.dejw.android.bakingrecipes;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import sk.dejw.android.bakingrecipes.models.Recipe;

public class RecipeStepActivity extends AppCompatActivity {

    public static final String RECIPE_NAME = "recipe_name";

    private String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        if(savedInstanceState == null) {

            mRecipeName = getIntent().getStringExtra(RECIPE_NAME);
            setTitle(mRecipeName);

            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

            Recipe recipe = getIntent().getParcelableExtra(RecipeStepFragment.RECIPE);
            recipeStepFragment.setRecipe(recipe);
            int recipeStepPosition = getIntent().getIntExtra(RecipeStepFragment.RECIPE_STEP_POSITION, 0);
            recipeStepFragment.setRecipeStepPosition(recipeStepPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_container, recipeStepFragment)
                    .commit();
        } else {
            mRecipeName = savedInstanceState.getString(RECIPE_NAME);
            setTitle(mRecipeName);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(RECIPE_NAME, mRecipeName);
        super.onSaveInstanceState(outState);
    }
}
