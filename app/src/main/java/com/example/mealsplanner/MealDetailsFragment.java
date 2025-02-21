package com.example.mealsplanner;

import android.app.BroadcastOptions;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.View.IngredientsAdapter;
import com.example.mealsplanner.View.MealViewModel;
import com.example.mealsplanner.model.Meal;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MealDetailsFragment extends Fragment {

    private ImageView ivMealImage;
    private TextView tvMealName;
    private TextView tvCountry;
    private RecyclerView rvIngredients;
    private TextView tvInstructions;
    private WebView webView;
    private Button btnFavorite;
    private Meal currentMeal;
    private MealViewModel mealViewModel;
    private ApiService apiService;


    public MealDetailsFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        initViews(view);
        initApiService();
        loadMealDetails();


        // Inflate the layout for this fragment
        return view;
    }

    private void initApiService() {
        apiService = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiService.class);

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(MealViewModel.class)) {
                    return (T) new MealViewModel(apiService);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        };

        mealViewModel = new ViewModelProvider(this, factory).get(MealViewModel.class);
    }

    private void initViews(View view) {
        ivMealImage = view.findViewById(R.id.ivMealImage);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvCountry = view.findViewById(R.id.tvCountry);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        webView = view.findViewById(R.id.webView);
        btnFavorite = view.findViewById(R.id.btnFavorite);

        rvIngredients = view.findViewById(R.id.rvIngredients);
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        btnFavorite.setOnClickListener(v -> handleFavoriteClick());
    }

    private void loadMealDetails() {
        if (getArguments() != null) {
            String mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
            mealViewModel.getMealById(mealId).observe(getViewLifecycleOwner(), meal -> {
                if (meal != null) {
                    currentMeal = meal;
                    displayMealDetails(meal);
                }
            });
        }
    }

    private void displayMealDetails(Meal meal) {
        Glide.with(this)
                .load(meal.getImageUrl())
                .into(ivMealImage);
        tvMealName.setText(meal.getName());
        tvCountry.setText(meal.getArea());
        tvInstructions.setText(meal.getInstructions());

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(meal.getIngredientsList());
        rvIngredients.setAdapter(ingredientsAdapter);

        webView.loadData(meal.getYoutubeUrl(), "text/html", "utf-8");
        currentMeal = meal;
        ToggleFavouriteButton();
    }

    private void handleFavoriteClick() {
        if (currentMeal != null) {
            currentMeal.setFavorite(!currentMeal.isFavorite());
            ToggleFavouriteButton();
        }

    }

    private void ToggleFavouriteButton() {
        btnFavorite.setText(currentMeal.isFavorite() ? "Remove from Favorites" : "Add to Favorites");
    }

}