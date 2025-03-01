package com.example.mealsplanner.auth.login.Presenter;

import android.text.TextUtils;

import com.example.mealsplanner.auth.login.View.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenterImpl  implements  LoginPresenter{

   private LoginView view;
   private FirebaseAuth firebaseAuth;

    public LoginPresenterImpl(LoginView view, FirebaseAuth firebaseAuth) {
        this.view = view;
        this.firebaseAuth = firebaseAuth;
    }


    @Override
    public void attemptLogin(String email, String password) {
        if (!validateInput(email, password)) {
            return;
        }

        view.showLoading();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        view.saveLoginState(email, password);
                        view.navigateToMain();
                    } else {
                        view.showError("Authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    private boolean validateInput(String email, String password) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            view.setEmailError("Email is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(password)) {
            view.setPasswordError("Password is required");
            isValid = false;
        }
        return isValid;
    }


    @Override
    public void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String idToken = account.getIdToken();
                if (idToken != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuthWithCredential(credential);
                } else {
                    view.showError("ID Token is null");
                }
            }
        } catch (ApiException e) {
            view.showError("Google sign in failed: " + e.getStatusCode());
        }
    }

    private void firebaseAuthWithCredential(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        view.navigateToMain();
                    } else {
                        view.showError("Authentication failed");
                    }
                });
    }

    @Override
    public void onGoogleSignInClick() {
        view.startGoogleSignIn();
    }

    @Override
    public void onSignUpClick() {
        view.navigateToRegister();
    }

    @Override
    public void onSkipClick() {
        view.navigateToGuestFragment();
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
