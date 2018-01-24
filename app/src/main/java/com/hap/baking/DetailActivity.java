package com.hap.baking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hap.baking.db.room.entity.Ingredient;
import com.hap.baking.db.room.entity.Step;
import com.hap.baking.section.ingredient.IngredientFragment;
import com.hap.baking.section.step.StepDetailFragment;
import com.hap.baking.section.step.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseAppActivity implements StepDetailFragment.OnStepDetailListener, VideoFragment.OnVideoFragmentListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private int stepPosition;

    private String getStepTitle() {
        return steps.get(stepPosition).getShortDescription();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final String title;
        if (getIntent().getParcelableArrayListExtra(IngredientFragment.EXTRA_INGREDIENT_KEY) != null) {
            ingredients = getIntent().getParcelableArrayListExtra(IngredientFragment.EXTRA_INGREDIENT_KEY);
            title = getResources().getString(R.string.ingredients);
        } else if (getIntent().getParcelableArrayListExtra(StepDetailFragment.EXTRA_STEPS_KEY) != null) {
            steps = getIntent().getParcelableArrayListExtra(StepDetailFragment.EXTRA_STEPS_KEY);
            stepPosition = getIntent().getIntExtra(StepDetailFragment.EXTRA_STEP_POSITION, 0);
            title = getStepTitle();
        } else {
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        if (savedInstanceState == null) {
            final Fragment fragment;
            final FragmentManager fm = getSupportFragmentManager();
            if (ingredients != null) {
                // add ingredients fragment
                fragment = IngredientFragment.newInstance(ingredients);
            } else {
                // add steps fragment
                fragment = StepDetailFragment.newInstance(steps, stepPosition);
            }
            fm.beginTransaction()
                    .add(R.id.detail_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setStepDescription(String shortDescription) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(shortDescription);
        }
    }

    @Override
    public void setStepPage(int stepPage) {
        this.stepPosition = stepPage;
    }

    @Override
    public void onCloseVideo() {
        onBackPressed();
    }
}
