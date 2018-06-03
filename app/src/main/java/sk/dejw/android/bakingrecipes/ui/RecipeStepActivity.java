package sk.dejw.android.bakingrecipes.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.models.RecipeStep;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepActivity.class.getSimpleName();
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE = "recipe";
    public static final String RECIPE_STEP_POSITION = "recipe_step_position";

    private String mRecipeName;
    private Recipe mRecipe;
    private int mRecipeStepPosition;
    private ArrayList<RecipeStep> mRecipeSteps;

    @BindView(R.id.bt_prev_step)
    Button mPreviousStep;
    @BindView(R.id.bt_next_step)
    Button mNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

        if(savedInstanceState == null) {

            mRecipeName = getIntent().getStringExtra(RECIPE_NAME);
            setTitle(mRecipeName);

            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

            mRecipe = getIntent().getParcelableExtra(RecipeStepFragment.RECIPE);
            mRecipeSteps = new ArrayList<>(Arrays.asList(mRecipe.getSteps()));
            recipeStepFragment.setRecipe(mRecipe);
            mRecipeStepPosition = getIntent().getIntExtra(RecipeStepFragment.RECIPE_STEP_POSITION, 0);
            recipeStepFragment.setRecipeStepPosition(mRecipeStepPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_container, recipeStepFragment)
                    .commit();
        } else {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            if (mRecipe != null) {
                mRecipeSteps = new ArrayList<>(Arrays.asList(mRecipe.getSteps()));
            }
            mRecipeStepPosition = savedInstanceState.getInt(RECIPE_STEP_POSITION);
            mRecipeName = savedInstanceState.getString(RECIPE_NAME);
            setTitle(mRecipeName);
        }

        initializeButtons();
    }

    private void initializeButtons() {
        final int previous = mRecipeStepPosition - 1;
        mPreviousStep.setVisibility(View.VISIBLE);
        mNextStep.setVisibility(View.VISIBLE);

        if(previous < 0) {
            mPreviousStep.setVisibility(View.GONE);
        } else {
            mPreviousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Previous clicked");
                    mRecipeStepPosition = previous;
                    RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

                    recipeStepFragment.setRecipe(mRecipe);
                    recipeStepFragment.setRecipeStepPosition(previous);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_container, recipeStepFragment)
                            .commit();
                    initializeButtons();
                }
            });
        }
        final int next = mRecipeStepPosition + 1;
        if(next >= mRecipeSteps.size()) {
            mNextStep.setVisibility(View.GONE);
        } else {
            mNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Previous clicked");
                    mRecipeStepPosition = next;
                    RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

                    recipeStepFragment.setRecipe(mRecipe);
                    recipeStepFragment.setRecipeStepPosition(next);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_container, recipeStepFragment)
                            .commit();
                    initializeButtons();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, mRecipe);
        outState.putInt(RECIPE_STEP_POSITION, mRecipeStepPosition);
        outState.putString(RECIPE_NAME, mRecipeName);
        super.onSaveInstanceState(outState);
    }
}
