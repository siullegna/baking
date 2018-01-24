package com.hap.baking.util;

import com.hap.baking.db.room.entity.Recipe;

import java.util.ArrayList;

/**
 * Created by luis on 12/6/17.
 */

public class ListItemFactory {
    public static ArrayList<Object> getStepsFromRecipe(final Recipe recipe) {
        final ArrayList<Object> steps = new ArrayList<>();

        steps.add(recipe.getIngredients());
        steps.addAll(recipe.getSteps());

        return steps;
    }
}
