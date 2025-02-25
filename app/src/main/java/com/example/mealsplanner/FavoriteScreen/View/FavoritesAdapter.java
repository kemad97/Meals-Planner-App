package com.example.mealsplanner.FavoriteScreen.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.FavoriteMeal;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    List<FavoriteMeal> meals = new ArrayList<>();
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(FavoriteMeal meal);
    }

    public FavoritesAdapter(List<FavoriteMeal> meals, OnFavoriteClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {
        FavoriteMeal meal = meals.get(position);
        holder.bind(meal);

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }


    public void setMeals(List<FavoriteMeal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealImage;
        TextView tvMealName;
        AppCompatButton btnRemoveFav;


        ViewHolder(View itemView) {
            super(itemView);
            ivMealImage = itemView.findViewById(R.id.ivMealImage);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            btnRemoveFav = itemView.findViewById(R.id.btnRemoveFav);

        }

        void bind(FavoriteMeal meal) {
            tvMealName.setText(meal.getName());
            Glide.with(itemView.getContext())
                    .load(meal.getImageUrl())
                    .into(ivMealImage);

            btnRemoveFav.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavoriteClick(meal);
                }
            });
        }

    }

}
