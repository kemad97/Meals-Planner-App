package com.example.mealsplanner.SearchScreen.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealsplanner.Data.remote.ApiService;
import com.example.mealsplanner.MealDetails.View.IngredientsAdapter;
import com.example.mealsplanner.R;
import com.example.mealsplanner.SearchScreen.Presenter.SearchPresenter;
import com.example.mealsplanner.SearchScreen.Presenter.SearchPresenterImpl;
import com.example.mealsplanner.model.Area;
import com.example.mealsplanner.model.CategoriesItem;
import com.example.mealsplanner.model.Meal;
import com.google.android.material.chip.Chip;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class SearchFragment extends Fragment implements SearchView {

    private SearchPresenter presenter;
    private RecyclerView rvAreas;
    private RecyclerView rvCategories;
    private RecyclerView rvIngredients;
    private Chip chipCategory;
    private Chip chipArea;
    private Chip chipIngredient;
    private TextInputEditText etSearch;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    private static  final  long DEBOUNCE_TIMEOUT=300;



    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        setupPresenter();
        initTextWatcher();
        handleChipListeners();
        return view;
    }


    private void initViews(View view) {
        rvAreas = view.findViewById(R.id.rvAreas);
        rvCategories = view.findViewById(R.id.rvCategories);
        rvIngredients = view.findViewById(R.id.rvIngredients);
        chipCategory = view.findViewById(R.id.chipCategory);
        chipArea = view.findViewById(R.id.chipCountry);
        chipIngredient = view.findViewById(R.id.chipIngredient);
        etSearch = view.findViewById(R.id.etSearch);
    }

    private void initTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                compositeDisposable.add(Observable.just(s.toString())
                        .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s1 -> {
                            if (rvCategories.getVisibility() == View.VISIBLE) {
                                presenter.searchCategories(s1);
                            } else if (rvAreas.getVisibility() == View.VISIBLE) {
                                presenter.searchAreas(s1);
                            } else if (rvIngredients.getVisibility() == View.VISIBLE) {
                                presenter.searchIngredients(s1);
                            }
                        }) );
            }

        });
        
    }




    private void setupPresenter()
    {
        ApiService apiService=new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiService.class);

        presenter=new SearchPresenterImpl(apiService);
        presenter.attachView(this);
    }

    private void handleChipListeners()
    {
        chipCategory.setOnClickListener(v->{
            rvIngredients.setVisibility(View.GONE);
            rvAreas.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
            presenter.loadCategories();
        });

        chipArea.setOnClickListener(v->{
            rvIngredients.setVisibility(View.GONE);
            rvCategories.setVisibility(View.GONE);
            rvAreas.setVisibility(View.VISIBLE);
            presenter.loadAreas();
        });

        chipIngredient.setOnClickListener(v->{
            rvCategories.setVisibility(View.GONE);
            rvAreas.setVisibility(View.GONE);
            rvIngredients.setVisibility(View.VISIBLE);
            presenter.loadIngredients();
        });
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void displayCategories(List<CategoriesItem> categories) {
        CategoriesAdapter adapter=new CategoriesAdapter(categories);
        rvCategories.setAdapter(adapter);

    }

    @Override
    public void displayAreas(List<Area> areas) {
        AreasAdapter adapter=new AreasAdapter(areas);
        rvAreas.setAdapter(adapter);

    }

    @Override
    public void displayIngredients(List<Meal.Ingredient> ingredients) {
        IngredientsAdapter adapter=new IngredientsAdapter(ingredients);
        rvIngredients.setAdapter(adapter);

    }

    @Override
    public void displaySearchResults(List<Meal> meals) {

    }
}