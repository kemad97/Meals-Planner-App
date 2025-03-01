package com.example.mealsplanner.FavoriteScreen.View;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mealsplanner.Data.local.AppDatabase;
import com.example.mealsplanner.FavoriteScreen.Presenter.FavPresenter;
import com.example.mealsplanner.FavoriteScreen.Presenter.FavPresenterImpl;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.FavoriteMeal;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesView {
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private FavPresenter presenter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        initViews(view);
        setupRecyclerView();
        initPresenter();
        loadFavorites();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rvFavorites);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new FavoritesAdapter(new ArrayList<>(), meal -> {
            presenter.removeFavorite(meal.getId());
        }, meal -> {
            NavController navController = Navigation.findNavController(requireView());
            FavoritesFragmentDirections.ActionFavoritesFragmentToMealDetailsFragment action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToMealDetailsFragment(meal.getId());
            navController.navigate(action);
        });


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(adapter);
    }

    private void initPresenter() {
        presenter = new FavPresenterImpl(this,
                AppDatabase.getInstance(requireContext()).mealDao());
    }

    private void loadFavorites() {
        presenter.loadFavorites();
    }

    @Override
    public void displayFavorites(List<FavoriteMeal> meals) {
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
    public void showRemoveConfirmationDialog(Runnable onConfirm) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Remove Favorite")
                .setMessage("Are you sure you want to remove this meal from favorites?")
                .setPositiveButton("Yes", (dialog, which) -> onConfirm.run())
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}