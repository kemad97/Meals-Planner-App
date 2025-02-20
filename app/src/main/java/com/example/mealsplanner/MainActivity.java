package com.example.mealsplanner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load HomeFragment
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        // Create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the FrameLayout with the new Fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit(); // Save the changes
    }
}