package com.hap.baking.section.recipe.holder;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luis on 12/5/17.
 */

public class RecipeHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_recipe)
    TextView tvRecipe;
    @BindView(R.id.tv_ingredients)
    TextView tvIngredients;
    @BindView(R.id.tv_steps)
    TextView tvSteps;
    @BindView(R.id.tv_servings)
    TextView tvServings;
    private final Context context;

    public RecipeHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
    }

    public void setupViews(final Recipe recipe) {
        final Resources resources = context.getResources();
        tvRecipe.setText(recipe.getName());
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            tvIngredients.setText(resources.getString(R.string.recipe_ingredients, recipe.getIngredients().size()));
            tvIngredients.setVisibility(View.VISIBLE);
        } else {
            tvIngredients.setVisibility(View.GONE);
        }
        if (recipe.getSteps() != null && !recipe.getSteps().isEmpty()) {
            tvSteps.setText(resources.getString(R.string.recipe_steps, recipe.getSteps().size()));
            tvSteps.setVisibility(View.VISIBLE);
        } else {
            tvSteps.setVisibility(View.GONE);
        }
        if (recipe.getServings() > 0) {
            tvServings.setText(resources.getString(R.string.recipe_servings, recipe.getServings()));
            tvServings.setVisibility(View.VISIBLE);
        } else {
            tvServings.setVisibility(View.GONE);
        }
    }
}
