package com.hap.baking.dagger.module;

import com.hap.baking.dagger.scope.ApplicationScope;
import com.hap.baking.db.room.model.ViewModelFactory;
import com.hap.baking.db.room.source.RecipeQueryDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by luis on 12/14/17.
 */
@Module
public class DatabaseModule {
    @Provides
    @ApplicationScope
    protected RecipeQueryDataSource provideRecipeQueryDataSource() {
        return new RecipeQueryDataSource();
    }

    @Provides
    @ApplicationScope
    protected ViewModelFactory provideViewModelFactory(final RecipeQueryDataSource recipeDataSource) {
        return new ViewModelFactory(recipeDataSource);
    }
}
