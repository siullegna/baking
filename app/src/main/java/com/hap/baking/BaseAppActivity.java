package com.hap.baking;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hap.baking.db.room.model.RecipeViewModel;
import com.hap.baking.db.room.model.ViewModelFactory;
import com.hap.baking.network.RecipeRestService;

import javax.inject.Inject;

/**
 * Created by luis on 12/14/17.
 */

public abstract class BaseAppActivity extends AppCompatActivity {
    @Inject
    protected RecipeRestService recipeRestService;
    @Inject
    protected ViewModelFactory viewModelFactory;
    protected RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BakingApplication.getInstance().getBakingAppComponent().inject(this);

        recipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel.class);
    }
}
