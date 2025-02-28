package com.example.mealsplanner;

import android.os.Bundle;

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
    public static final String EXTRA_IS_GUEST = "is_guest";
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

            //  clear the back stack
            NavOptions navOptions = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(navController.getGraph().getStartDestination(), false)
                    .build();

            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                try {
                    navController.navigate(itemId, null, navOptions);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            });
        }
    }

    private void handleGuestMode() {
        boolean isGuest = getIntent().getBooleanExtra(EXTRA_IS_GUEST, false);
        if (isGuest && navController != null) {
            navController.navigate(R.id.guestFragment);

        }
    }

}