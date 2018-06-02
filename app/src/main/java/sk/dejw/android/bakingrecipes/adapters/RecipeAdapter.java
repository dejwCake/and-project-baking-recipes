package sk.dejw.android.bakingrecipes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mData;
    private RecipeAdapterOnClickHandler mClickHandler;

    public RecipeAdapter(Context context, ArrayList<Recipe> data, RecipeAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mData = data;
        this.mClickHandler = clickHandler;
    }

    public interface RecipeAdapterOnClickHandler {
        void onRecipeClick(Recipe recipe);
    }


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
        if(!recipe.getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.recipeImageView);
        } else {
            holder.recipeImageView.setVisibility(View.GONE);
        }
        holder.recipeServingsTextView.setText(String.valueOf(recipe.getServings()));
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
        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;
        @BindView(R.id.tv_recipe_servings)
        TextView recipeServingsTextView;
        @BindView(R.id.iv_recipe_image)
        ImageView recipeImageView;
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
