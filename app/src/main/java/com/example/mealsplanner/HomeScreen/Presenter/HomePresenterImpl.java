package com.example.mealsplanner.HomeScreen.Presenter;

import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.HomeScreen.View.HomeView;
import com.example.mealsplanner.common.NetworkUtils;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private final ApiService apiService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private HomeView view;



    public HomePresenterImpl(ApiService apiService) {
        this.apiService = apiService;
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
                apiService.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {

                                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                        view.displayRandomMeal(response.getMeals().get(0));
                                    }
                                },
                                error -> {

                                    view.showError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loadCategories() {
        if (view == null) return;


        compositeDisposable.add(
                apiService.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {

                                    if (response.getCategoriesResponse() != null) {
                                        view.displayCategories(response.getCategoriesResponse());
                                    }
                                },
                                error -> {

                                    view.showError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loadAreas() {
        if (view == null) return;


        compositeDisposable.add(
                apiService.getAreas("list")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {

                                    if (response.getAreasResponse() != null) {
                                        view.displayAreas(response.getAreasResponse());
                                    }
                                },
                                error -> {

                                    view.showError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loadIngredients() {
        if (view == null) return;
        compositeDisposable.add(
                apiService.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getIngredients() != null) {
                                        view.displayIngredients(response.getIngredients());
                                    }
                                },
                                error -> view.showError("Failed to load ingredients: " + error.getMessage())
                        )
        );
    }

    @Override
    public void onCategorySelected(String category) {
        if (view == null) return;


        compositeDisposable.add(
                apiService.filterByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getMeals() != null) {
                                        view.navigateToMealsList(category, null, null);
                                    }
                                },
                                error -> view.showError("Failed to load meals by category: " + error.getMessage())
                        )
        );
    }

    @Override
    public void onAreaSelected(String area) {
        if (view == null) return;


        compositeDisposable.add(
                apiService.filterByArea(area)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getMeals() != null) {
                                        view.navigateToMealsList(null, area, null);
                                    }
                                },
                                error -> view.showError("Failed to load meals by area: " + error.getMessage())
                        )
        );
    }

    @Override
    public void onIngredientSelected(String ing) {
        if (view == null) return;
        compositeDisposable.add(
                apiService.filterByIngredient(ing)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getMeals() != null) {
                                        view.navigateToMealsList(null, null, ing);
                                    }
                                },
                                error -> view.showError("Failed to load meals by area: " + error.getMessage())
                        )
        );

    }

    private void checkNetworkAndExecute(Runnable action) {
        if (view == null) return;

        if (!NetworkUtils.isNetworkAvailable(view.getContext())) {
            view.navigateToNoNetwork();
            return;
        }

        action.run();
    }


}
