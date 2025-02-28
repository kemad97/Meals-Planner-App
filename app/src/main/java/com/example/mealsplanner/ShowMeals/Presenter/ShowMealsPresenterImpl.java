package com.example.mealsplanner.ShowMeals.Presenter;

import android.annotation.SuppressLint;

import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.ShowMeals.View.ShowMealsView;
import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
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
    @Override
    public void loadMealsByCategory(String category) {
        if (view != null) {
            view.showLoading();
        }

        disposables.add(
                apiService.filterByCategory(category)
                        .flatMap(response -> {
                            if (response.getMeals() != null) {
                                return mealDao.getFavorites()
                                        .map(favorites -> {
                                            Set<String> favoriteIds = favorites.stream()
                                                    .map(FavoriteMeal::getId)
                                                    .collect(Collectors.toSet());

                                            List<Meal> meals = response.getMeals();

                                            for (Meal meal : response.getMeals()) {
                                                meal.setFavorite(favoriteIds.contains(meal.getId()));
                                            }
                                            return response.getMeals();
                                        });
                            }
                            return Single.just(new ArrayList<Meal>());
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showMeals(meals);
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
    public void loadMealsByIngredient(String ingredient) {
        if (view != null) {
            view.showLoading();
        }

        disposables.add(
                apiService.filterByIngredient(ingredient)
                        .flatMap(response -> {
                            if (response.getMeals() != null) {
                                return mealDao.getFavorites()
                                        .map(favorites -> {
                                            Set<String> favoriteIds = favorites.stream()
                                                    .map(FavoriteMeal::getId)
                                                    .collect(Collectors.toSet());

                                            List<Meal> meals = response.getMeals();
                                            for (Meal meal : meals) {
                                                meal.setFavorite(favoriteIds.contains(meal.getId()));
                                            }
                                            return meals;
                                        });
                            }
                            return Single.just(new ArrayList<Meal>());
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (view != null) {
                                        view.hideLoading();
                                        view.showMeals(meals);
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
 public void loadMealsByArea(String area) {
     if (view != null) {
         view.showLoading();
     }

     disposables.add(
         apiService.filterByArea(area)
             .flatMap(response -> {
                 if (response.getMeals() != null) {
                     return mealDao.getFavorites()
                         .map(favorites -> {
                             Set<String> favoriteIds = favorites.stream()
                                 .map(FavoriteMeal::getId)
                                 .collect(Collectors.toSet());

                             List<Meal> meals = response.getMeals();
                             for (Meal meal : meals) {
                                 meal.setFavorite(favoriteIds.contains(meal.getId()));
                             }
                             return meals;
                         });
                 }
                 return Single.just(new ArrayList<Meal>());
             })
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                 meals -> {
                     if (view != null) {
                         view.hideLoading();
                         view.showMeals(meals);
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
    public void showMeals(List<Meal> meals) {
        disposables.add(
            mealDao.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favorites -> {
                    Set<String> favoriteIds = favorites.stream()
                        .map(FavoriteMeal::getId)
                        .collect(Collectors.toSet());

                    for (Meal meal : meals) {
                        meal.setFavorite(favoriteIds.contains(meal.getId()));
                    }
                    if (view != null) {
                        view.showMeals(meals);
                    }
                }, throwable -> {
                    if (view != null) {
                        view.showError("Error loading favorite status");
                    }
                })
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
