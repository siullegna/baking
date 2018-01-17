package com.hap.baking.db.room.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.hap.baking.db.room.source.RecipeDataSource;

/**
 * Created by luis on 12/14/17.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final RecipeDataSource recipeDataSource;

    public ViewModelFactory(RecipeDataSource recipeDataSource) {
        this.recipeDataSource = recipeDataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(recipeDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
