package com.example.mealsplanner.MealDetails.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>
{
    private final List<Meal.Ingredient> ingredientList;

    public IngredientsAdapter(List<Meal.Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
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
