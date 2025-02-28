package com.example.mealsplanner.PlannerScreen.View;

import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.List;

public interface PlannerView {

    void showError(String message);
    void updateMealsList(List<WeeklyPlanMeal> meals);
    void onDateSelected(String date);
}
