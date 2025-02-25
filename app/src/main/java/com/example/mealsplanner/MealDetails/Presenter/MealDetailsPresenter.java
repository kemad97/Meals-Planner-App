package com.example.mealsplanner.MealDetails.Presenter;

import com.example.mealsplanner.MealDetails.View.MealDetailsView;

public interface MealDetailsPresenter {
    void attachView(MealDetailsView view);
    void detachView();
    void loadMealDetails(String mealId);
    void toggleFavorite();
}
