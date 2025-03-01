package com.example.mealsplanner.SearchScreen.View;

import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface SearchView {
    void showError(String message);
    void displayCategories(List<CategoriesItem> categories);
    void displayAreas(List<Area> areas);
    void displayIngredients(List<Meal.Ingredient> ingredients);
    void navigateToNoNetwork();

}
