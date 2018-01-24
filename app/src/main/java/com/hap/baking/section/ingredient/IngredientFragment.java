package com.hap.baking.section.ingredient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.baking.BakingApplication;
import com.hap.baking.R;
import com.hap.baking.db.room.entity.Ingredient;
import com.hap.baking.section.ingredient.adapter.IngredientAdapter;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment {
    public static final String EXTRA_INGREDIENT_KEY = "com.hap.baking.fragment.EXTRA_INGREDIENT_KEY";
    private ArrayList<Ingredient> ingredients;

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredients = getArguments().getParcelableArrayList(EXTRA_INGREDIENT_KEY);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredients list of ingredients.
     * @return A new instance of fragment IngredientFragment.
     */
    public static IngredientFragment newInstance(ArrayList<Ingredient> ingredients) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_INGREDIENT_KEY, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this, view);
        final Context context = BakingApplication.getInstance();
        rvIngredients.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvIngredients.setLayoutManager(layoutManager);
        rvIngredients.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
        final IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        rvIngredients.setAdapter(ingredientAdapter);
        return view;
    }
}
