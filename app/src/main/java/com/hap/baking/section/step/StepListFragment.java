package com.hap.baking.section.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.baking.BakingApplication;
import com.hap.baking.R;
import com.hap.baking.RecipeActivity;
import com.hap.baking.db.room.entity.Ingredient;
import com.hap.baking.db.room.entity.Recipe;
import com.hap.baking.db.room.entity.Step;
import com.hap.baking.section.step.adapter.StepAdapter;
import com.hap.baking.util.ListItemFactory;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStepFragmentListener} interface
 * to handle interaction events.
 */
public class StepListFragment extends Fragment implements StepAdapter.OnItemClickListener {
    private static final String EXTRA_SELECTED_ITEM_KEY = "com.hap.baking.fragment.EXTRA_SELECTED_ITEM_KEY";
    @BindView(R.id.rv_steps)
    RecyclerView rvSteps;

    private Recipe recipe;
    private boolean isTwoPane;
    private int selectedItem;
    private ArrayList<Object> steps;
    private StepAdapter stepAdapter;

    private OnStepFragmentListener mListener;

    public StepListFragment() {
        // Required empty public constructor
    }

    public void setSelectedItem(final int selectedItem) {
        this.selectedItem = selectedItem;
        stepAdapter.setSelected(selectedItem);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) rvSteps.getLayoutManager();
        if (selectedItem < layoutManager.findFirstCompletelyVisibleItemPosition() || selectedItem > layoutManager.findLastCompletelyVisibleItemPosition()) {
            rvSteps.scrollToPosition(selectedItem);
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            recipe = args.getParcelable(RecipeActivity.EXTRA_RECIPE_KEY);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTwoPane = getResources().getBoolean(R.bool.isTablet);

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(EXTRA_SELECTED_ITEM_KEY);
        } else if (isTwoPane) {
            selectedItem = 0;   // default on tablet
        } else {
            selectedItem = -1;  // no selection in phone
        }
    }

    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);

        final Context context = BakingApplication.getInstance();
        rvSteps.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvSteps.setLayoutManager(layoutManager);
        rvSteps.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
        stepAdapter = new StepAdapter(this);
        stepAdapter.setSelected(selectedItem);
        rvSteps.setAdapter(stepAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        steps = ListItemFactory.getStepsFromRecipe(recipe);
        stepAdapter.addAll(steps);
        if (isTwoPane) {
            onClick(selectedItem);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(EXTRA_SELECTED_ITEM_KEY, selectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(int position) {
        if (mListener != null) {
            final Object object = steps.get(position);
            if (object instanceof ArrayList) {
                mListener.onIngredientClick((ArrayList<Ingredient>) object);
            } else if (object instanceof Step) {
                mListener.onStepClick(position - 1);
            }
        }
        if (isTwoPane) {
            selectedItem = position;
            stepAdapter.setSelected(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepFragmentListener) {
            mListener = (OnStepFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepFragmentListener {
        void onIngredientClick(ArrayList<Ingredient> ingredients);

        void onStepClick(int stepPosition);
    }
}
