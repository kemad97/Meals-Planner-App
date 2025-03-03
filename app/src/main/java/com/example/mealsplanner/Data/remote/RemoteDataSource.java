package com.example.mealsplanner.Data.remote;

import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface RemoteDataSource {
    Single<Meal> getRandomMeal();
    Single<List<CategoriesItem>> getCategories();
    Single<List<Area>> getAreas();
    Single<List<Meal.Ingredient>> getIngredients();
    Single<List<Meal>> searchMeals(String query);
    Single<List<Meal>> getMealsByCategory(String category);
    Single<List<Meal>> getMealsByArea(String area);
    Single<List<Meal>> getMealsByIngredient(String ingredient);
    Single<Meal> getMealById(String id);
}