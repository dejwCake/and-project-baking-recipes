package sk.dejw.android.bakingrecipes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Recipe implements Parcelable {
    private Integer id;
    private String name;
    private Ingredient[] ingredients;
    private RecipeStep[] steps;
    private Integer servings;
    private String image;

    public Recipe(Integer id, String name, Ingredient[] ingredients, RecipeStep[] steps, Integer servings, String image)
    {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArray(Ingredient.CREATOR);
        steps = in.createTypedArray(RecipeStep.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedArray(ingredients, flags);
        dest.writeTypedArray(steps, flags);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public String getIngredientsInJson() {
        Gson gson = new Gson();
        return gson.toJson(ingredients);
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public RecipeStep[] getSteps() {
        return steps;
    }

    public String getStepsInJson() {
        Gson gson = new Gson();
        return gson.toJson(steps);
    }

    public void setSteps(RecipeStep[] steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Recipe mockObject() {
        Ingredient[] ingredients = {
            new Ingredient(500, "G", "Mascapone Cheese(room temperature)"),
            new Ingredient(2, "CUP", "heavy cream(cold)"),
        };
        RecipeStep[] steps = {
                new RecipeStep(0, "Recipe Introduction", "Recipe Introduction", "", ""),
        };
        Recipe recipe = new Recipe(0, "Test", ingredients, steps, 2, "");
        return recipe;
    }
}
