package com.example.mealsplanner.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mealsplanner.ApiService;
import com.example.mealsplanner.model.Meal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealViewModel extends ViewModel {
    private final ApiService apiService;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Meal> mealData= new MutableLiveData<>();


    public MealViewModel(ApiService apiService) {
       this.apiService = apiService;
    }

    public LiveData<Meal> getMealById(String id) {
        disposables.add(
                apiService.getMealById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                                        mealData.setValue(response.getMeals().get(0));
                                    }
                                },
                                throwable -> {
                                    // Handle error
                                }
                        )
        );
        return mealData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}
