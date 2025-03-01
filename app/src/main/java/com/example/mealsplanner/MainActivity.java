package com.example.mealsplanner;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String IS_GUEST = "is_guest";
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();
        handleGuestMode();
    }


    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottomNavView);
            NavigationUI.setupWithNavController(bottomNav, navController);


            bottomNav.setOnItemSelectedListener(item -> {
                // Clear back stack and navigate to selected destination
                navController.popBackStack();
                navController.navigate(item.getItemId());
                return true;
            });
        }
    }

    private void handleGuestMode() {
        boolean isGuest = getIntent().getBooleanExtra(IS_GUEST, false);
        if (isGuest && navController != null) {
            navController.navigate(R.id.guestFragment);

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    // Navigate to login when back is pressed in guest mode
                    navController.navigate(R.id.loginActivity, null,
                            new NavOptions.Builder()
                                    .setPopUpTo(navController.getGraph().getStartDestination(), true)
                                    .build());
                }
            });
        }

        }
    }

