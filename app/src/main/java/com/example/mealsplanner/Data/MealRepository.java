package com.example.mealsplanner.Data;

import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.Meal;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<Meal> getRandomMeal();

    Single<List<CategoriesItem>> getCategories();

    Single<List<Area>> getAreas();

    Single<List<Meal.Ingredient>> getIngredients();

    Single<List<Meal>> searchMeals(String query);

    Single<List<Meal>> getMealsByCategory(String category);

    Single<List<Meal>> getMealsByArea(String area);

    Single<List<Meal>> getMealsByIngredient(String ingredient);

    Single<Meal> getMealById(String id);

    Single<List<FavoriteMeal>> getFavorites();

    Completable addToFavorites(FavoriteMeal meal);

    Completable removeFromFavorites(FavoriteMeal meal);

    Single<Boolean> isFavorite(String mealId);

    Single<List<WeeklyPlanMeal>> getWeeklyPlan(String date);

    Completable addToWeeklyPlan(WeeklyPlanMeal meal);

    Completable removeFromWeeklyPlan(WeeklyPlanMeal meal);

    Single<Boolean> isMealPlannedForDate(String mealId, String date);

    Completable removeFromFavoritesById(String mealId);
}
