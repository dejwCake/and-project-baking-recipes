package sk.dejw.android.bakingrecipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sk.dejw.android.bakingrecipes.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipe";

    Recipe mRecipe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
    }
}
