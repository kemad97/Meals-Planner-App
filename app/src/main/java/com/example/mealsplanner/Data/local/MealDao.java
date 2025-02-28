package com.example.mealsplanner.Data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {
    @Query("SELECT * FROM favorite_meals")
    Single<List<FavoriteMeal>> getFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToFavorites(FavoriteMeal meal);

    @Delete
    Completable removeFromFavorites(FavoriteMeal meal);
    @Query("DELETE FROM favorite_meals WHERE meal_id = :mealId")
    Completable removeFromFavoritesById(String mealId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE meal_id = :mealId)")
    Single<Boolean> isFavorite(String mealId);

    // Weekly Plan
    @Query("SELECT * FROM weekly_plan WHERE planned_date = :date")
    Single<List<WeeklyPlanMeal>> getWeeklyPlan(String date);

    @Query("SELECT EXISTS(SELECT 1 FROM weekly_plan WHERE meal_id = :mealId AND planned_date = :date)")
    Single<Boolean> isMealPlannedForDate(String mealId, String date);

    @Insert
    Completable addToWeeklyPlan(WeeklyPlanMeal meal);

    @Delete
    Completable removeFromWeeklyPlan(WeeklyPlanMeal meal);
}
