package com.hap.baking.db.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.db.RecipeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luis on 12/14/17.
 */
@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(final Recipe... recipeEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(final ArrayList<Recipe> recipeEntities);

    @Update
    void updateRecipes(final Recipe... recipeEntities);

    @Delete
    void deleteRecipes(final Recipe... recipeEntities);

    @Query("SELECT * FROM " + RecipeContract.RecipeEntity.TBL_RECIPE)
    List<Recipe> loadListOfAllRecipes();

    @Query("SELECT * FROM " + RecipeContract.RecipeEntity.TBL_RECIPE)
    Cursor loadCursorOfAllRecipes();

    @Query("SELECT * FROM " + RecipeContract.RecipeEntity.TBL_RECIPE + " WHERE " + RecipeContract.RecipeEntity.COLUMN_IS_FAVORITE + " = :isFavorite")
    List<Recipe> loadFavorites(final boolean isFavorite);
}