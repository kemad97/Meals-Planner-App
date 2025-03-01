package com.example.mealsplanner.auth.login.Presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface LoginPresenter {
    void attemptLogin(String email, String password);
    void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask);
    void onGoogleSignInClick();
    void onSignUpClick();
    void onSkipClick();
    void onDestroy();
}
