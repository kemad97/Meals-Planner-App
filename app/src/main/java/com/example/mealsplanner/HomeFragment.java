package com.example.mealsplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.google.android.material.card.MaterialCardView;

import retrofit2.Retrofit;

public class HomeFragment extends Fragment {
    private ApiService apiService;
    private RecyclerView rvCategories;
    private RecyclerView rvCountries;
    private ImageView ivMealOfDay;
    private TextView tvMealOfDayName;
    private Meal mealOfTheDay;
    private MaterialCardView cardMealOfDay;

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
        cardMealOfDay = view.findViewById(R.id.cardMealOfDay);

        ivMealOfDay.setOnClickListener(v->handleMealOfDayClick(view));
        cardMealOfDay.setOnClickListener(v->handleCardMealOfDayClick(view));
    }

    private void handleCardMealOfDayClick(View view) {
        if (mealOfTheDay != null) {
            NavController navController = Navigation.findNavController(view);
            HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealOfTheDay.getId());
            navController.navigate(action);
        }
    }

    private void handleMealOfDayClick(View view) {
        if(mealOfTheDay!=null)
        {
            NavController navController= Navigation.findNavController(view);
            HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealOfTheDay.getId());
            navController.navigate(action);


        }
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
                        mealOfTheDay = response.getMeals().get(0);
                        tvMealOfDayName.setText(mealOfTheDay.getName());

                        Glide.with(this)
                                .load(mealOfTheDay.getImageUrl())
                                .into(ivMealOfDay);
                    }
                }, throwable -> {
                    // Handle error
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