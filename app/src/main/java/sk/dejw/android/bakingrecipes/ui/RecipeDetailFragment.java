package sk.dejw.android.bakingrecipes.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.adapters.IngredientAdapter;
import sk.dejw.android.bakingrecipes.adapters.RecipeStepAdapter;
import sk.dejw.android.bakingrecipes.models.Ingredient;
import sk.dejw.android.bakingrecipes.models.Recipe;
import sk.dejw.android.bakingrecipes.models.RecipeStep;

public class RecipeDetailFragment extends Fragment  implements RecipeStepAdapter.RecipeStepAdapterOnClickHandler {
    private static final String RECIPE = "recipe";

    private Recipe mRecipe;

    OnRecipeStepClickListener mCallback;

    @BindView(R.id.rv_ingredient_list_view)
    RecyclerView mIngredientListView;
    @BindView(R.id.rv_step_list_view)
    RecyclerView mStepListView;

    IngredientAdapter mIngredientAdapter;
    RecipeStepAdapter mRecipeStepAdapter;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mRecipe == null && savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
        }

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        mIngredientListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientAdapter = new IngredientAdapter(new ArrayList<Ingredient>(Arrays.asList(mRecipe.getIngredients())));
        mIngredientListView.setAdapter(mIngredientAdapter);

        mStepListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecipeStepAdapter = new RecipeStepAdapter(getActivity(), new ArrayList<RecipeStep>(Arrays.asList(mRecipe.getSteps())), this);
        mStepListView.setAdapter(mRecipeStepAdapter);

        return rootView;
    }

    @Override
    public void onRecipeStepClick(int recipeStepPosition) {
        mCallback.onRecipeStepSelected(recipeStepPosition);
    }

    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(int recipeStepPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE, mRecipe);
        super.onSaveInstanceState(currentState);
    }
}
