package com.example.mealsplanner.FavoriteScreen.Presenter;

public interface FavPresenter {
    void loadFavorites();
    void removeFavorite(String mealId);
    void onDestroy();
}
