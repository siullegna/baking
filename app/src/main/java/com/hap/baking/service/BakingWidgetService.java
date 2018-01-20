package com.hap.baking.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hap.baking.BakingActivity;
import com.hap.baking.BakingWidget;
import com.hap.baking.RecipeActivity;
import com.hap.baking.db.room.entity.Recipe;

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
    private final Context context;
    private final int widgetId;
    private Recipe recipe;

    BakingListFactoryProvider(Context context, Intent intent) {
        this.context = context;
        this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = BakingWidget.getRecipeByWidgetId(widgetId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe != null
                ? recipe.getIngredients().size()
                : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        if (recipe != null) {
            views.setTextViewText(android.R.id.text1, recipe.getIngredients().get(position).getIngredient());
            views.setTextColor(android.R.id.text1, ContextCompat.getColor(context, android.R.color.black));

            final Bundle args = new Bundle();
            args.putParcelable(RecipeActivity.EXTRA_RECIPE_KEY, recipe);
            args.putBoolean(BakingActivity.EXTRA_IS_RECIPE_FROM_WIDGET_KEY, true);
            final Intent recipeIntent = new Intent();
            recipeIntent.putExtras(args);
            views.setOnClickFillInIntent(android.R.id.text1, recipeIntent);
        }
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