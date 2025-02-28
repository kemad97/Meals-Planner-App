package com.example.mealsplanner.model.response;

import com.example.mealsplanner.model.Meal;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse {
    @SerializedName("meals")
    private List<Meal.Ingredient> ingredients;

    public List<Meal.Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Meal.Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
