package com.hap.baking.section.ingredient.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Ingredient;
import com.hap.baking.section.ingredient.holder.IngredientHolder;

import java.util.ArrayList;

/**
 * Created by luis on 12/9/17.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientHolder> {
    private final ArrayList<Ingredient> ingredients;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.holder_ingredient, parent, false);
        return new IngredientHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        holder.setupViews(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
