package com.hap.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.hap.baking.db.RecipeContract;
import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.service.BakingWidgetService;
import com.hap.baking.util.SessionPreferences;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {
    public static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        // setup title of the recipe
        final Recipe recipe = getRecipeByWidgetId(appWidgetId);
        if (recipe != null) {
            views.setTextViewText(R.id.tv_recipe, recipe.getName());
        } else {
            views.setTextViewText(R.id.tv_recipe, context.getString(R.string.recipe_title));
        }

        // Intent for the service - Add the app widget ID to the intent extras.
        final Intent intent = new Intent(context, BakingWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.lv_ingredients, intent);

        // Set the BakingActivity intent to launch when clicked
        final Intent appIntent = new Intent(context, BakingActivity.class);
        final PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_ingredients, appPendingIntent);
        views.setOnClickPendingIntent(R.id.root, appPendingIntent);

        views.setEmptyView(R.id.lv_ingredients, R.id.tv_no_ingredient);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // we delete the shared pref key
            ConfigurationBakingWidget.deleteWidgetRecipeByKey(BakingApplication.getInstance().getString(R.string.sp_recipe_prefix, appWidgetId));
        }
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static Recipe getRecipeByWidgetId(final int widgetId) {
        final SessionPreferences sessionPreferences = BakingApplication.getSessionPreferences();
        final Integer recipeId = sessionPreferences.getInt(BakingApplication.getInstance().getString(R.string.sp_recipe_prefix, widgetId));
        if (recipeId <= 0) {
            return null;
        }
        final Uri queryUri = RecipeContract.RecipeEntity.getContentUriByRecipeId(recipeId.toString());
        final String normalizedPathSegment = RecipeContract.RecipeEntity.CONTENT_URI.getLastPathSegment();
        final String[] selectionArguments = new String[]{normalizedPathSegment};
        final Cursor cursor = BakingApplication.getInstance().getContentResolver().query(queryUri,
                null,
                null,
                selectionArguments,
                null);

        try {
            if (cursor == null || cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToNext();
            return Recipe.fromCursor(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}