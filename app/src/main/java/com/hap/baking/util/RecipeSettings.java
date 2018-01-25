package com.hap.baking.util;

/**
 * Created by luis on 12/4/17.
 */

public class RecipeSettings {
    private RecipeSettings() {

    }

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static String getBaseUrl() {
        return RecipeSettings.BASE_URL;
    }
}
