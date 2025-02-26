package com.example.mealsplanner.HomeScreen.View;

import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface HomeView {
    void showError(String message);
    void displayRandomMeal(Meal meal);
    void displayCategories(List<CategoriesItem> categories);
    void displayAreas(List<Area> areas);
    void navigateToMealsList(String category, String area);

}
