package com.example.mealsplanner;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("search.php")
    Single<MealResponse> searchMeals(@Query("s") String query);

    @GET("filter.php")
    Single<MealResponse> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Single<MealResponse> filterByArea(@Query("a") String area);

    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String id);

    @GET("list.php")
    Single<AreaResponse> getAreas(@Query("a") String list);
}