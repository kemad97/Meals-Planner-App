package com.example.mealsplanner.SearchScreen.Presenter;

import com.example.mealsplanner.Data.MealRepository;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.SearchScreen.View.SearchView;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenterImpl implements SearchPresenter {

    private final MealRepository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SearchView view;

    public SearchPresenterImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(SearchView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        compositeDisposable.clear();
        this.view = null;
    }


    @Override
    public void loadCategories() {
        if (view == null) return;

        compositeDisposable.add(
                repository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response != null) {
                                        view.displayCategories(response);
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
                repository.getAreas()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response != null) {
                                        view.displayAreas(response);
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
                repository.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                IngredientResponse -> {
                                    if (IngredientResponse != null) {
                                        List <Meal.Ingredient> ingredients=IngredientResponse.stream()
                                                .map(meal -> new Meal.Ingredient(
                                                        meal.getName(),
                                                        meal.getMeasure()
                                                ))
                                                .collect(Collectors.toList());
                                        view.displayIngredients(ingredients);
                                    }
                                },
                                error -> view.showError(error.getMessage())
                        )
        );
    }

    @Override
    public void searchCategories(String query) {
        if (view == null) return;
        compositeDisposable.add(
                repository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response != null) {
                                        List<CategoriesItem> filteredCategories = response
                                                .stream()
                                                .filter(category ->
                                                        category.getStrCategory().toLowerCase()
                                                                .contains(query.toLowerCase()))
                                                .collect(Collectors.toList());
                                        view.displayCategories(filteredCategories);
                                    }
                                },
                                error -> view.showError(error.getMessage())
                        )
        );
    }

    @Override
    public void searchAreas(String query) {
        if (view == null) return;
        compositeDisposable.add(
                repository.getAreas()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response!= null) {
                                        List<Area> filteredAreas = response
                                                .stream()
                                                .filter(area ->
                                                        area.getName().toLowerCase()
                                                                .contains(query.toLowerCase()))
                                                .collect(Collectors.toList());
                                        view.displayAreas(filteredAreas);
                                    }
                                },
                                error -> view.showError(error.getMessage())
                        )
        );

    }

    @Override
    public void searchIngredients(String query) {
        if (view == null) return;
        compositeDisposable.add(
                repository.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response!= null) {
                                        List<Meal.Ingredient> filteredIngredients = response
                                                .stream()
                                                .filter(meal ->
                                                        meal.getName().toLowerCase()
                                                                .contains(query.toLowerCase()))
                                                .map(meal -> new Meal.Ingredient(
                                                        meal.getName(),
                                                        meal.getMeasure()
                                                ))
                                                .collect(Collectors.toList());
                                        view.displayIngredients(filteredIngredients);
                                    }
                                },
                                error -> view.showError(error.getMessage())
                        )
        );


    }


    @Override
    public void onCategorySelected(String category) {
        if (view == null) return;

        compositeDisposable.add(
                repository.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {

                                },
                                error -> {

                                    view.showError(error.getMessage());
                                }
                        )
        );
    }

    @Override
    public void onAreaSelected(String area) {
        if (view == null) return;
        compositeDisposable.add(
                repository.getMealsByArea(area)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {

                                },
                                error -> {

                                    view.showError(error.getMessage());
                                }
                        )
        );
    }


}
