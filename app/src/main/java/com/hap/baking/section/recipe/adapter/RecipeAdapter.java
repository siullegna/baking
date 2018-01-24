package com.hap.baking.section.recipe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.section.recipe.holder.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luis on 12/5/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
    private final ArrayList<Recipe> recipeEntities = new ArrayList<>();
    private final OnRecipeClickListener onRecipeClickListener;

    public RecipeAdapter(final OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public boolean isEmpty() {
        return recipeEntities.isEmpty();
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_recipe, parent, false);
        return new RecipeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {
        final Recipe recipe = recipeEntities.get(position);
        holder.setupViews(recipe);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRecipeClickListener != null) {
                    onRecipeClickListener.onClick(recipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeEntities.size();
    }

    public void addAll(final ArrayList<Recipe> recipeEntities) {
        this.recipeEntities.clear();
        this.recipeEntities.addAll(recipeEntities);
        notifyDataSetChanged();
    }

    public void addAll(final List<Recipe> recipeEntities) {
        this.recipeEntities.clear();
        this.recipeEntities.addAll(recipeEntities);
        notifyDataSetChanged();
    }

    public interface OnRecipeClickListener {
        void onClick(final Recipe recipe);
    }
}
