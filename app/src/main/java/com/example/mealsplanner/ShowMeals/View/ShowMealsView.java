package com.example.mealsplanner.ShowMeals.View;

import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface ShowMealsView {
    void showLoading();
    void hideLoading();
    void showMeals(List<Meal> meals);
    void showError(String message);
    void updateFavoriteStatus(Meal meal, boolean isFavorite);
}
