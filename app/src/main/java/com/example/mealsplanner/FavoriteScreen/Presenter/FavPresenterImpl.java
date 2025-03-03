package com.example.mealsplanner.FavoriteScreen.Presenter;

import com.example.mealsplanner.Data.MealRepository;
import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.FavoriteScreen.View.FavoritesView;
import com.example.mealsplanner.model.FavoriteMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavPresenterImpl implements  FavPresenter {
    private final FavoritesView view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public FavPresenterImpl(FavoritesView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadFavorites() {
        view.showLoading();
        disposables.add(
                    repository.getFavorites()
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
        view.showRemoveConfirmationDialog(
                () -> disposables.add(
                       repository.removeFromFavoritesById(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> loadFavorites(),
                                throwable -> view.showError("Error removing favorite: " + throwable.getMessage())
                        ))
        );
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

}
