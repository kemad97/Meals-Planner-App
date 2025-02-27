package com.example.mealsplanner.PlannerScreen.View;

import android.media.Image;
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
import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.ArrayList;
import java.util.List;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {
    private List<WeeklyPlanMeal> meals = new ArrayList<>();
    private OnMealDeleteListener deleteListener;

    public interface OnMealDeleteListener {
        void onMealDelete(WeeklyPlanMeal meal);
    }

    public void setOnMealDeleteListener(OnMealDeleteListener listener) {
        this.deleteListener = listener;
    }



    @NonNull
    @Override
    public PlannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planned_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerAdapter.ViewHolder holder, int position) {
        WeeklyPlanMeal meal = meals.get(position);
        holder.tvMealName.setText(meal.getMealName());
        Glide.with(holder.ivMeal.getContext())
                .load(meal.getMealImage())
                .into(holder.ivMeal);
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onMealDelete(meal);
            }
        });
    }


    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setMeals(List<WeeklyPlanMeal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMeal;
        TextView tvMealName;
        ImageButton btnDelete;

        public ViewHolder(View view) {
            super(view);
            ivMeal = view.findViewById(R.id.ivMeal);
            tvMealName = view.findViewById(R.id.tvMealName);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }
}
