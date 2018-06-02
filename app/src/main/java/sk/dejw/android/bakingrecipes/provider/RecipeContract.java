package sk.dejw.android.bakingrecipes.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to define the columns in a
 * content provider baked by a database
 */

public class RecipeContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_ID = "recipe_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_NAME = "name";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_INGREDIENTS_JSON = "ingredients_json";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_RECIPE_STEPS_JSON = "recipe_steps_json";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_IMAGE = "image";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    @DefaultValue("0")
    public static final String COLUMN_SHOW_ON_WIDGET = "show_on_widget";

    public static final long INVALID_RECIPE_ID = -1;
}