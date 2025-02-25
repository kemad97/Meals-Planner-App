package com.example.mealsplanner.HomeScreen.Presenter;

import com.example.mealsplanner.HomeScreen.View.HomeView;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import java.util.List;

public interface HomePresenter {
    void attachView(HomeView view);
    void detachView();
    void loadRandomMeal();
    void loadCategories();
    void loadAreas();
    void onCategorySelected(String category);
    void onAreaSelected(String area);
}
