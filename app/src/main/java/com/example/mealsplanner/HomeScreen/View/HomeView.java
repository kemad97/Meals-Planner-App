package com.example.mealsplanner.HomeScreen.View;

import android.content.Context;

import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface HomeView {
    void showError(String message);
    void displayRandomMeal(Meal meal);
    void displayCategories(List<CategoriesItem> categories);
    void displayAreas(List<Area> areas);
    void displayIngredients(List<Meal.Ingredient> ingredients);
    void navigateToMealsList(String category, String area,String ing);

    Context getContext();
    void navigateToNoNetwork();



}
