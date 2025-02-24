package com.example.mealsplanner.SearchScreen.Presenter;

import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.Data.remote.NetworkCallback;
import com.example.mealsplanner.SearchScreen.View.SearchView;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenterImpl implements SearchPresenter {

    private final ApiService apiService;
    private final CompositeDisposable compositeDisposable =new CompositeDisposable();
    private SearchView view;

    public SearchPresenterImpl(ApiService apiService) {
        this.apiService = apiService;
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
                apiService.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getCategories() != null) {
                                        view.displayCategories(response.getCategories());
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
                                    if (response.getAreas() != null) {
                                        view.displayAreas(response.getAreas());
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
//        compositeDisposable.add(
//                apiService.getIngredients()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                response -> {
//                                    if (response.getMeals() != null) {
//                                        view.displayIngredients(response.getMeals());
//                                    }
//                                },
//                                error -> {
//                                    view.showError(error.getMessage());
//                                }
//                        )
//        );
    }

    @Override
    public void searchCategories(String query) {
        if (view == null) return;
        compositeDisposable.add(
                apiService.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getCategories() != null) {
                                        List<CategoriesItem> filteredCategories = response.getCategories()
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

    }

    @Override
    public void searchIngredients(String query) {

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
                apiService.filterByArea(area)
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
