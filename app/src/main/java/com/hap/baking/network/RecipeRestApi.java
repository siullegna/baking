package com.hap.baking.network;

import com.hap.baking.db.room.entity.Recipe;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by luis on 12/4/17.
 */

public interface RecipeRestApi {
    @GET("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
    Observable<ArrayList<Recipe>> getRecipes();
}
