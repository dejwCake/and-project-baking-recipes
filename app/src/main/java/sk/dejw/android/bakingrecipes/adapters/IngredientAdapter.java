package sk.dejw.android.bakingrecipes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.Ingredient;
import sk.dejw.android.bakingrecipes.models.Recipe;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> mData;

    public IngredientAdapter(ArrayList<Ingredient> data){
        this.mData = data;
    }

    public interface RecipeAdapterOnClickHandler {
        void onRecipeClick(Recipe recipe);
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        Ingredient ingredient = mData.get(position);

        holder.ingredientNameTextView.setText(ingredient.getIngredient());
        holder.ingredientQuantityTextView.setText(String.format("%d %s", ingredient.getQuantity(), ingredient.getMeasure()));
        holder.itemPosition = position;
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_name) TextView ingredientNameTextView;
        @BindView(R.id.tv_ingredient_quantity) TextView ingredientQuantityTextView;
        int itemPosition;

        public IngredientViewHolder(View layoutView) {
            super(layoutView);
            ButterKnife.bind(this, layoutView);
        }
    }
}
