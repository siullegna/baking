package com.hap.baking;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hap.baking.db.room.entity.Recipe;

/**
 * Created by luis on 12/16/17.
 */

public class ConfigurationBakingWidget extends BakingActivity {
    private static final String TAG = ConfigurationBakingWidget.class.getName();
    private int widgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        if (savedInstanceState == null) {
            final Bundle args = getIntent().getExtras();
            if (args == null) {
                return;
            }
            widgetId = args.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        } else {
            widgetId = savedInstanceState.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Log.d(TAG, "WidgetId" + widgetId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Recipe recipe) {
        final Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        // we store the selected recipe Id, using the widget id
        // Let's say the widgetId = 1 and  recipe.id = 4
        // key = recipe_1, recipeId = 4
        // then we can read the value for that specific widgetId to load the information we need
        saveWidgetRecipeId(getString(R.string.sp_recipe_prefix, widgetId), recipe.getId());

        setResult(RESULT_OK, resultValue);
        finish();
    }

    public static void saveWidgetRecipeId(final String key, final int recipeId) {
        BakingApplication.getSessionPreferences().putInt(key, recipeId);
    }

    public static void deleteWidgetRecipeByKey(final String key) {
        BakingApplication.getSessionPreferences().deleteKey(key);
    }
}
