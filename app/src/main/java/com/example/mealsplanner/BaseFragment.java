package com.example.mealsplanner;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    protected void checkNetwork() {
        if (!com.examplecontent.Context.NetworkUtils.isNetworkAvailable(requireContext())) {
            new AlertDialog.Builder(requireContext())
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton("Retry", (dialog, which) -> checkNetwork())
                .setCancelable(false)
                .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkNetwork();
    }
}