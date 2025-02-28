package com.example.mealsplanner.FavoriteScreen.Presenter;

import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.FavoriteScreen.View.FavoritesView;
import com.example.mealsplanner.model.FavoriteMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavPresenterImpl implements  FavPresenter {
    private final FavoritesView view;
    private final MealDao mealDao;
    private final CompositeDisposable disposables;

    public FavPresenterImpl(FavoritesView view, MealDao mealDao) {
        this.view = view;
        this.mealDao = mealDao;
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadFavorites() {
        view.showLoading();
        disposables.add(
                mealDao.getFavorites()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    view.hideLoading();
                                    view.displayFavorites(meals);
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }
                        )
        );
    }

    @Override
    public void removeFavorite(String mealId) {
        disposables.add(mealDao.removeFromFavoritesById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::loadFavorites,
                        throwable -> view.showError("Error removing favorite: " + throwable.getMessage())
                ));


    }

    @Override
    public void onDestroy() {
        disposables.clear();

    }

}
