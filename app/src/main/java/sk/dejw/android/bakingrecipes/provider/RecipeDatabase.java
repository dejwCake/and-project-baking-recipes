package sk.dejw.android.bakingrecipes.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to create a database with one
 * table for messages
 */

@Database(version = RecipeDatabase.VERSION)
public class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(RecipeContract.class)
    public static final String RECIPES = "recipes";

}
