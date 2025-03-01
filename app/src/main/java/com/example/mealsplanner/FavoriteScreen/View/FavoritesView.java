package com.example.mealsplanner.FavoriteScreen.View;

import com.example.mealsplanner.model.FavoriteMeal;

import java.util.List;

public interface FavoritesView {
    void displayFavorites(List<FavoriteMeal> favMeals);
    void showError(String message);
    void showLoading();
    void hideLoading();
    void showRemoveConfirmationDialog(Runnable onConfirm);
}
