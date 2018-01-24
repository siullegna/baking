package com.hap.baking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hap.baking.db.room.entity.Ingredient;
import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.section.ingredient.IngredientFragment;
import com.hap.baking.section.step.StepDetailFragment;
import com.hap.baking.section.step.StepListFragment;
import com.hap.baking.section.step.VideoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends BaseAppActivity implements StepListFragment.OnStepFragmentListener, StepDetailFragment.OnStepDetailListener,
        VideoFragment.OnVideoFragmentListener {
    private static final String TAG = RecipeActivity.class.getName();
    public static final String EXTRA_RECIPE_KEY = "com.hap.baking.RecipeActivity.EXTRA_RECIPE_KEY";
    public static final String EXTRA_IS_TWO_PANE_KEY = "com.hap.baking.RecipeActivity.EXTRA_IS_TWO_PANE_KEY";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.detail_fragment_container)
    FrameLayout detailFragmentContainer;
    private Recipe recipe;
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            recipe = getIntent().getParcelableExtra(EXTRA_RECIPE_KEY);
        } else {
            recipe = savedInstanceState.getParcelable(EXTRA_RECIPE_KEY);
        }

        if (recipe == null) {
            Toast.makeText(this, R.string.error_cannot_load_recipe, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipe.getName());
        }

        final FragmentManager fm = getSupportFragmentManager();
        final StepListFragment stepListFragment = (StepListFragment) fm.findFragmentById(R.id.steps_fragment);
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE_KEY, recipe);
        stepListFragment.setArguments(args);
        if (detailFragmentContainer != null) {
            isTwoPane = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // setup new fragment
        } else {
            isTwoPane = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_RECIPE_KEY, recipe);
        outState.putBoolean(EXTRA_IS_TWO_PANE_KEY, isTwoPane);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onIngredientClick(ArrayList<Ingredient> ingredients) {
        if (isTwoPane) {
            final FragmentManager fm = getSupportFragmentManager();
            // add ingredients fragment
            final Fragment fragment = IngredientFragment.newInstance(ingredients);
            fm.beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        } else {
            final Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putParcelableArrayListExtra(IngredientFragment.EXTRA_INGREDIENT_KEY, ingredients);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onStepClick(int stepPosition) {
        if (isTwoPane) {
            final FragmentManager fm = getSupportFragmentManager();
            final StepDetailFragment fragment;
            if (fm.findFragmentByTag(StepDetailFragment.TAG) == null) {
                // add steps fragment
                fragment = StepDetailFragment.newInstance(recipe.getSteps(), stepPosition);
                fm.beginTransaction()
                        .replace(R.id.detail_fragment_container, fragment, StepDetailFragment.TAG)
                        .commit();
            } else {
                fragment = (StepDetailFragment) fm.findFragmentByTag(StepDetailFragment.TAG);
                fragment.setStepPosition(stepPosition);
            }
        } else {
            final Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putParcelableArrayListExtra(StepDetailFragment.EXTRA_STEPS_KEY, recipe.getSteps());
            detailIntent.putExtra(StepDetailFragment.EXTRA_STEP_POSITION, stepPosition);
            startActivity(detailIntent);
        }
    }

    @Override
    public void setStepDescription(String shortDescription) {
        Log.d(TAG, "no need to update anything here");
    }

    @Override
    public void setStepPage(int stepPage) {
        final FragmentManager fm = getSupportFragmentManager();
        final StepListFragment stepListFragment = (StepListFragment) fm.findFragmentById(R.id.steps_fragment);
        stepListFragment.setSelectedItem(++stepPage);
    }

    @Override
    public void onCloseVideo() {
        Log.d(TAG, "nothing happen");
    }
}
