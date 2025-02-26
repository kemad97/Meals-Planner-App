package com.example.mealsplanner.ShowMeals.View;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public class ShowMealsAdapter extends RecyclerView.Adapter<ShowMealsAdapter.MealViewHolder> {
    private List<Meal> meals;
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
        void onFavoriteClick(Meal meal, boolean isFavorite);
    }

    public ShowMealsAdapter(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals != null ? meals.size() : 0;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMealImage;
        private final TextView tvMealName;
        private final ImageButton btnFavorite;
        private boolean isFavorite;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivMealImage);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }

        void bind(Meal meal) {
            tvMealName.setText(meal.getName());

            Glide.with(itemView.getContext())
                    .load(meal.getImageUrl())
                    .into(ivMealImage);

            // Set initial favorite state
            isFavorite = meal.isFavorite();
            updateFavoriteIcon();

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealClick(meal);
                }
            });

            btnFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    isFavorite = !isFavorite;
                    meal.setFavorite(isFavorite);
                    updateFavoriteIcon();
                    listener.onFavoriteClick(meal, isFavorite);
                }
            });
        }

        private void updateFavoriteIcon() {
            btnFavorite.setImageResource(isFavorite ?
                    R.drawable.ic_favorite_filled :
                    R.drawable.ic_favorite);
        }
    }
}