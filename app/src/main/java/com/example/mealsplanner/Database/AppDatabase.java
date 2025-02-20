package com.example.mealsplanner.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.WeeklyPlanMeal;

@Database(entities = {FavoriteMeal.class, WeeklyPlanMeal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static volatile AppDatabase INSTANCE;

    public abstract MealDao mealDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "meal_planner_db").build();
                }
            }
        }
        return INSTANCE;
    }
}