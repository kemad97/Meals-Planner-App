package com.example.mealsplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NoNetworkFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_network, container, false);

        Button btnRetry = view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(v -> {
            if (NetworkUtils.isNetworkAvailable(requireContext())) {
                // go to previous fragment
                Navigation.findNavController(v).navigateUp();
            }
        });

        return view;
    }
}