package sk.dejw.android.bakingrecipes.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.dejw.android.bakingrecipes.R;
import sk.dejw.android.bakingrecipes.models.RecipeStep;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private Context mContext;
    private ArrayList<RecipeStep> mData;
    private RecipeStepAdapterOnClickHandler mClickHandler;
//    private SparseBooleanArray sSelectedItems;

    public RecipeStepAdapter(Context context, ArrayList<RecipeStep> data, RecipeStepAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mData = data;
        this.mClickHandler = clickHandler;
//        sSelectedItems = new SparseBooleanArray();
    }

    public interface RecipeStepAdapterOnClickHandler {
        void onRecipeStepClick(int recipeStepPosition);
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_list_item, parent, false);

        return new RecipeStepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        RecipeStep recipeStep = mData.get(position);

//        if (sSelectedItems.get(position)) {
//            holder.recipeStepPositionTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
//            holder.recipeStepShortDescriptionTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
//        } else {
//            holder.recipeStepPositionTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
//            holder.recipeStepShortDescriptionTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
//        }

        holder.recipeStepPositionTextView.setText(String.valueOf(recipeStep.getId() + 1).concat("."));
        holder.recipeStepShortDescriptionTextView.setText(recipeStep.getShortDescription());
        holder.itemPosition = position;
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_step_position)
        TextView recipeStepPositionTextView;
        @BindView(R.id.tv_recipe_step_short_description)
        TextView recipeStepShortDescriptionTextView;
//        @BindView(R.id.cl_recipe_step_item)
//        ConstraintLayout recipeStepItemConstraintLayout;
        int itemPosition;

        public RecipeStepViewHolder(View layoutView) {
            super(layoutView);
            ButterKnife.bind(this, layoutView);

            layoutView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (sSelectedItems.get(getAdapterPosition(), false)) {
//                        sSelectedItems.delete(getAdapterPosition());
//                        recipeStepItemConstraintLayout.setSelected(false);
//                        recipeStepPositionTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
//                        recipeStepShortDescriptionTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
//                    } else {
//                        sSelectedItems.put(itemPosition, false);
//                        recipeStepPositionTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
//                        recipeStepShortDescriptionTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
//                        sSelectedItems.put(getAdapterPosition(), true);
//                        recipeStepItemConstraintLayout.setSelected(true);
//                    }
                    mClickHandler.onRecipeStepClick(itemPosition);
                }
            });
        }
    }
}
