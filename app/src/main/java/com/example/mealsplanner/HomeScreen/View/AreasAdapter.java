package com.example.mealsplanner.HomeScreen.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Area;

import java.util.List;

public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.AreaViewHolder> {
    private final List<Area> areas;
    private OnAreaClickListener listener;

    public AreasAdapter(List<Area> areas, OnAreaClickListener listener) {
        this.areas = areas;
        this.listener = listener;
    }

    public interface OnAreaClickListener {
        void onAreaClick(Area area);
    }

    public AreasAdapter(List<Area> areas) {
        this.areas = areas;
    }


    public OnAreaClickListener getListener() {
        return listener;
    }


    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.bind(area);
        getSetOnTouchListener(holder, area);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void getSetOnTouchListener(AreaViewHolder holder, Area area) {
        holder.itemView.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Multiple animations combined
                    view.animate()
                            .scaleX(0.90f)
                            .scaleY(0.90f)
                            .translationY(-20f)  // Slight upward movement
                            .setDuration(150)
                            .withLayer()
                            .setInterpolator(new OvershootInterpolator(1.5f))
                            .start();

                    // Add elevation (shadow)
                    view.setElevation(20f);

                    // Optional: Add rotation effect
                    view.animate()
                            .rotationX(5f)
                            .setDuration(150)
                            .start();
                    break;

                case MotionEvent.ACTION_UP:
                    // Reset with bounce effect
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .translationY(0f)
                            .rotationX(0f)
                            .setDuration(300)
                            .setInterpolator(new BounceInterpolator())
                            .withLayer()
                            .withEndAction(() -> {
                                view.setElevation(0f);
                                if (listener != null) {
                                    listener.onAreaClick(area);
                                }
                            })
                            .start();
                    break;

                case MotionEvent.ACTION_CANCEL:
                    // Smooth reset
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .translationY(0f)
                            .rotationX(0f)
                            .setDuration(200)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .withLayer()
                            .withEndAction(() -> view.setElevation(0f))
                            .start();
                    break;
            }
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return areas.size();
    }

    public void setListener(OnAreaClickListener listener) {
        this.listener = listener;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvArea;

        public AreaViewHolder(View itemView) {

            super(itemView);
            tvArea = itemView.findViewById(R.id.tv_area);
        }

        public void bind(final Area area) {
            if (area != null) {
                tvArea.setText(area.getName());
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onAreaClick(area);
                    }
                });
            }
        }


    }
}
