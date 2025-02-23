package com.example.mealsplanner.MealDetails.Presenter;

import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.MealDetails.View.MealDetailsView;
import com.example.mealsplanner.model.Meal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {
    private final ApiService apiService;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MealDetailsView view;
    private Meal currentMeal;

    public MealDetailsPresenterImpl(ApiService apiService) {
        this.apiService = apiService;
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
            currentMeal.setFavorite(!currentMeal.isFavorite());
            view.setFavorite(currentMeal.isFavorite());
        }
        else {
            view.showError("Meal not loaded");
        }
    }
}
