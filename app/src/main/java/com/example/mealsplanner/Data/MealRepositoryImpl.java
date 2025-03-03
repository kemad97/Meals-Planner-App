package com.example.mealsplanner.Data;

import static okhttp3.internal.Internal.instance;

import android.content.Context;

import com.example.mealsplanner.Data.local.AppDatabase;
import com.example.mealsplanner.Data.local.LocalDataSource;
import com.example.mealsplanner.Data.local.LocalDataSourceImpl;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.Data.remote.RemoteDataSource;
import com.example.mealsplanner.Data.remote.RemoteDataSourceImpl;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.Meal;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRepositoryImpl implements MealRepository {
    private final LocalDataSource localDataSource;
    private final RemoteDataSource remoteDataSource;
    private static  MealRepositoryImpl instance;


    private MealRepositoryImpl(LocalDataSource localDataSource, RemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static MealRepository getInstance(Context context)
    {
        if(instance==null )
        {
            ApiService apiService=new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                    .create(ApiService.class);

            LocalDataSource    localDataSource=new LocalDataSourceImpl(AppDatabase.getInstance(context).mealDao() );
            RemoteDataSource remoteDataSource=new RemoteDataSourceImpl(apiService);
            instance=new MealRepositoryImpl(localDataSource,remoteDataSource);

        }
        return instance;

    }


    @Override
    public Single<Meal> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<List<CategoriesItem>> getCategories() {
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<List<Area>> getAreas() {
        return remoteDataSource.getAreas();
    }

    @Override
    public Single<List<Meal.Ingredient>> getIngredients() {
        return remoteDataSource.getIngredients();
    }

    @Override
    public Single<List<Meal>> searchMeals(String query) {
        return remoteDataSource.searchMeals(query);
    }

    @Override
    public Single<List<Meal>> getMealsByCategory(String category) {
        return remoteDataSource.getMealsByCategory(category);
    }

    @Override
    public Single<List<Meal>> getMealsByArea(String area) {
        return remoteDataSource.getMealsByArea(area);
    }

    @Override
    public Single<List<Meal>> getMealsByIngredient(String ingredient) {
        return remoteDataSource.getMealsByIngredient(ingredient);
    }

    @Override
    public Single<Meal> getMealById(String id) {
        return remoteDataSource.getMealById(id);
    }

    @Override
    public Single<List<FavoriteMeal>> getFavorites() {
        return localDataSource.getFavorites();
    }

    @Override
    public Completable addToFavorites(FavoriteMeal meal) {
        return localDataSource.addToFavorites(meal);
    }

    @Override
    public Completable removeFromFavorites(FavoriteMeal meal) {
        return localDataSource.removeFromFavorites(meal);
    }

    @Override
    public Single<Boolean> isFavorite(String mealId) {
        return localDataSource.isFavorite(mealId);
    }

    @Override
    public Single<List<WeeklyPlanMeal>> getWeeklyPlan(String date) {
        return localDataSource.getWeeklyPlan(date);
    }

    @Override
    public Completable addToWeeklyPlan(WeeklyPlanMeal meal) {
        return localDataSource.addToWeeklyPlan(meal);
    }

    @Override
    public Completable removeFromWeeklyPlan(WeeklyPlanMeal meal) {
        return localDataSource.removeFromWeeklyPlan(meal);
    }

    @Override
    public Single<Boolean> isMealPlannedForDate(String mealId, String date) {
        return localDataSource.isMealPlannedForDate(mealId, date);
    }

    @Override
    public Completable removeFromFavoritesById(String mealId) {
        return localDataSource.removeFromFavoritesById(mealId);
    }
}
