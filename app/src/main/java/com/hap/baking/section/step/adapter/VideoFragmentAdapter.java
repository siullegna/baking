package com.hap.baking.section.step.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hap.baking.db.room.entity.Step;
import com.hap.baking.section.step.VideoFragment;

import java.util.ArrayList;

/**
 * Created by luis on 12/12/17.
 */

public class VideoFragmentAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Step> steps;

    public VideoFragmentAdapter(FragmentManager fm, ArrayList<Step> steps) {
        super(fm);
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        return VideoFragment.newInstance(steps.get(position));
    }

    @Override
    public int getCount() {
        return steps != null
                ? steps.size()
                : 0;
    }
}
