package com.hap.baking.section.step.holder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luis on 12/6/17.
 */

public class StepHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_step)
    TextView tvStep;
    @BindView(R.id.iv_step_icon)
    ImageView ivStepIcon;

    public StepHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setupViews(Object object, boolean isSelected) {
        final Resources resources = itemView.getResources();
        if (object instanceof ArrayList) {
            tvStep.setText(resources.getString(R.string.recipe_ingredients, ((ArrayList) object).size()));
        } else if (object instanceof Step) {
            final Step step = (Step) object;
            tvStep.setText(step.getShortDescription());
            final Drawable errorImage = ContextCompat.getDrawable(itemView.getContext(), R.mipmap.ic_recipe);
            if (!TextUtils.isEmpty(step.getThumbnailURL()) && errorImage != null) {
                Picasso.with(itemView.getContext())
                        .load(Uri.parse(step.getThumbnailURL()))
                        .error(errorImage)
                        .into(ivStepIcon);
            }
        }

        tvStep.setTextColor(ContextCompat.getColor(itemView.getContext(), isSelected
                ? R.color.colorAccent
                : R.color.colorPrimaryText));
    }
}
