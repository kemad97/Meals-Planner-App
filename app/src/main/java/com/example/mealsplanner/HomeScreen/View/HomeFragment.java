package com.example.mealsplanner.HomeScreen.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.HomeScreen.Presenter.HomePresenter;
import com.example.mealsplanner.HomeScreen.Presenter.HomePresenterImpl;
import com.example.mealsplanner.HomeScreen.View.HomeFragmentDirections;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;

import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView {
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
        loadData();
        return view;
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
        }
        else
        {
            ivSignout.setVisibility(View.GONE);
        }
    }

    private void signOut() {
        auth.signOut();
        googleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), task -> {

                    // Nav to login screen
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_homeFragment_to_guestFragment);
                });
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

    private void loadData() {
        presenter.loadRandomMeal();
        presenter.loadCategories();
        presenter.loadAreas();
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
        CategoriesAdapter adapter = new CategoriesAdapter(categories);
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void displayAreas(List<Area> areas) {
        AreasAdapter adapter = new AreasAdapter(areas);
        rvCountries.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}