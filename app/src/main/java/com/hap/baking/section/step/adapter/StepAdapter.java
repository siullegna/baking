package com.hap.baking.section.step.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hap.baking.R;
import com.hap.baking.section.step.holder.StepHolder;

import java.util.ArrayList;

/**
 * Created by luis on 12/6/17.
 */

public class StepAdapter extends RecyclerView.Adapter<StepHolder> {
    private final ArrayList<Object> steps = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;
    private int selectedItem = -1;

    public StepAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.holder_step, parent, false);
        return new StepHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepHolder holder, final int position) {
        final Object object = steps.get(position);
        holder.setupViews(object, selectedItem == position);
        setupClickListener(holder, position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    private void setupClickListener(final StepHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });
    }

    public void addAll(final ArrayList<Object> steps) {
        this.steps.clear();
        this.steps.addAll(steps);
        notifyDataSetChanged();
    }

    public void setSelected(final int selectedItem) {
        final int oldSelectedItem = this.selectedItem;
        this.selectedItem = selectedItem;
        notifyItemChanged(oldSelectedItem);
        notifyItemChanged(selectedItem);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
