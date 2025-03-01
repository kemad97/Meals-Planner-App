package com.example.mealsplanner.common;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mealsplanner.R;


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