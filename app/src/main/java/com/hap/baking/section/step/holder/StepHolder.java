package com.hap.baking.section.step.holder;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luis on 12/6/17.
 */

public class StepHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_step)
    TextView tvStep;

    public StepHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupViews(Object object, boolean isSelected) {
        final Resources resources = itemView.getResources();
        if (object instanceof ArrayList) {
            tvStep.setText(resources.getString(R.string.recipe_ingredients, ((ArrayList) object).size()));
        } else if (object instanceof Step) {
            tvStep.setText(((Step) object).getShortDescription());
        }

        tvStep.setTextColor(ContextCompat.getColor(itemView.getContext(), isSelected
                ? R.color.colorAccent
                : R.color.colorPrimaryText));
    }
}
