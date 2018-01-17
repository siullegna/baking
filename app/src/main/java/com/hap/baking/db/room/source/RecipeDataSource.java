package com.hap.baking.db.room.source;

import android.database.Cursor;

import com.hap.baking.db.room.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by luis on 12/14/17.
 */

public interface RecipeDataSource {
    void insertRecipes(final Recipe... recipeEntities);

    void insertRecipes(final ArrayList<Recipe> recipeEntities);

    void updateRecipes(final Recipe... recipeEntities);

    void deleteRecipes(final Recipe... recipeEntities);

    List<Recipe> loadListOfAllRecipes();

    Cursor loadCursorOfAllRecipes();

    Observable<List<Recipe>> loadObservableOfAllRecipes();

    List<Recipe> loadFavorites(final boolean isFavorite);
}
