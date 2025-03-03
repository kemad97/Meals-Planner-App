package com.example.mealsplanner.Data.local;

import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class LocalDataSourceImpl  implements LocalDataSource{
    private final MealDao mealDao;

    public LocalDataSourceImpl(MealDao mealDao) {
        this.mealDao = mealDao;
    }

    @Override
    public Single<List<FavoriteMeal>> getFavorites() {
        return mealDao.getFavorites();
    }

    @Override
    public Completable addToFavorites(FavoriteMeal meal) {
        return mealDao.addToFavorites(meal);
    }

    @Override
    public Completable removeFromFavorites(FavoriteMeal meal) {
        return mealDao.removeFromFavorites(meal);
    }

    @Override
    public Completable removeFromFavoritesById(String mealId) {
        return mealDao.removeFromFavoritesById(mealId);
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return mealDao.isFavorite(mealId);
    }
    @Override
    public Single<List<WeeklyPlanMeal>> getWeeklyPlan(String date) {
        return mealDao.getWeeklyPlan(date);
    }
    @Override
    public Completable addToWeeklyPlan(WeeklyPlanMeal meal) {
        return mealDao.addToWeeklyPlan(meal);
    }

    @Override
    public Completable removeFromWeeklyPlan(WeeklyPlanMeal meal) {
        return mealDao.removeFromWeeklyPlan(meal);
    }


    @Override
    public Single<Boolean> isMealPlannedForDate(String mealId, String date) {
        return mealDao.isMealPlannedForDate(mealId, date);
    }
}
