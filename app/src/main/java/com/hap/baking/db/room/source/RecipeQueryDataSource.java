package com.hap.baking.db.room.source;

import android.database.Cursor;
import android.util.Log;

import com.hap.baking.db.room.AppDatabase;
import com.hap.baking.db.room.dao.RecipeDao;
import com.hap.baking.db.room.entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luis on 12/14/17.
 */
public class RecipeQueryDataSource implements RecipeDataSource {
    private static final String TAG = RecipeQueryDataSource.class.getName();

    private RecipeDao getRecipeDao() {
        return AppDatabase.getInstance().recipeDao();
    }

    @Override
    public void insertRecipes(final Recipe... recipeEntities) {
        getRecipeDao().insertRecipes(recipeEntities);
    }

    @Override
    public void insertRecipes(final ArrayList<Recipe> recipeEntities) {
        getRecipeDao().insertRecipes(recipeEntities);
    }

    @Override
    public void updateRecipes(final Recipe... recipeEntities) {
        getRecipeDao().updateRecipes(recipeEntities);
    }

    @Override
    public void deleteRecipes(final Recipe... recipeEntities) {
        getRecipeDao().deleteRecipes(recipeEntities);
    }

    @Override
    public List<Recipe> loadListOfAllRecipes() {
        return getRecipeDao().loadListOfAllRecipes();
    }

    @Override
    public Cursor loadCursorOfAllRecipes() {
        return getRecipeDao().loadCursorOfAllRecipes();
    }

    @Override
    public Observable<List<Recipe>> loadObservableOfAllRecipes() {
        return Observable.fromCallable(new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() throws Exception {
                return getRecipeDao().loadListOfAllRecipes();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, List<Recipe>>() {
                    @Override
                    public List<Recipe> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.getMessage());
                        return new ArrayList<>();
                    }
                });
    }

    @Override
    public List<Recipe> loadFavorites(final boolean isFavorite) {
        return getRecipeDao().loadFavorites(isFavorite);
    }
}
