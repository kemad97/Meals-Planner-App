package com.example.mealsplanner.PlannerScreen.View;

        import android.os.Bundle;
        import android.widget.CalendarView;
        import android.widget.ProgressBar;
        import android.widget.Toast;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import com.example.mealsplanner.Data.local.AppDatabase;
        import com.example.mealsplanner.Data.local.MealDao;
        import com.example.mealsplanner.PlannerScreen.Presenter.PlannerPresenter;
        import com.example.mealsplanner.PlannerScreen.Presenter.PlannerPresenterImpl;
        import com.example.mealsplanner.R;
        import com.example.mealsplanner.model.WeeklyPlanMeal;

        import java.util.Calendar;
        import java.util.List;
        import java.util.Locale;

        public class PlannerFragment extends Fragment implements PlannerView {
            private CalendarView calendarView;
            private RecyclerView rvPlannedMeals;
            private PlannerPresenter presenter;
            private MealDao mealDao;
            private PlannerAdapter adapter = new PlannerAdapter();

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                mealDao = AppDatabase.getInstance(requireContext()).mealDao();
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_planner, container, false);
                initViews(view);
                setupCalendar();
                initPresenter();
                return view;
            }

            private void initViews(View view) {
                calendarView = view.findViewById(R.id.calendarView);
                rvPlannedMeals = view.findViewById(R.id.rvPlannedMeals);

                rvPlannedMeals.setLayoutManager(new LinearLayoutManager(getContext()));
                rvPlannedMeals.setAdapter(adapter);

                adapter.setOnMealDeleteListener(meal -> {
                    presenter.removeMealFromDate(meal);
                });
            }

            private void setupCalendar() {
                // Set minimum date to today
                Calendar today = Calendar.getInstance();
                calendarView.setMinDate(today.getTimeInMillis());

                calendarView.setOnDateChangeListener((view, year, month, day) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, day);

                    if (selectedDate.getTimeInMillis() >= today.getTimeInMillis()) {
                        String date = String.format(Locale.getDefault(), "%d-%02d-%02d",
                            year, month + 1, day);
                        onDateSelected(date);
                    } else {
                        Toast.makeText(getContext(),
                            "Cannot select past dates", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void initPresenter() {
                presenter = new PlannerPresenterImpl(this, mealDao);

                Calendar today = Calendar.getInstance();
                String currentDate = String.format(Locale.getDefault(), "%d-%02d-%02d",
                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH) + 1,
                        today.get(Calendar.DAY_OF_MONTH));
                presenter.getMealsForDate(currentDate);
            }


            @Override
            public void showError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateMealsList(List<WeeklyPlanMeal> meals) {
                if (meals.isEmpty()) {
                    // Show empty state
                    rvPlannedMeals.setVisibility(View.GONE);
                } else {
                    rvPlannedMeals.setVisibility(View.VISIBLE);
                    adapter.setMeals(meals);
                }
            }

            @Override
            public void onDateSelected(String date) {
                presenter.getMealsForDate(date);
            }

            @Override
            public void onDestroyView() {
                super.onDestroyView();
                if (presenter != null) {
                    presenter.onDestroy();
                }
            }
        }