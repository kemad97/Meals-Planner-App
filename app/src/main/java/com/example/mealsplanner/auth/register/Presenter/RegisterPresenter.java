package com.example.mealsplanner.auth.register.Presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface RegisterPresenter {
    void attemptRegister(String name, String email, String password, String confirmPassword);
    void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask);
    void onGoogleSignInClick();
    void onSignInClick();
    void onDestroy();
}
