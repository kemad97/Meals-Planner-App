package com.example.mealsplanner.Data.remote;

import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class RemoteDataSourceImpl  implements RemoteDataSource{
    private final ApiService apiService;

    public RemoteDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<Meal> getRandomMeal() {
        return apiService.getRandomMeal()
                .map(mealResponse -> mealResponse.getMeals().get(0));
    }

    @Override
    public Single<List<CategoriesItem>> getCategories() {
        return apiService.getCategories()
                .map(response -> response.getCategoriesResponse());
    }

    @Override
    public Single<List<Area>> getAreas() {
        return apiService.getAreas("list")
                .map(response -> response.getAreasResponse());
    }


    @Override
    public Single<List<Meal.Ingredient>> getIngredients() {
        return apiService.getIngredients()
                .map(response -> response.getIngredients());
    }

    @Override
    public Single<List<Meal>> searchMeals(String query) {
        return apiService.searchMeals(query)
                .map(response -> response.getMeals());
    }
    @Override
    public Single<List<Meal>> getMealsByCategory(String category) {
        return apiService.filterByCategory(category)
                .map(response -> response.getMeals());
    }


    @Override
    public Single<List<Meal>> getMealsByArea(String area) {
        return apiService.filterByArea(area)
                .map(response -> response.getMeals());
    }

    @Override
    public Single<List<Meal>> getMealsByIngredient(String ingredient) {
        return apiService.filterByIngredient(ingredient)
                .map(response -> response.getMeals());
    }


    @Override
    public Single<Meal> getMealById(String id) {
        return apiService.getMealById(id)
                .map(response -> response.getMeals().get(0));
    }
}
