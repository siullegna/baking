package com.hap.baking.db.sqlite.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hap.baking.db.RecipeContract;
import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.db.sqlite.DatabaseHelper;

/**
 * Created by luis on 12/14/17.
 */

public class RecipeProvider extends ContentProvider {
    private static final int CODE_ALL_RECIPES = 1111;
    private static final int CODE_SINGLE_RECIPE = 2222;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DatabaseHelper databaseHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, RecipeContract.PATH_RECIPES, CODE_ALL_RECIPES);
        matcher.addURI(authority, RecipeContract.PATH_RECIPES + "/#", CODE_SINGLE_RECIPE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_ALL_RECIPES:
            case CODE_SINGLE_RECIPE:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    final long _id = db.insert(RecipeContract.RecipeEntity.TBL_RECIPE, null, contentValues);
                    if (_id != -1) {
                        rowsInserted++;
                    }
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    context.getContentResolver().notifyChange(uri, null);
                }
                final Recipe recipe = Recipe.fromContentValues(contentValues);
                return ContentUris.withAppendedId(uri, recipe.getId());
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (sUriMatcher.match(uri)) {
            case CODE_ALL_RECIPES:
            case CODE_SINGLE_RECIPE:
                for (final ContentValues contentValues : values) {
                    insert(uri, contentValues);
                }
                return values.length;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_ALL_RECIPES:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }

                cursor = databaseHelper.getReadableDatabase()
                        .query(RecipeContract.RecipeEntity.TBL_RECIPE,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);

                if (cursor == null) {
                    return null;
                }
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;
            case CODE_SINGLE_RECIPE:
                final String normalizedPathSegment = uri.getLastPathSegment();
                final String[] selectionArguments = new String[]{normalizedPathSegment};
                cursor = databaseHelper.getReadableDatabase()
                        .query(RecipeContract.RecipeEntity.TBL_RECIPE,
                                projection,
                                RecipeContract.RecipeEntity._ID + " = ? ",
                                selectionArguments,
                                null,
                                null,
                                sortOrder);
                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        databaseHelper = null;
        super.shutdown();
    }
}
