package com.hap.baking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.section.recipe.RecipeItemDecoration;
import com.hap.baking.section.recipe.adapter.RecipeAdapter;
import com.hap.baking.widget.EmptyScreenView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BakingActivity extends BaseAppActivity implements RecipeAdapter.OnRecipeClickListener {
    private static final String TAG = BakingActivity.class.getName();
    public static final String EXTRA_IS_RECIPE_FROM_WIDGET_KEY = "com.hap.baking.BakingActivity.EXTRA_IS_RECIPE_FROM_WIDGET_KEY";
    public static final String EXTRA_IS_FIRST_LOAD_KEY = "com.hap.baking.BakingActivity.EXTRA_IS_FIRST_LOAD_KEY";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;
    @BindView(R.id.empty_screen_view)
    EmptyScreenView emptyScreenView;

    private RecipeAdapter recipeAdapter;
    private boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_recipes);
        }

        if (savedInstanceState != null) {
            isFirstLoad = savedInstanceState.getBoolean(EXTRA_IS_FIRST_LOAD_KEY);
        }

        rvRecipes.setHasFixedSize(true);
        final int cols = getResources().getBoolean(R.bool.isTablet)
                ? 3
                : 1;

        final GridLayoutManager layoutManager = new GridLayoutManager(this, cols, LinearLayoutManager.VERTICAL, false);
        rvRecipes.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(this);
        rvRecipes.setAdapter(recipeAdapter);

        rvRecipes.addItemDecoration(new RecipeItemDecoration(this));

        refresh.setColorSchemeResources(R.color.colorAccent);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRecipes();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // try to load from the db first, if we do have something, we just load it, if we don't have anything, at the same time we're trying to get data from the api, if we get something
        // we will load from there, otherwise if we don't have anything in the db, and we don't get response, we show empty screen.
        // notice that even though we display something from the db, we still try to load from the api, so in the case we get more new data that we don't have yet.
        loadRecipesFromDb();
        if (isFirstLoad) {
            showLoader();
            emptyScreenView.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.GONE);
            // load from db if there's something
            getRecipes();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Bundle args = getIntent().getExtras();
        if (args != null) {
            final boolean isWidget = args.getBoolean(EXTRA_IS_RECIPE_FROM_WIDGET_KEY, false);
            final Recipe recipe = args.getParcelable(RecipeActivity.EXTRA_RECIPE_KEY);
            if (isWidget && recipe != null) {
                onClick(recipe);
                getIntent().putExtra(EXTRA_IS_RECIPE_FROM_WIDGET_KEY, false);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onClick(Recipe recipe) {
        final Intent recipeIntent = new Intent(this, RecipeActivity.class);
        recipeIntent.putExtra(RecipeActivity.EXTRA_RECIPE_KEY, recipe);
        startActivity(recipeIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_IS_FIRST_LOAD_KEY, isFirstLoad);
        super.onSaveInstanceState(outState);
    }

    private void loadRecipesFromDb() {
        compositeDisposable.add(recipeViewModel.loadListOfAllRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> recipeEntities) throws Exception {
                        if (recipeEntities.isEmpty()) {
                            return;
                        }
                        recipeAdapter.addAll(recipeEntities);
                        if (recipeAdapter.isEmpty()) {
                            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.NETWORK_ERROR);
                            rvRecipes.setVisibility(View.GONE);
                        } else {
                            emptyScreenView.setVisibility(View.GONE);
                            rvRecipes.setVisibility(View.VISIBLE);
                        }
                        hideLoader();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                    }
                }));
    }

    private void showLoader() {
        loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loader.setVisibility(View.GONE);
        refresh.setRefreshing(false);
    }

    private void getRecipes() {
        recipeRestService.getRecipes()
                .subscribe(new Consumer<ArrayList<Recipe>>() {
                    @Override
                    public void accept(ArrayList<Recipe> recipeEntities) throws Exception {
                        isFirstLoad = false;
                        recipeAdapter.addAll(recipeEntities);
                        if (recipeAdapter.isEmpty()) {
                            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.NETWORK_ERROR);
                            rvRecipes.setVisibility(View.GONE);
                        } else {
                            emptyScreenView.setVisibility(View.GONE);
                            rvRecipes.setVisibility(View.VISIBLE);
                        }

                        insertRecipes(recipeEntities);
                        hideLoader();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (recipeAdapter.isEmpty()) {
                            emptyScreenView.setupEmptyScreen(EmptyScreenView.ScreenType.NETWORK_ERROR);
                            rvRecipes.setVisibility(View.GONE);
                        } else {
                            emptyScreenView.setVisibility(View.GONE);
                            rvRecipes.setVisibility(View.VISIBLE);
                        }
                        hideLoader();
                    }
                });
    }

    private void insertRecipes(final ArrayList<Recipe> recipeEntities) {
        compositeDisposable.add(recipeViewModel.insertRecipes(recipeEntities)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "run()");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                    }
                }));
    }
}
