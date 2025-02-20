package com.example.mealsplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mealsplanner.model.Meal;

import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView rvCategories;
    private RecyclerView rvCountries;
    private ImageView ivMealOfDay;
    private TextView tvMealOfDayName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupApiService();
        loadData();
        return view;
    }

    private void initViews(View view) {
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCountries = view.findViewById(R.id.rvCountries);
        ivMealOfDay = view.findViewById(R.id.ivMealOfDay);
        tvMealOfDayName = view.findViewById(R.id.tvMealOfDayName);
    }

    private void setupApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void loadData() {
        // Load random meal
        apiService.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getMeals() != null && !response.getMeals().isEmpty()) {
                        Meal meal = response.getMeals().get(0);
                        tvMealOfDayName.setText(meal.getName());

                        Glide.with(this)
                                .load(meal.getImageUrl())
                                .into(ivMealOfDay);
                    }
                }, throwable -> {
                });

        // Load categories
        apiService.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    CategoriesAdapter categoriesAdapter = new CategoriesAdapter(response.getCategories());
                    rvCategories.setAdapter(categoriesAdapter);
                }, throwable -> {
                    // Handle error
                });
        // Load areas
        apiService.getAreas("list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // Set up countries adapter
                }, throwable -> {
                    // Handle error
                });
    }
}