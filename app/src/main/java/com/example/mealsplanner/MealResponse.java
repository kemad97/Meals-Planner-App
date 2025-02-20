package com.example.mealsplanner;


import com.example.mealsplanner.model.Meal;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }
}

