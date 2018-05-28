package sk.dejw.android.bakingrecipes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.Recipe;

/**
 * Converts cursor data for squawk messages into visible list items in a RecyclerView
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public RecipeAdapter(ArrayList<Recipe> data, RecipeAdapterOnClickHandler clickHandler){
        this.mData = data;
        this.mClickHandler = clickHandler;
    }

    public interface RecipeAdapterOnClickHandler {
        void onRecipeClick(Recipe recipe);
    }

    private ArrayList<Recipe> mData;
    private RecipeAdapterOnClickHandler mClickHandler;

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mData.get(position);

        holder.recipeNameTextView.setText(recipe.getName());
        holder.itemPosition = position;
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void swapData(ArrayList<Recipe> newData) {
        mData = newData;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name) TextView recipeNameTextView;
        int itemPosition;

        public RecipeViewHolder(View layoutView) {
            super(layoutView);
            ButterKnife.bind(this, layoutView);

            layoutView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe recipe = mData.get(itemPosition);
                    mClickHandler.onRecipeClick(recipe);
                }
            });
        }
    }
}
