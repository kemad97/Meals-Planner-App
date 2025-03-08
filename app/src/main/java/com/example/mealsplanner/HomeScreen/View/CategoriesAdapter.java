package com.example.mealsplanner.HomeScreen.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<CategoriesItem> categories;
    private OnCategoryClickListener listener;

    public CategoriesAdapter(List<CategoriesItem> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(CategoriesItem category);
    }

    public CategoriesAdapter() {
    }


    public CategoriesAdapter(List<CategoriesItem> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoriesItem category = categories.get(position);
        holder.tvCategoryName.setText(category.getStrCategory());
        Glide.with(holder.itemView.getContext())
                .load(category.getStrCategoryThumb())
                .into(holder.ivCategoryImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
        // Add enhanced touch animation
        getSetOnTouchListener(holder, category);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void getSetOnTouchListener(@NonNull CategoryViewHolder holder, CategoriesItem category) {
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
                                    listener.onCategoryClick(category);
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
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView ivCategoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
        }
    }
}