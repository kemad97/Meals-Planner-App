package com.example.mealsplanner.auth.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealsplanner.MainActivity;
import com.example.mealsplanner.R;
import com.example.mealsplanner.auth.login.View.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPswd;
    private EditText etConfirmPswd;
    private Button btnSignup;
    private TextView txtSignin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ImageView imgGoogle;
    private GoogleSignInClient mGoogleSignInClient;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initUI();
        configureSignUpButton();
        configureSignInText();
        initGoogleSignIn();
        configureGoogleSignup();



    }

    private void initGoogleSignIn() {
        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        } catch (Exception e) {
            Toast.makeText(this, "Google Sign-In initialization failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleGoogleSignInResult(task);
                            Toast.makeText(this, "Google sign in successful", Toast.LENGTH_SHORT).show();
                        } else {
                            int resultCode = result.getResultCode();
                            String errorMessage = "Google sign in failed with result code: " + resultCode;
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
            );

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String idToken = account.getIdToken();
                if (idToken != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuthWithCredential(credential);
                } else {
                    Toast.makeText(this, "ID Token is null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ApiException e) {
            String errorMessage = "Google sign in failed: " + e.getStatusCode();
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithCredential(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Save user info
                        FirebaseUser user = mAuth.getCurrentUser();
                        navigateToMain();

                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    private void configureGoogleSignup() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void initUI() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPswd = findViewById(R.id.etPswd);
        btnSignup = findViewById(R.id.btnSignup);
        txtSignin = findViewById(R.id.txtSignin);
        progressBar = findViewById(R.id.progressBar);
        etConfirmPswd = findViewById(R.id.etConfirmPswd);
        imgGoogle = findViewById(R.id.imgGoogle);

    }

    private void configureSignInText() {
        txtSignin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void configureSignUpButton() {
        btnSignup.setOnClickListener(v -> registerUser());
    }

    private void  registerUser()
    {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPswd.getText().toString().trim();
        String confirmPassword = etConfirmPswd.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPswd.setError("Password is required");
            etPswd.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPswd.setError("Password should be at least 6 characters");
            etPswd.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPswd.setError("Passwords don't match");
            etConfirmPswd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Update profile
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (profileTask.isSuccessful()) {
                                        saveUserToSharedPreferences(email, password, name);
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void saveUserToSharedPreferences(String email, String password, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();
    }


}