package com.example.mealsplanner.HomeScreen.Presenter;

import com.example.mealsplanner.Data.MealRepository;
import com.example.mealsplanner.HomeScreen.View.HomeView;
import com.example.mealsplanner.common.NetworkUtils;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private final MealRepository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private HomeView view;


    public HomePresenterImpl(MealRepository repository, HomeView view) {
        this.repository = repository;
        this.view = view;
    }

    public HomePresenterImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(HomeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        compositeDisposable.clear();
        this.view = null;
    }

    @Override
    public void loadRandomMeal() {
        if (view == null) return;

        compositeDisposable.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meal -> view.displayRandomMeal(meal),
                                error -> view.showError(error.getMessage())
                        )
        );
    }

    @Override
    public void loadCategories() {
        if (view == null) return;

                compositeDisposable.add(
                        repository.getCategories()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        categories -> view.displayCategories(categories),
                                        error -> view.showError(error.getMessage())
                                )
                );

    }


    @Override
    public void loadAreas() {
        if (view == null) return;
        checkNetworkAndExecute(() ->
                compositeDisposable.add(
                        repository.getAreas()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        areas -> view.displayAreas(areas),
                                        error -> view.showError(error.getMessage())
                                )
                )
        );
    }


    @Override
    public void loadIngredients() {
        if (view == null) return;
        checkNetworkAndExecute(() ->
                compositeDisposable.add(
                        repository.getIngredients()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        ingredients -> view.displayIngredients(ingredients),
                                        error -> view.showError("Failed to load ingredients: " + error.getMessage())
                                )
                )
        );
    }

    @Override
    public void onCategorySelected(String category) {
        if (view == null) return;
        checkNetworkAndExecute(() ->
                compositeDisposable.add(
                        repository.getMealsByCategory(category)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        meals -> view.navigateToMealsList(category, null, null),
                                        error -> view.showError("Failed to load meals by category: " + error.getMessage())
                                )
                )
        );
    }

    @Override
    public void onAreaSelected(String area) {
        if (view == null) return;
        checkNetworkAndExecute(() ->
                compositeDisposable.add(
                        repository.getMealsByArea(area)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        meals -> view.navigateToMealsList(null, area, null),
                                        error -> view.showError("Failed to load meals by area: " + error.getMessage())
                                )
                )
        );
    }

    @Override
    public void onIngredientSelected(String ingredient) {
        if (view == null) return;
        checkNetworkAndExecute(() ->
                compositeDisposable.add(
                        repository.getMealsByIngredient(ingredient)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        meals -> view.navigateToMealsList(null, null, ingredient),
                                        error -> view.showError("Failed to load meals by ingredient: " + error.getMessage())
                                )
                )
        );
    }

    private void checkNetworkAndExecute(Runnable action) {
        if (view == null) return;

        if (!NetworkUtils.isNetworkAvailable(view.getContext())) {
            view.navigateToNoNetwork();;
        }

        action.run();
    }


}
