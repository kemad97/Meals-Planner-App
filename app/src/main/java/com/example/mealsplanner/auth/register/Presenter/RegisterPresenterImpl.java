package com.example.mealsplanner.auth.register.Presenter;

import com.example.mealsplanner.auth.register.View.Registerview;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterPresenterImpl implements RegisterPresenter {
    private Registerview view;
    private FirebaseAuth firebaseAuth;

    public RegisterPresenterImpl(Registerview view, FirebaseAuth firebaseAuth) {
        this.view = view;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void attemptRegister(String name, String email, String password, String confirmPassword) {
        if (!validateInput(name, email, password, confirmPassword)) {
            return;
        }

        view.showLoading();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        view.saveUserData(email, password, name);
                        view.navigateToMain();
                    } else {
                        view.showError("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private boolean validateInput(String name, String email, String password, String confirmPassword) {
        boolean isValid = true;
        if (name.isEmpty()) {
            view.setInputError("Name is required");
            isValid = false;
        }
        if (email.isEmpty()) {
            view.setInputError("Email is required");
            isValid = false;
        }
        if (password.isEmpty()) {
            view.setInputError("Password is required");
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            view.setInputError("Confirm password is required");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            view.setInputError("Passwords do not match");
            isValid = false;
        }
        return isValid;
    }

    @Override

    public void onGoogleSignInClick() {
        view.startGoogleSignIn();
    }

    @Override

    public void onDestroy() {
        view = null;
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

    public void onSignInClick() {
        view.navigateToLogin();
    }
}
