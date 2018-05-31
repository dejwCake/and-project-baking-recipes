package sk.dejw.android.bakingrecipes.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) to create a content provider and
 * define
 * URIs for the provider
 */

@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class)
public final class RecipeProvider {

    public static final String AUTHORITY = "sk.dejw.android.bakingrecipes.recipes";

    interface Path {
        String RECIPES = "recipes";
    }

    @TableEndpoint(table = RecipeDatabase.RECIPES)
    public static class Recipes {

        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeContract.COLUMN_ID + " ASC")
        public static final Uri RECIPES_URI = Uri.parse("content://" + AUTHORITY + "/recipes");

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "LIST_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = RecipeContract._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/recipes/" + id);
        }
    }
}