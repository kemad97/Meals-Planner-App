package com.example.mealsplanner.PlannerScreen.Presenter;

import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.PlannerScreen.View.PlannerView;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenterImpl implements PlannerPresenter {
    private PlannerView view;
    private MealDao mealDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PlannerPresenterImpl(PlannerView view, MealDao mealDao) {
        this.view = view;
        this.mealDao = mealDao;
    }

    @Override
    public void addMealToDate(String mealId, String date) {

        disposable.add(
                mealDao.addToWeeklyPlan(new WeeklyPlanMeal(mealId, date))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {

                                    getMealsForDate(date);
                                },
                                throwable -> {

                                    view.showError(throwable.getMessage());
                                }
                        )
        );

    }


    @Override
    public void getMealsForDate(String date) {
        disposable.add(
                mealDao.getWeeklyPlan(date, date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (view != null) {
                                        view.updateMealsList(meals);
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showError(throwable.getMessage());
                                    }
                                }
                        )
        );
    }
    @Override
    public void onDestroy() {
        disposable.clear();
        view = null;
    }
}