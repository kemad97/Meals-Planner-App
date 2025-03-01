package com.example.mealsplanner.auth.register.View;

public interface Registerview {
    void showLoading();
    void hideLoading();
    void showError(String message);
    void setInputError(String error);

    void navigateToMain();
    void navigateToLogin();
    void saveUserData(String email, String password, String name);
    void startGoogleSignIn();
}
