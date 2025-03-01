package com.example.mealsplanner.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class NetworkMonitor implements DefaultLifecycleObserver {
    private final Context context;
    private final NetworkCallback callback;
    private final ConnectivityManager connectivityManager;

    public NetworkMonitor(Context context, NetworkCallback callback) {
        this.context = context;
        this.callback = callback;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        registerNetworkCallback();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        unregisterNetworkCallback();
    }

    private void registerNetworkCallback() {
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                callback.onNetworkAvailable();
            }

            @Override
            public void onLost(@NonNull Network network) {
                callback.onNetworkLost();
            }
        };

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void unregisterNetworkCallback() {
        try {
            connectivityManager.unregisterNetworkCallback(new ConnectivityManager.NetworkCallback());
        } catch (Exception ignored) {}
    }

    public interface NetworkCallback {
        void onNetworkAvailable();
        void onNetworkLost();
    }
}