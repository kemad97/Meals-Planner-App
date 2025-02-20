package com.example.mealsplanner.Database;

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

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE meal_id = :mealId)")
    Single<Boolean> isFavorite(String mealId);

    // Weekly Plan
    @Query("SELECT * FROM weekly_plan WHERE planned_date BETWEEN :startDate AND :endDate")
    Single<List<WeeklyPlanMeal>> getWeeklyPlan(String startDate, String endDate);

    @Insert
    Completable addToWeeklyPlan(WeeklyPlanMeal meal);

    @Delete
    Completable removeFromWeeklyPlan(WeeklyPlanMeal meal);
}
