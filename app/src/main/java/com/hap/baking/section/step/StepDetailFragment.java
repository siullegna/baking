package com.hap.baking.section.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.baking.R;
import com.hap.baking.db.room.entity.Step;
import com.hap.baking.section.step.adapter.VideoFragmentAdapter;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStepDetailListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment {
    public static final String TAG = StepDetailFragment.class.getName();
    public static final String EXTRA_STEPS_KEY = "com.hap.baking.EXTRA_STEPS_KEY";
    public static final String EXTRA_STEP_POSITION = "com.hap.baking.EXTRA_STEP_POSITION";
    @BindView(R.id.vp_steps)
    ViewPager vpSteps;
    @BindView(R.id.page_indicator)
    TabLayout pageIndicator;

    private ArrayList<Step> steps;
    private int stepPosition;

    private OnStepDetailListener mListener;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public void setStepPosition(final int stepPosition) {
        this.stepPosition = stepPosition;
        vpSteps.setCurrentItem(stepPosition);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param steps        Parameter 1.
     * @param stepPosition Parameter 2.
     * @return A new instance of fragment StepDetailFragment.
     */
    public static StepDetailFragment newInstance(final ArrayList<Step> steps, final int stepPosition) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_STEPS_KEY, steps);
        args.putInt(EXTRA_STEP_POSITION, stepPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList(EXTRA_STEPS_KEY);
            stepPosition = getArguments().getInt(EXTRA_STEP_POSITION);
        }
    }

    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() == null) {
            return view;
        }
        vpSteps.setAdapter(new VideoFragmentAdapter(getActivity().getSupportFragmentManager(), steps));
        vpSteps.setCurrentItem(stepPosition);
        vpSteps.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stepPosition = position;
                if (mListener != null) {
                    mListener.setStepDescription(steps.get(stepPosition).getShortDescription());
                    mListener.setStepPage(stepPosition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pageIndicator.setupWithViewPager(vpSteps, true);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepDetailListener) {
            mListener = (OnStepDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(EXTRA_STEPS_KEY, steps);
        outState.putInt(EXTRA_STEP_POSITION, stepPosition);
        super.onSaveInstanceState(outState);
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
    public interface OnStepDetailListener {
        void setStepDescription(final String shortDescription);

        void setStepPage(final int stepPage);
    }
}
