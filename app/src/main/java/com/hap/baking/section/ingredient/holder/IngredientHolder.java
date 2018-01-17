package com.hap.baking.section.ingredient.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luis on 12/9/17.
 */

public class IngredientHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_ingredient)
    TextView tvIngredient;
    @BindView(R.id.tv_measure)
    TextView tvMeasure;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;

    public IngredientHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupViews(final Ingredient ingredient) {
        tvIngredient.setText(ingredient.getIngredient());
        if (TextUtils.isEmpty(ingredient.getMeasure())) {
            tvMeasure.setVisibility(View.GONE);
        } else {
            tvMeasure.setVisibility(View.VISIBLE);
            tvMeasure.setText(ingredient.getMeasure());
        }
        if (ingredient.getQuantity() <= 0) {
            tvQuantity.setVisibility(View.GONE);
        } else {
            tvQuantity.setVisibility(View.VISIBLE);
            tvQuantity.setText(String.valueOf(ingredient.getQuantity()));
        }
    }
}
