package com.hap.baking.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by luis on 12/14/17.
 */

public class RecipeContract {
    private RecipeContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.hap.baking";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPES = "recipes";

    public static final class RecipeEntity implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPES)
                .build();
        public static final String TBL_RECIPE = "recipe";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_STEPS = "steps";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static Uri getContentUriByRecipeId(final String recipeId) {
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_RECIPES)
                    .appendPath(recipeId)
                    .build();
        }
    }
}