package com.hap.baking.section.recipe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hap.baking.R;

/**
 * Created by luis on 12/11/17.
 */

public class RecipeItemDecoration extends RecyclerView.ItemDecoration {
    private final boolean isTablet;

    public RecipeItemDecoration(final Context context) {
        isTablet = context.getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final Resources resources = parent.getResources();
        final int left = resources.getDimensionPixelSize(R.dimen.recipe_padding);
        final int top = resources.getDimensionPixelSize(R.dimen.recipe_padding);
        final int right = resources.getDimensionPixelSize(R.dimen.recipe_padding);
        final int bottom = resources.getDimensionPixelSize(R.dimen.recipe_padding);
        if (!isTablet) {
            outRect.left = left;
            outRect.top = position == 0
                    ? top
                    : top / 2;
            outRect.right = right;
            outRect.bottom = bottom / 2;
        } else {
            switch (position % 3) {
                case 0:
                    outRect.left = left;
                    outRect.top = position < 3
                            ? top
                            : top / 2;
                    outRect.right = right / 2;
                    outRect.bottom = bottom / 2;
                    break;
                case 1:
                    outRect.left = left / 2;
                    outRect.top = position < 3
                            ? top
                            : top / 2;
                    outRect.right = right / 2;
                    outRect.bottom = bottom / 2;
                    break;
                case 2:
                    outRect.left = left / 2;
                    outRect.top = position < 3
                            ? top
                            : top / 2;
                    outRect.right = right;
                    outRect.bottom = bottom / 2;
                    break;
            }

        }
    }
}
