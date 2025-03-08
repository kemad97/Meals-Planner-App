package com.example.mealsplanner.MealDetails.View;

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
import com.example.mealsplanner.HomeScreen.View.CategoriesAdapter;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>
{
    private final List<Meal.Ingredient> ingredientList;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Meal.Ingredient ingredient);
    }

    public void setListener(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public IngredientsAdapter(List<Meal.Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public IngredientsAdapter(List<Meal.Ingredient> ingredientList, OnIngredientClickListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.IngredientViewHolder holder, int position) {
        Meal.Ingredient ingredient=ingredientList.get(position);
        holder.tvIngredientName.setText(ingredient.getName());
        holder.tvIngredientMeasure.setText(ingredient.getMeasure());

        Glide.with(holder.itemView.getContext())
                .load(ingredient.getImageUrl())
                .into(holder.ivIngredientImage);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIngredientClick(ingredient);
            }
        });
        getSetOnTouchListener(holder,ingredient);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void getSetOnTouchListener(@NonNull IngredientsAdapter.IngredientViewHolder holder, Meal.Ingredient ingredient) {
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
                                    listener.onIngredientClick(ingredient);
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
        return ingredientList.size();
    }


    public static class IngredientViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvIngredientName;
        TextView tvIngredientMeasure;
        ImageView ivIngredientImage;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvIngredientMeasure = itemView.findViewById(R.id.tvMeasurement);
            ivIngredientImage = itemView.findViewById(R.id.ivIngredientImage);
        }
    }
}
