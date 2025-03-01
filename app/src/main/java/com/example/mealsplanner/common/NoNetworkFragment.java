package com.example.mealsplanner.common;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mealsplanner.R;

public class NoNetworkFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_network, container, false);

        Button btnRetry = view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(v -> {
            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.loginActivity, null,
                        new NavOptions.Builder()
                                .setPopUpTo(navController.getGraph().getStartDestination(), true)
                                .build());
            }
        });
        return view;
    }
}