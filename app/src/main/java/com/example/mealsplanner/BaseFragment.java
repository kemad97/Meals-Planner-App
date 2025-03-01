package com.example.mealsplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public abstract class BaseFragment extends Fragment {
    protected void checkNetworkAndExecute(Runnable action) {
        if (!NetworkUtils.isNetworkAvailable(requireContext())) {
            navigateToNoNetwork();
        } else {
            action.run();
        }
    }

    private void navigateToNoNetwork() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_noNetworkFragment);
    }
}