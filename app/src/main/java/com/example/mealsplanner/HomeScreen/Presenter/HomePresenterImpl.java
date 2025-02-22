package com.example.mealsplanner.HomeScreen.Presenter;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.Data.remote.NetworkCallback;
import com.example.mealsplanner.HomeScreen.View.CategoriesAdapter;
import com.example.mealsplanner.HomeScreen.View.HomeView;


import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter, NetworkCallback {

    private final ApiService apiService;
    private final CompositeDisposable compositeDisposable =new CompositeDisposable();
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


    @Override
    public <T> void onSuccessResult(List<T> result) {

    }

    @Override
    public void onFailureResult(String errMsg) {

    }
}
