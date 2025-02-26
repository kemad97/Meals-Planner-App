package com.example.mealsplanner.ShowMeals.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mealsplanner.Data.local.AppDatabase;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.R;
import com.example.mealsplanner.ShowMeals.Presenter.ShowMealsPresenter;
import com.example.mealsplanner.ShowMeals.Presenter.ShowMealsPresenterImpl;
import com.example.mealsplanner.model.Meal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ShowMealsFragment extends Fragment implements ShowMealsView {

    private RecyclerView rvMeals;
    private ProgressBar progressBar;
    private ShowMealsAdapter adapter;
    private ShowMealsPresenter presenter;

    public ShowMealsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupPresenter();
        loadMeals();
    }

    private void loadMeals() {
            if (getArguments() != null) {
                ShowMealsFragmentArgs args = ShowMealsFragmentArgs.fromBundle(getArguments());
                String category = args.getCategory();
                String area = args.getArea();

                if (category != null && !category.isEmpty()) {
                    presenter.loadMealsByCategory(category);
                } else if (area != null && !area.isEmpty()) {
                    presenter.loadMealsByArea(area);
                }
            }
        }


    private void setupPresenter() {
        ApiService apiService = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiService.class);

        presenter = new ShowMealsPresenterImpl(
                apiService,
                AppDatabase.getInstance(requireContext()).mealDao()
        );
        presenter.attachView(this);

    }

    private void initViews(View view) {
        rvMeals = view.findViewById(R.id.rvMeals);
        progressBar = view.findViewById(R.id.progressBar);

        adapter = new ShowMealsAdapter(new ArrayList<>(), new ShowMealsAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                navigateToDetails(meal);
            }

            @Override
            public void onFavoriteClick(Meal meal, boolean isFavorite) {
                presenter.toggleFavorite(meal);
            }
        });

        rvMeals.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvMeals.setAdapter(adapter);
    }

    private void navigateToDetails(Meal meal) {
        ShowMealsFragmentDirections.ActionShowMealsFragmentToMealDetailsFragment action =
                ShowMealsFragmentDirections.actionShowMealsFragmentToMealDetailsFragment(meal.getId());
        Navigation.findNavController(requireView()).navigate(action);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_meals, container, false);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showMeals(List<Meal> meals) {
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateFavoriteStatus(Meal meal, boolean isFavorite) {
        if (adapter != null) {
            int position = adapter.getMealPosition(meal);
            if (position != -1) {
                meal.setFavorite(isFavorite);
                adapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}