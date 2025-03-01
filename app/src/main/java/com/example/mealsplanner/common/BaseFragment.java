package com.example.mealsplanner.common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mealsplanner.R;


public abstract class BaseFragment extends Fragment implements NetworkMonitor.NetworkCallback {
    private NetworkMonitor networkMonitor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkMonitor = new NetworkMonitor(requireContext(), this);
        getLifecycle().addObserver(networkMonitor);

        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.noNetworkFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLifecycle().removeObserver(networkMonitor);
    }

    @Override
    public void onNetworkAvailable() {
    }
}
