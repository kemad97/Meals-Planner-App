package com.example.mealsplanner.SearchScreen.Presenter;

import com.example.mealsplanner.SearchScreen.View.SearchView;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface SearchPresenter {
    void attachView(SearchView view);
    void detachView();
    void loadCategories();
    void loadAreas();
    void onCategorySelected(String category);
    void onAreaSelected(String area);

    void loadIngredients();
    void searchCategories(String query);
    void searchAreas(String query);
    void searchIngredients(String query);
}
