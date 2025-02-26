package com.example.mealsplanner.ShowMeals.Presenter;

import com.example.mealsplanner.ShowMeals.View.ShowMealsView;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface ShowMealsPresenter {
    void attachView(ShowMealsView view);
    void detachView();
    void loadMealsByCategory(String category);
    void loadMealsByArea(String area);

    void showMeals(List<Meal> meals);

    void toggleFavorite(Meal meal);
}
