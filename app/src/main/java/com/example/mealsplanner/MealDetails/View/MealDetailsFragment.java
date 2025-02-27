package com.example.mealsplanner.MealDetails.View;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mealsplanner.Data.local.AppDatabase;
import com.example.mealsplanner.Data.local.MealDao;
import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.MealDetails.Presenter.MealDetailsPresenter;
import com.example.mealsplanner.MealDetails.Presenter.MealDetailsPresenterImpl;
import com.example.mealsplanner.R;
import com.example.mealsplanner.model.Meal;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private ImageView ivMealImage;
    private TextView tvMealName;
    private TextView tvCountry;
    private RecyclerView rvIngredients;
    private TextView tvInstructions;
    private WebView webView;
    private AppCompatButton btnFavorite;
    private MealDetailsPresenter presenter;
    private ImageButton btnCalendar;


    public MealDetailsFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);
        initViews(view);
        //initApiService();
        setupPresenter();
        loadMealDetails();


        // Inflate the layout for this fragment
        return view;
    }


    private void setupPresenter() {
        ApiService apiService = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiService.class);

        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();

        presenter = new MealDetailsPresenterImpl(apiService,mealDao);
        presenter.attachView(this);
    }

    private void initViews(View view) {
        ivMealImage = view.findViewById(R.id.ivMealImage);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvCountry = view.findViewById(R.id.tvCountry);
        tvInstructions = view.findViewById(R.id.tvInstructions);
        webView = view.findViewById(R.id.webView);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnCalendar = view.findViewById(R.id.btnCalendar);

        rvIngredients = view.findViewById(R.id.rvIngredients);
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        btnFavorite.setOnClickListener(v -> presenter.toggleFavorite());
        btnCalendar.setOnClickListener(v -> {presenter.addMealToCalendar();});
    }


    private void loadMealDetails() {
        if (getArguments() != null) {
            String mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
            presenter.loadMealDetails(mealId);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void displayMealDetails(Meal meal) {
        Glide.with(this)
                .load(meal.getImageUrl())
                .into(ivMealImage);
        tvMealName.setText(meal.getName());
        tvCountry.setText(meal.getArea());
        tvInstructions.setText(meal.getInstructions());

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(meal.getIngredientsList());
        rvIngredients.setAdapter(ingredientsAdapter);

//        webView.loadData(meal.getYoutubeUrl(), "text/html", "utf-8");
//        updateFavoriteStatus(meal.isFavorite());

        String youtubeUrl = meal.getYoutubeUrl();
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            String videoId = extractYoutubeVideoId(youtubeUrl);
            if (videoId != null) {
                String embedUrl = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadData(embedUrl, "text/html", "utf-8");
            }
        }

        updateFavoriteStatus(meal.isFavorite());
    }

    private String extractYoutubeVideoId(String url) {
        String pattern = "^(?:https?:\\/\\/)?(?:www\\.|m\\.)?(?:youtube\\.com\\/watch\\?v=|youtu.be\\/)([a-zA-Z0-9_-]{11}).*";
        java.util.regex.Pattern compiledPattern = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = compiledPattern.matcher(url);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        updateFavoriteStatus(isFavorite);
    }

    public void updateFavoriteStatus(boolean isFavorite) {
        btnFavorite.setText(isFavorite ? "Remove from Favorites" : "Add to Favorites");
    }

    public

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

}