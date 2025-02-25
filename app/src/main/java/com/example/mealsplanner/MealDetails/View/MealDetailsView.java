package com.example.mealsplanner.MealDetails.View;

import com.example.mealsplanner.model.Meal;

public interface MealDetailsView {
    void showError(String message);

    void displayMealDetails(Meal meal);

    void setFavorite(boolean isFavorite);
}
