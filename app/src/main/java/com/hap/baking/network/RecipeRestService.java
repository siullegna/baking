package com.hap.baking.network;

import com.hap.baking.db.room.entity.Recipe;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by luis on 12/4/17.
 */

public class RecipeRestService {
    private final RecipeRestApi recipeRestApi;

    public RecipeRestService(RecipeRestApi recipeRestApi) {
        this.recipeRestApi = recipeRestApi;
    }

    public Observable<ArrayList<Recipe>> getRecipes() {
        final Observable<ArrayList<Recipe>> recipesObservable = recipeRestApi.getRecipes();
        return recipesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
