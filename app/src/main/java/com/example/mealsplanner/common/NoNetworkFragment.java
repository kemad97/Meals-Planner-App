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
import android.widget.Toast;

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
                NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(navController.getGraph().getStartDestination(), true)
                    .build();
                navController.navigate(R.id.action_noNetworkFragment_to_homeFragment, null, navOptions);
            } else {
                Toast.makeText(requireContext(), "Please check your network settings.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}