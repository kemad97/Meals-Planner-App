package com.example.mealsplanner.ShowMeals.Presenter;

import android.annotation.SuppressLint;

import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.ShowMeals.View.ShowMealsView;
import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShowMealsPresenterImpl implements ShowMealsPresenter {
    private final ApiService apiService;
    private final MealDao mealDao;
    private ShowMealsView view;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ShowMealsPresenterImpl(ApiService apiService, MealDao mealDao) {
        this.apiService = apiService;
        this.mealDao = mealDao;
    }


    @Override
    public void attachView(ShowMealsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        this.view = null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadMealsByCategory(String category) {
        apiService.filterByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (view != null) {
                                view.hideLoading();
                                if (response.getMeals() != null) {
                                    view.showMeals(response.getMeals());
                                }

                            }
                        },
                        throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                view.showError("Failed to load meals: " + throwable.getMessage());
                            }
                        }
                );
    }

    @Override
    public void loadMealsByArea(String area) {
        if (view != null) {
            view.showLoading();
        }

        disposables.add(
                apiService.filterByArea(area)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        if (response.getMeals() != null) {
                                            view.showMeals(response.getMeals());
                                        }
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showError("Failed to load meals: " + throwable.getMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void toggleFavorite(Meal meal) {
        if (meal.isFavorite()) {
            disposables.add(
                    mealDao.removeFromFavoritesById(meal.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        if (view != null) {
                                            meal.setFavorite(false);
                                            view.updateFavoriteStatus(meal, false);
                                        }
                                    },
                                    throwable -> {
                                        if (view != null) {
                                            view.showError("Error removing from favorites");
                                        }
                                    }
                            )

            );

        }

        else {
            FavoriteMeal favoriteMeal = new FavoriteMeal(meal);
            disposables.add(
                    mealDao.addToFavorites(favoriteMeal)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        if (view != null) {
                                            meal.setFavorite(true);
                                            view.updateFavoriteStatus(meal, true);
                                        }
                                    },
                                    throwable -> {
                                        if (view != null) {
                                            view.showError("Error adding to favorites");
                                        }
                                    }
                            )
            );
        }

    }
}
