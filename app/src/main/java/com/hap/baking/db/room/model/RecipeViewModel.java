package com.hap.baking.db.room.model;

import android.arch.lifecycle.ViewModel;

import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.db.room.source.RecipeDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by luis on 12/14/17.
 */

public class RecipeViewModel extends ViewModel {
    private final RecipeDataSource recipeDataSource;

    RecipeViewModel(RecipeDataSource recipeDataSource) {
        this.recipeDataSource = recipeDataSource;
    }

    public Completable insertRecipes(final ArrayList<Recipe> recipeEntities) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                recipeDataSource.insertRecipes(recipeEntities);
            }
        });
    }

    public Observable<List<Recipe>> loadListOfAllRecipes() {
        return Observable.fromCallable(new Callable<List<Recipe>>() {
            @Override
            public List<Recipe> call() throws Exception {
                return recipeDataSource.loadListOfAllRecipes();
            }
        });
    }

}
