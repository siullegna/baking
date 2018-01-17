package com.hap.baking.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hap.baking.R;
import com.hap.baking.db.RecipeContract;
import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.util.SessionPreferences;

/**
 * Created by luis on 12/18/17.
 */

public class BakingWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingListFactoryProvider(this.getApplicationContext(), intent);
    }
}

class BakingListFactoryProvider implements RemoteViewsService.RemoteViewsFactory {
    private final SessionPreferences sessionPreferences;
    private final Context context;
    private final int widgetId;
    private Recipe recipe;

    BakingListFactoryProvider(Context context, Intent intent) {
        this.context = context;
        this.sessionPreferences = new SessionPreferences(context);
        this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final String recipeId = String.valueOf(sessionPreferences.getInt(context.getString(R.string.sp_recipe_prefix, widgetId)));
        final Uri queryUri = RecipeContract.RecipeEntity.getContentUriByRecipeId(recipeId);
        final String normalizedPathSegment = RecipeContract.RecipeEntity.CONTENT_URI.getLastPathSegment();
        final String[] selectionArguments = new String[]{normalizedPathSegment};
        final Cursor cursor = context.getContentResolver().query(queryUri,
                null,
                null,
                selectionArguments,
                null);

        try {
            if (cursor == null || cursor.getCount() == 0) {
                return;
            }
            cursor.moveToNext();
            recipe = Recipe.fromCursor(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        views.setTextViewText(android.R.id.text1, recipe.getIngredients().get(position).getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}