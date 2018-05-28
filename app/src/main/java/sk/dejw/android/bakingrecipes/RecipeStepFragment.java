package sk.dejw.android.bakingrecipes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.models.RecipeStep;

public class RecipeStepFragment extends Fragment {

    public static final String RECIPE_STEP = "recipe_step";

    private RecipeStep mRecipeStep;

    @BindView(R.id.tv_step_description)
    TextView mDescription;

    public RecipeStepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecipeStep = savedInstanceState.getParcelable(RECIPE_STEP);
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        mDescription.setText(mRecipeStep.getDescription());

        //TODO setup layout
        return rootView;
    }

    public void setRecipeStep(RecipeStep recipeStep) {
        mRecipeStep = recipeStep;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE_STEP, mRecipeStep);
        super.onSaveInstanceState(currentState);
    }
}
