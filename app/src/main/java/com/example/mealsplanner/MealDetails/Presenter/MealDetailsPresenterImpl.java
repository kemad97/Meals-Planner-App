package com.example.mealsplanner.MealDetails.Presenter;

import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.MealDetails.View.MealDetailsView;
import com.example.mealsplanner.model.FavoriteMeal;
import com.example.mealsplanner.model.Meal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {
    private final ApiService apiService;
    private final MealDao mealDao;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MealDetailsView view;
    private Meal currentMeal;

    public MealDao getMealDao() {
        return mealDao;
    }

    public MealDetailsPresenterImpl(ApiService apiService, MealDao mealDao) {
        this.apiService = apiService;
        this.mealDao = mealDao;

    }

    @Override
    public void attachView(MealDetailsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        this.view = null;
    }

    @Override
    public void loadMealDetails(String mealId) {
        disposables.add(
                apiService.getMealById(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                        currentMeal = response.getMeals().get(0);
                                        if (view != null) {
                                            view.displayMealDetails(currentMeal);
                                        }
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showError(throwable.getMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void toggleFavorite() {
        if (currentMeal != null) {
            if (currentMeal.isFavorite()) {
                disposables.add(
                        mealDao.removeFromFavoritesById(currentMeal.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            currentMeal.setFavorite(false);
                                            view.setFavorite(false);
                                        },
                                        throwable -> view.showError("Error removing from favorites")
                                )
                );
            } else {
                FavoriteMeal favoriteMeal = new FavoriteMeal(currentMeal);
                disposables.add(
                        mealDao.addToFavorites(favoriteMeal)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            currentMeal.setFavorite(true);
                                            view.setFavorite(true);
                                        },
                                        throwable -> view.showError("Error adding to favorites")
                                )
                );
            }

        }
    }

    @Override
    public void addMealToCalendar() {
        
    }
}

