package com.hap.baking.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hap.baking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by luis on 12/5/17.
 */

public class EmptyScreenView extends LinearLayout {
    @BindView(R.id.empty_screen_message)
    TextView emptyScreenMessage;

    public EmptyScreenView(Context context) {
        this(context, null);
    }

    public EmptyScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.view_empty_screen, this);
        ButterKnife.bind(this, view);
    }

    public void setupEmptyScreen(final ScreenType screenType) {
        if (screenType == null) {
            setVisibility(VISIBLE);
            emptyScreenMessage.setText(R.string.empty_screen_unknown_error);
            return;
        }

        switch (screenType) {
            case GONE:
                this.setVisibility(GONE);
                break;
            case EMPTY_RECIPES:
                setVisibility(VISIBLE);
                emptyScreenMessage.setText(R.string.empty_screen_no_recipes);
                break;
            case NETWORK_ERROR:
                setVisibility(VISIBLE);
                emptyScreenMessage.setText(R.string.empty_screen_network_error);
                break;
        }
    }

    public enum ScreenType {
        GONE,
        EMPTY_RECIPES,
        NETWORK_ERROR
    }
}
