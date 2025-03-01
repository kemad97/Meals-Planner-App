package com.example.mealsplanner.auth.login.View;

public interface LoginView {
    void showLoading();
    void hideLoading();
    void showError(String message);
    void setEmailError(String error);
    void setPasswordError(String error);
    void navigateToMain();
    void navigateToRegister();
    void navigateToGuestFragment();
    void saveLoginState(String email, String password);

    void startGoogleSignIn();
}