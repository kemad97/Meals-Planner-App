package com.example.mealsplanner.PlannerScreen.Presenter;

import com.example.mealsplanner.Data.MealRepository;
import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.PlannerScreen.View.PlannerView;
import com.example.mealsplanner.model.WeeklyPlanMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannerPresenterImpl implements PlannerPresenter {
    private PlannerView view;
    private MealRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PlannerPresenterImpl(PlannerView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }



    @Override
    public void getMealsForDate(String date) {
        disposable.add(
                repository.getWeeklyPlan(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (view != null) {
                                        view.updateMealsList(meals);
                                    }
                                },
                                throwable -> {
                                        view.showError(throwable.getMessage());
                                }
                        )
        );
    }
    @Override
    public void onDestroy() {
        disposable.clear();
        view = null;
    }


    private void addMealToWeeklyPlan(String mealId, String date) {
        disposable.add(
                repository.addToWeeklyPlan(new WeeklyPlanMeal(mealId, date))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    getMealsForDate(date);
                                },
                                throwable -> view.showError(throwable.getMessage())
                        )
        );
    }
    @Override
    public void removeMealFromDate(WeeklyPlanMeal meal) {
        disposable.add(
                repository.removeFromWeeklyPlan(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    getMealsForDate(meal.getPlannedDate());
                                },
                                throwable -> {
                                    view.showError(throwable.getMessage());
                                }
                        )
        );

    }
}