package com.example.mealsplanner.PlannerScreen.View;

public interface PlannerView {

    void showError(String message);
    void updateMealsList();
    void onDateSelected(String date);
}
