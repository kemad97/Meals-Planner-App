package com.example.mealsplanner.Data.local;

import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface LocalDataSource {
    Single<List<FavoriteMeal>> getFavorites();
    Completable addToFavorites(FavoriteMeal meal);
    Completable removeFromFavorites(FavoriteMeal meal);
    Completable removeFromFavoritesById(String mealId);

    Single<Boolean> isFavorite(String mealId);
    Single<List<WeeklyPlanMeal>> getWeeklyPlan(String date);
    Completable addToWeeklyPlan(WeeklyPlanMeal meal);
    Completable removeFromWeeklyPlan(WeeklyPlanMeal meal);
    Single<Boolean> isMealPlannedForDate(String mealId, String date);
}