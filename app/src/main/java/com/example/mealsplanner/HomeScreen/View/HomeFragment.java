package com.example.mealsplanner.HomeScreen.View;


import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealsplanner.common.BaseFragment;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.HomeScreen.Presenter.HomePresenter;
import com.example.mealsplanner.HomeScreen.Presenter.HomePresenterImpl;
import com.example.mealsplanner.MealDetails.View.IngredientsAdapter;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeFragment extends BaseFragment implements HomeView {
    private HomePresenter presenter;
    private RecyclerView rvCategories;
    private RecyclerView rvCountries;
    private ImageView ivMealOfDay;
    private TextView tvMealOfDayName;
    private MaterialCardView cardMealOfDay;
    private Meal mealOfTheDay;
    private ImageView ivSignout;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setupFirebase();
        setupPresenter();
       // loadData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void setupFirebase() {
        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        checkIfUserSigned();
    }

    private void checkIfUserSigned() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            ivSignout.setVisibility(View.VISIBLE);
            ivSignout.setOnClickListener(v -> signOut());
        } else {
            ivSignout.setVisibility(View.GONE);
        }
    }

private void signOut() {
    auth.signOut();
    googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity(), task -> {
                NavController navController = Navigation.findNavController(requireView());
                // Clear entire back stack and navigate to login
                navController.navigate(R.id.loginActivity, null,
                        new NavOptions.Builder()
                                .setPopUpTo(navController.getGraph().getStartDestination(), true)
                                .build());
            });
}
    private void initViews(View view) {
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCountries = view.findViewById(R.id.rvCountries);
        ivMealOfDay = view.findViewById(R.id.ivMealOfDay);
        tvMealOfDayName = view.findViewById(R.id.tvMealOfDayName);
        cardMealOfDay = view.findViewById(R.id.cardMealOfDay);
        ivSignout = view.findViewById(R.id.ivSignout);

        View.OnClickListener mealClickListener = v -> handleMealOfDayClick(v);
        ivMealOfDay.setOnClickListener(mealClickListener);
        cardMealOfDay.setOnClickListener(mealClickListener);
    }

    protected void loadData() {
        checkNetworkAndExecute(() -> {
            presenter.loadRandomMeal();
            presenter.loadCategories();
            presenter.loadAreas();
            presenter.loadIngredients();
        });
    }

    private void handleMealOfDayClick(View view) {
        checkNetworkAndExecute(() -> {
            if (mealOfTheDay != null) {
                NavController navController = Navigation.findNavController(view);
                HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                        HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealOfTheDay.getId());
                navController.navigate(action);
            }
        });
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
                category -> presenter.onCategorySelected(category.getStrCategory()));
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void displayAreas(List<Area> areas) {
        AreasAdapter adapter = new AreasAdapter(areas,
                area -> presenter.onAreaSelected(area.getName()));
        rvCountries.setAdapter(adapter);
    }

    @Override
    public void displayIngredients(List<Meal.Ingredient> ingredients) {
        IngredientsAdapter adapter = new IngredientsAdapter(ingredients,
                ingredient -> presenter.onIngredientSelected(ingredient.getName()));
        rvCountries.setAdapter(adapter);
    }

    @Override
    public void navigateToMealsList(String category, String area, String ing) {
        NavController navController = Navigation.findNavController(requireView());
        HomeFragmentDirections.ActionHomeFragmentToShowMealsFragment action =
                HomeFragmentDirections.actionHomeFragmentToShowMealsFragment(category, area, ing);
        navController.navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void navigateToNoNetwork() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_noNetworkFragment);
    }
}