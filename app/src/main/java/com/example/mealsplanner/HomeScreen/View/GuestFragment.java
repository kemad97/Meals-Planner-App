package com.example.mealsplanner.HomeScreen.View;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.HomeScreen.Presenter.HomePresenter;
import com.example.mealsplanner.HomeScreen.Presenter.HomePresenterImpl;
import com.example.mealsplanner.MealDetails.View.IngredientsAdapter;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class GuestFragment extends Fragment implements HomeView {

    private HomePresenter presenter;
    private RecyclerView rvCategories;
    private RecyclerView rvCountries;
    private ImageView ivMealOfDay;
    private TextView tvMealOfDayName;
    private MaterialCardView cardMealOfDay;
    private Meal mealOfTheDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_guest, container, false);
        initViews(view);
        setupPresenter();
        setupMenu();
        loadData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMenu();
    }

    private void setupMenu() {
        View searchView = requireActivity().findViewById(R.id.searchFragment);
        View favoritesView = requireActivity().findViewById(R.id.favoritesFragment);
        View plannerView = requireActivity().findViewById(R.id.plannerFragment);

        View.OnClickListener menuClickListener = v -> showRegisterPrompt();

        searchView.setOnClickListener(menuClickListener);
        favoritesView.setOnClickListener(menuClickListener);
        plannerView.setOnClickListener(menuClickListener);
    }

    private void initViews(View view) {
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCountries = view.findViewById(R.id.rvCountries);
        ivMealOfDay = view.findViewById(R.id.ivMealOfDay);
        tvMealOfDayName = view.findViewById(R.id.tvMealOfDayName);
        cardMealOfDay = view.findViewById(R.id.cardMealOfDay);

        // Set click listeners that show login prompt
        View.OnClickListener loginPromptListener = v -> showRegisterPrompt();
        ivMealOfDay.setOnClickListener(loginPromptListener);
        cardMealOfDay.setOnClickListener(loginPromptListener);
    }

    private void setupPresenter() {
        ApiService apiService = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiService.class);

        presenter = new HomePresenterImpl(apiService);
        presenter.attachView(this);
    }

    private void loadData() {
        presenter.loadRandomMeal();
        presenter.loadCategories();
        presenter.loadIngredients();
    }

    private void showRegisterPrompt() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sign Up Required")
                .setMessage("Please sign up to access this feature")
                .setPositiveButton("Sign Up", (dialog, which) -> {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_guestFragment_to_registerActivity);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayRandomMeal(Meal meal) {
        mealOfTheDay = meal;
        tvMealOfDayName.setText(meal.getName());
        Glide.with(this)
                .load(meal.getImageUrl())
                .into(ivMealOfDay);
    }

    @Override
    public void displayCategories(List<CategoriesItem> categories) {
        CategoriesAdapter adapter = new CategoriesAdapter(categories,
                category -> showRegisterPrompt());
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void displayAreas(List<Area> areas) {
        AreasAdapter adapter = new AreasAdapter(areas,
                area -> showRegisterPrompt());
        rvCountries.setAdapter(adapter);
    }

    @Override
    public void displayIngredients(List<Meal.Ingredient> ingredients) {
        IngredientsAdapter adapter = new IngredientsAdapter(ingredients, ingredient ->
                presenter.onIngredientSelected(ingredient.getName())
        );
        rvCountries.setAdapter(adapter);    }

    @Override
    public void navigateToMealsList(String category, String area, String ing) {
        showRegisterPrompt();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }



    @Override
    public void navigateToNoNetwork() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_guestFragment_to_noNetworkFragment);
    }
}