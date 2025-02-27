package com.example.mealsplanner.PlannerScreen.Presenter;

public interface PlannerPresenter {
    void addMealToDate(String mealId, String date);
    void getMealsForDate(String date);
    void onDestroy();
}
