package com.example.mealsplanner.PlannerScreen.Presenter;

import com.example.mealsplanner.model.WeeklyPlanMeal;

public interface PlannerPresenter {
    void getMealsForDate(String date);
    void onDestroy();

    void removeMealFromDate(WeeklyPlanMeal meal);
}
